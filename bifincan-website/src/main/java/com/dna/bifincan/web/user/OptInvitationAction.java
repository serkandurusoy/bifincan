package com.dna.bifincan.web.user;

import com.dna.bifincan.model.user.UserInvitation;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.spring.ScopeType;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.encoding.PasswordEncoder;

@Named("optInvitationAction")
@Scope(ScopeType.VIEW)
public class OptInvitationAction implements Serializable {
	private static final long serialVersionUID = -2677255983299678281L;

	@SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(OptInvitationAction.class);

    private static final String OPT_OUT = "sadece-bu-davetiye-icin", OPT_OUT_ALL = "su-ana-kadar-aldigim-tum-davetiyeler-icin";

    private String email;

    private String confirmationKey;

    private String invitationId;

    private boolean sent = false;

    private String action;

    private UserInvitation invitation;

    @Inject
    private UserService userService;

    @Inject
    private PasswordEncoder encoder;

    private List<UserInvitation> invitations;

    public List<UserInvitation> getInvitations() {
        return invitations;
    }

    /**
     * This method is called automatically before rendering the 'opt out'
     * confirmation page. It validates the input parameters and initializes some
     * variables.
     */
    public void init() {
//		Map<String, String> params =  FacesUtils.getExternalContext().getRequestParameterMap();
//		
//		if(this.getAction() == null) this.setAction(params.get("type"));
//		
//		if(this.getEmail() == null) this.setEmail(params.get("email"));
//		if(this.getConfirmationKey() == null) this.setConfirmationKey(params.get("confirmationkey"));
//		if(this.getInvitationId() == null) this.setInvitationId(params.get("invitedid"));
        if (log.isDebugEnabled()) {
            log.debug("action @"+this.getAction());
            log.debug("email @"+this.getEmail());
            log.debug("comfirmationKey @"+this.getConfirmationKey());
            log.debug("invitation id @"+ this.getInvitationId());
        }

        this.invitation = userService.findInvitation(Long.parseLong(this.invitationId));
        if (invitation == null) {
            FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "invitation_notfound"));
            return;
        }

        if (!invitation.getEmail().equals(this.email)) {
            FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "invitation_email_notmatch"));
            return;
        }

        if (!this.confirmationKey.equals(generateConfirmationKey(this.email, invitation.getInviter().getUsername()))) {
            FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "confirmationkey_notmatch"));
            return;
        }

        if(OPT_OUT.equals(action)) {
            this.invitations = Arrays.asList(this.invitation);
        } else if (OPT_OUT_ALL.equals(action)) {
            this.invitations = userService.findInvitationsByEmail(email);
        }
    }

    /**
     * This method performs the 'opt out' operation for one or more invitations
     * (all associated with the current email).
     */
    public void opt() {
        try {
            if (OPT_OUT.equals(action)) {
                userService.optOutInvitations(Arrays.asList(this.invitation));
            } else if (OPT_OUT_ALL.equals(action)) {
                List<UserInvitation> _invitations = userService.findNotOptedInvitationsByEmail(email);
                userService.optOutInvitations(_invitations);
            }

            FacesUtils.addSuccessMessage(FacesUtils.getBundleKey("messages", "invitation_opted_successfully"));
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "error.generalErrorKey"));
        }
    }

    /**
     * This helper method calculates the correct hashed confirmation key.
     *
     * @param email2 the email
     * @param _currentUsername the usuername
     * @param createTime the creation time
     * @return the hashed key
     */
    private String generateConfirmationKey(String email2, String _currentUsername) {
        String confirmKey = encoder.encodePassword(email2, _currentUsername);
        return confirmKey;
    }

    // setters & getters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public String getConfirmationKey() {
        return confirmationKey;
    }

    public void setConfirmationKey(String confirmationKey) {
        this.confirmationKey = confirmationKey;
    }

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public UserInvitation getInvitation() {
        return invitation;
    }

    public void setInvitation(UserInvitation invitation) {
        this.invitation = invitation;
    }
}
