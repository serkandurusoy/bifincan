package com.dna.bifincan.web.user;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.dna.bifincan.model.user.EmailCampaign;
import com.dna.bifincan.model.user.UserInvitation;
import com.dna.bifincan.service.MailService;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.spring.ScopeType;

@Named("optEmailAction")
@Scope(ScopeType.VIEW)
public class OptEmailAction implements Serializable {
	private static final long serialVersionUID = 4148629560245792219L;

	@SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(OptEmailAction.class);

    private static final String OPT_OUT = "bifincandan", OPT_OUT_ALL = "gelen-davetiyelerimden";

    private String email;
    private String campaign;    
    private String confirmationKey;

    private boolean sent = false;
    private String action;

    private UserInvitation invitation;
    private String successMessage, errorMessage; // to avoid styling the JSF standard messages
    
    @Inject
    private UserService userService;
    @Inject
    private MailService mailService;    
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
        if (log.isDebugEnabled()) {
            log.debug("action @"+this.getAction());
            log.debug("email @"+this.getEmail());
            log.debug("campaign @"+this.getCampaign());            
            log.debug("comfirmationKey @"+this.getConfirmationKey());
        }

        if (!(this.email != null && !"".equals(this.email))) {
            FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "opt_email_notpresent"));
            return;
        }

        if (!(this.campaign != null && !"".equals(this.campaign))) {
            FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "opt_campaign_notpresent"));
            return;
        } else {
        	EmailCampaign campaign = mailService.findEmailCampaign(Long.parseLong(this.campaign));
        	if(campaign == null) {
                FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "opt_campaign_notvalid"));
                return;        		
        	}
        	
            if (!this.confirmationKey.equals(encoder.encodePassword(this.email, campaign.getTimeScheduled()))) {
                FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "confirmationkey_notmatch"));
                return;
            }
        }

        if(OPT_OUT.equals(action)) {
            this.invitations = null;
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
                mailService.optOutEmailFromList(this.email); 
                produceMessage("email_opted_successfully", true);
            } else if (OPT_OUT_ALL.equals(action)) {
                List<UserInvitation> _invitations = userService.findNotOptedInvitationsByEmail(email);
                userService.optOutInvitations(_invitations);
                produceMessage("invitation_opted_successfully", true);
            }

            
        } catch (Exception ex) {
            ex.printStackTrace();
            produceMessage("error.generalErrorKey", false);
        }
    }

    private void produceMessage(String key, boolean success) {
    	if(success) {
    		this.successMessage = FacesUtils.getBundleKey("messages", key);
    		this.errorMessage = null;
    	} else {
    		this.successMessage = null;
    		this.errorMessage = FacesUtils.getBundleKey("messages", key);
    	}	
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public UserInvitation getInvitation() {
        return invitation;
    }

    public String getCampaign() {
		return campaign;
	}

	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}

	public void setInvitation(UserInvitation invitation) {
        this.invitation = invitation;
    }

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
        
}
