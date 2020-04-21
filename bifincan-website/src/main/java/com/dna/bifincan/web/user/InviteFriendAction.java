package com.dna.bifincan.web.user;

import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.model.user.UserInvitation;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.MailService;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.WebConstants;
import com.dna.bifincan.util.spring.ScopeType;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.encoding.PasswordEncoder;

@Named("inviteFriendAction")
@Scope(ScopeType.VIEW)
public class InviteFriendAction implements Serializable {
	private static final long serialVersionUID = -385126853704131539L;

	private static final Logger log = LoggerFactory.getLogger(InviteFriendAction.class);

    @Email
    @NotNull
    @NotEmpty
    private String email;

    private boolean sent = false;

//    private String confirmationKey;
//
//    private Long invitedId;
//
//    private String action = "signup";
    private User inviter;

    @Inject
    private UserService userService;

    @Inject
    private MailService mailService;
    //@Inject
    //private CurrentUserHolder currentUser;

    @Inject
    PasswordEncoder encoder;

    @Inject
    private ConfigurationService configurationService;

    Integer numberOfInvitationsPerDay = null;

    int numberRemain = 0;

    @PostConstruct
    public void init() {
      //  if (log.isDebugEnabled()) {
      //      log.debug("call init ...");
     //   }
        inviter = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(WebConstants.CURRENT_USER_SESSION_KEY);

        // Configuration configuration = this.configurationService.find("numInvitationsPerDay");
        // numberOfInvitationsPerDay = Integer.parseInt(configuration.getValue());
        
        //numberOfInvitationsPerDay = 10;
        int activityLevel = inviter.getActivityLevel();
        Long city = userService.getUserPrimaryAddress(userService.findUserByUsername(inviter.getUsername())).getArea().getDistrict().getCounty().getCity().getId();
        int age = inviter.getAge();
        int gender = inviter.getGenderType().ordinal();
        int income = inviter.getMonthlyNetTLIncomeType().ordinal();
        int employment = inviter.getEmploymentStatusType().ordinal();
        int education = inviter.getEducationType().ordinal();
        int combinedProfile = income + employment + education;
        int extraInvitations = inviter.getExtraInvitations();
        boolean idVerified = inviter.isIdVerified();
        boolean marketingProfessional = inviter.isMarketingProfessional();
        boolean ambassador = inviter.isAmbassador();

        numberOfInvitationsPerDay = 1;
        
        numberOfInvitationsPerDay = numberOfInvitationsPerDay + extraInvitations;
        
        if (idVerified) {
            numberOfInvitationsPerDay = numberOfInvitationsPerDay + 1;
        }

        if (ambassador) {
            numberOfInvitationsPerDay = numberOfInvitationsPerDay + 1;
        }

        if (marketingProfessional) {
            numberOfInvitationsPerDay = numberOfInvitationsPerDay + 1;
        }

        if (age >= 30) {
            numberOfInvitationsPerDay = numberOfInvitationsPerDay + activityLevel - 1;
            if (city != 34 && city != 6 && city != 16 && city != 35 && city != 41) {numberOfInvitationsPerDay = numberOfInvitationsPerDay + 1;}
            //if (city != 34 && city != 6 && city != 35 && city != 41) {numberOfInvitationsPerDay = numberOfInvitationsPerDay + 1;}
            if (combinedProfile >= 10) {numberOfInvitationsPerDay = numberOfInvitationsPerDay + 1;}
            if (combinedProfile >= 10 && idVerified) {numberOfInvitationsPerDay = numberOfInvitationsPerDay + 1;}
            if (combinedProfile >= 10 && ambassador) {numberOfInvitationsPerDay = numberOfInvitationsPerDay + 1;}
        }

        if (age >= 35) {
            numberOfInvitationsPerDay = numberOfInvitationsPerDay + activityLevel - 1;
            if (idVerified) {numberOfInvitationsPerDay = numberOfInvitationsPerDay + 1;}
            if (ambassador) {numberOfInvitationsPerDay = numberOfInvitationsPerDay + 1;}
        }
        
        calculateNumberRemain();

       // if (log.isDebugEnabled()) {
       //     log.debug("call init ...");
       //     log.debug("numberOfInvitationsPerDay@" + numberOfInvitationsPerDay);
       //     log.debug("numberRemain@" + numberRemain);
       // }
    }

    private void calculateNumberRemain() {
        if (inviter != null) {
            this.historicalInvitations = userService.findInvitationsByInviter(inviter);
        } else {
            this.historicalInvitations = Collections.<UserInvitation>emptyList();
        }
        int count = this.userService.invitationsCountToday(inviter).intValue();
        //System.out.println(">> numberRemain = " + numberRemain + ", inviter.isFounder() = " + inviter.isFounder());
        numberRemain = !inviter.isFounder() ? (numberOfInvitationsPerDay - count) : -1;
    }

