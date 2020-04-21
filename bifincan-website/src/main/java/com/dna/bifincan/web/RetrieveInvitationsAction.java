package com.dna.bifincan.web;

import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.model.user.UserInvitation;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.MailService;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;
import java.io.Serializable;
import java.util.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.encoding.PasswordEncoder;

@Named(value = "retrieveInvitationsAction")
@Scope("request")
public class RetrieveInvitationsAction implements Serializable {
	private static final long serialVersionUID = 6515635743658158582L;

	private static final Logger log = LoggerFactory.getLogger(RetrieveInvitationsAction.class);
    
    @NotNull
    @NotEmpty
    @Email
    private String email;
    
    @Inject
    UserService userService;
    
    @Inject
    MailService mailService;
    
    @Inject
    PasswordEncoder encoder;
    
    @Inject
    private ConfigurationService configurationService;
    
    public String getBaseUrlWithPath() {
        String applicationCanonicalURL = "https://www.bifincan.com";
        ExternalContext contextUrl = FacesContext.getCurrentInstance().getExternalContext();
        String ctxPath = contextUrl.getRequestContextPath();
        Configuration canonicalUrlConfig = configurationService.find((ConfigurationType.APPLICATION_CANONICAL_URL.getKey()));
        if (canonicalUrlConfig != null) {
            applicationCanonicalURL = canonicalUrlConfig.getValue();
        }
        String baseUrlWithPath = applicationCanonicalURL + ctxPath;
        return baseUrlWithPath;
    }
    
    public void retrieve() {
        if (log.isDebugEnabled()) {
            log.debug("call retrieve@" + this.email);
        }
        User _user = userService.findUserByEmail(this.email);
        
        if (_user != null) {
            FacesUtils.addWarningMessage(FacesUtils.getBundleKey("messages", "error.noWaitingInviations"));
            return;
        } else {
            List<UserInvitation> _invitations = userService.findInvitationsByEmail(this.email);
            if (!_invitations.isEmpty()) {
                
                List<InvitationResultWrapper> _results = new ArrayList<InvitationResultWrapper>();
                
                for (UserInvitation _invitation : _invitations) {
                    String encodedVal = generateConfirmationKey(this.email, _invitation.getInviter().getUsername());
                    String link = getBaseUrlWithPath() + "/yeni-uye/" + this.email + "/" + _invitation.getId() + "/" + encodedVal;
                    _invitation.setLastInvitationTime(new Date());
                    userService.saveUserInvitation(_invitation);
                    
                    _results.add(new InvitationResultWrapper(_invitation.getInviter().getFirstName(), _invitation.getInviter().getLastName(), link, _invitation.getOptedOut()));
                }
                
                Map model = new HashMap();
                model.put("baseurl", getBaseUrlWithPath());
                model.put("invitations", _results);
                if (log.isDebugEnabled()) {
                    log.debug("invitations @" + _results);
                }
                
                mailService.sendEmail(this.email, "bifincan davetiyelerin", "retrieve-invitations", model);
                FacesUtils.addSuccessMessage(FacesUtils.getBundleKey("messages", "info.currentInviationsListWasSent"));
                return;
            } else {
                FacesUtils.addSuccessMessage(FacesUtils.getBundleKey("messages", "error.noWaitingInviations"));
                return;
            }
        }
    }
    
   public static class InvitationResultWrapper {
        
        String firstName;
        
        String lastName;
        
        String link;
        
        boolean opted;
        
        public InvitationResultWrapper(String firstName, String lastName, String link, boolean opted) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.link = link;
            this.opted = opted;
        }
        
        public String getFirstName() {
            return firstName;
        }
        
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        
        public String getLastName() {
            return lastName;
        }
        
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        
        public String getLink() {
            return link;
        }
        
        public void setLink(String link) {
            this.link = link;
        }
        
        public boolean isOpted() {
            return opted;
        }
        
        public void setOpted(boolean opted) {
            this.opted = opted;
        }
    }
    
    private String generateConfirmationKey(String email2, String _currentUsername) {
        String confirmKey = encoder.encodePassword(email2, _currentUsername);
        return confirmKey;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