    public Integer getNumberOfInvitationsPerDay() {
        return numberOfInvitationsPerDay;
    }

    public int getNumberRemain() {
        return numberRemain;
    }

    public String getBaseUrlWithPath() {
        String applicationCanonicalURL = "https://www.bifincan.com";
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        String ctxPath = context.getRequestContextPath();
        Configuration canonicalUrlConfig = configurationService.find((ConfigurationType.APPLICATION_CANONICAL_URL.getKey()));
        if (canonicalUrlConfig != null) {
            applicationCanonicalURL = canonicalUrlConfig.getValue();
        }
        String baseUrlWithPath = applicationCanonicalURL + ctxPath;
        return baseUrlWithPath;
    }
    List<UserInvitation> historicalInvitations;

//    UserInvitation acceptedInvitation;
//
//    public UserInvitation getAcceptedInvitation() {
//        return acceptedInvitation;
//    }
    public List<UserInvitation> getHistoricalInvitations() {
        return historicalInvitations;
    }

//    public void searchByEmail(AjaxBehaviorEvent _event) {
//        log.debug("search by email.");
//        String _email = (String) ((UIInput) _event.getComponent()).getValue();
//        log.debug("email@" + _email);
//
//        if (_email == null || _email.trim().length() == 0) {
//            historicalInvitations = Collections.<UserInvitation>emptyList();
//        }else{
//            historicalInvitations= userService.findInvitationsByEmail(_email);
//        }
//        
//        User _user = userService.findUserByEmail(_email);
//        if (_user != null) {
//            this.acceptedInvitation=_user.getAcceptedInvitation();
//        }
//    }
    @SuppressWarnings("unchecked")
    public void sendInvitation() {
      //  if (log.isDebugEnabled()) {
      //      log.debug("send invitation to @" + email);
      //  }
        //
        // if (email == null || email.trim().length() == 0) {
        // FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages",
        // "invited_email_required"));
        // }

        User _user = userService.findUserByEmail(email);
        if (_user != null) {
            FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "user_existed"));
            return;
        }

        if(email.contains("facebook") || email.contains("facebok")) {
            FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "no_facebook_invitation"));
            return;
        }

        if(email.contains("gmial.") ||
           email.contains("yaho.") ||
           email.contains("hotmial.") ||
           email.endsWith(".co") ||
           email.endsWith(".com.t") ||
           email.endsWith(".com.r") ||
           email.endsWith(".rt") ||
           email.endsWith(".comtr") ||
           email.endsWith(".comrt") ||
           email.endsWith(".cotr") ||
           email.endsWith(".co.tr")
           ) {
            FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "wrong_email_domain"));
            return;
        }

        if (userService.findInvitationByInviterAndEmail(inviter, email) != null) {
            FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "user_invitation_sent_before"));
            return;
        }


        if (numberRemain == 0) {
            FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "user_invitation_no remain"));
            return;
        }

        UserInvitation _invitation = new UserInvitation();
        _invitation.setEmail(this.email);

        _invitation.setInviter(inviter);

        int pointsBalance = userService.saveInvitation(_invitation);  // save the invitation

        // update the user's balance in session
        HttpServletRequest request = (HttpServletRequest) FacesUtils.getExternalContext().getRequest();
        inviter.setPointsBalance(pointsBalance);
        request.getSession(true).setAttribute(WebConstants.CURRENT_USER_SESSION_KEY, inviter);

        String encodedVal = generateConfirmationKey(this.email, inviter.getUsername());


        String link = getBaseUrlWithPath() + "/yeni-uye/" + this.email + "/" + _invitation.getId() + "/" + encodedVal;

        String link2 = getBaseUrlWithPath() + "/davetiyenin-hatirlatilmasini-istemiyorum/sadece-bu-davetiye-icin/" + this.email + "/" + _invitation.getId() + "/" + encodedVal;

        String link3 = getBaseUrlWithPath() + "/davetiyenin-hatirlatilmasini-istemiyorum/su-ana-kadar-aldigim-tum-davetiyeler-icin/" + this.email + "/" + _invitation.getId() + "/" + encodedVal;

        @SuppressWarnings("rawtypes")
        Map model = new HashMap();
        model.put("baseurl", getBaseUrlWithPath());
        model.put("userfirstname", inviter.getFirstName());
        model.put("userlastname", inviter.getLastName());
        model.put("link", link);
        model.put("link2", link2);
        model.put("link3", link3);
        try {
            mailService.sendEmail(this.email, inviter.getFirstName() + " " + inviter.getLastName() + " seni bifincan'a davet ediyor", "invitation", model);
        } catch (Exception e) {
            FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "user_invitation_sent_failed"));
            return;
        }

        this.email = null;
        calculateNumberRemain();
        FacesUtils.addSuccessMessage(FacesUtils.getBundleKey("messages", "user_invitation_sent_successfully"));
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

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
