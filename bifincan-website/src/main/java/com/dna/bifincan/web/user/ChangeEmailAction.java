package com.dna.bifincan.web.user;

import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.model.user.verification.EmailVerification;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.MailService;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.WebConstants;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Named(value = "changeEmailAction")
@Scope("request")
public class ChangeEmailAction implements Serializable {
	private static final long serialVersionUID = -8933773365585770651L;

	private static final Logger log = LoggerFactory.getLogger(ChangeEmailAction.class);

    @NotNull
    @NotEmpty
    @Email
    private String email;

    private boolean changed;

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
        Configuration canonicalUrlConfig = configurationService.find((ConfigurationType.
                        APPLICATION_CANONICAL_URL.getKey()));
        if(canonicalUrlConfig != null) applicationCanonicalURL = canonicalUrlConfig.getValue();
        String baseUrlWithPath = applicationCanonicalURL + ctxPath;
        return baseUrlWithPath;
    }

    private String confirmationKey;

    private String username;

    public void changeEmail() {
	Object userObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	if (log.isDebugEnabled()) {
	    log.debug("username is @" + userObject);
	}

	UserDetails userDetails = (UserDetails) userObject;
	User _user = userService.findUserByUsername(userDetails.getUsername());

	if (this.email.equals(_user.getEmail())) {
	    FacesUtils.addErrorMessage("e-posta adresi değişmedi");
	    return;
	}

	Date sentTime = new Date();

	EmailVerification verification = _user.getEmailVerification();
	if (verification == null) {
	    verification = new EmailVerification();
	    verification.setEmailVerificationSentTime(sentTime);
	    verification.setEmailVerified(false);
	    _user.setEmailVerification(verification);
	} else {
	    _user.getEmailVerification().setEmailVerificationSentTime(sentTime);
	    _user.getEmailVerification().setEmailVerified(false);
	}
	_user.setActive(false);
	String oldEmail = _user.getEmail();
	log.debug(">> oldEmail = " + oldEmail);
	_user.setEmail(email);

	userService.changeEmail(_user, oldEmail);
	
	if (log.isDebugEnabled()) {
	    log.debug(">>>>>sent time @" + _user.getEmailVerification().getEmailVerificationSentTime());
	    log.debug(">>>>>sent time, long value@"
		    + _user.getEmailVerification().getEmailVerificationSentTime().getTime());
	}

	String encodedVal = generateConfirmationKey(_user);
	if (log.isDebugEnabled()) {
	    log.debug(">>>>>>>>>>>>>>>confirmationKey is @" + encodedVal);
	}

	ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

        String link = getBaseUrlWithPath() + "/eposta-dogrula/" + _user.getUsername() + "/" + encodedVal;
	try {
	    sendVerificationEmail(_user, link);
	} catch (MailException e) {
	    e.printStackTrace();
	    FacesUtils.addErrorMessage("Email sent failed.");
	    return;
	} catch (MessagingException e) {
	    e.printStackTrace();
	    FacesUtils.addErrorMessage("Email sent failed.");
	    return;
	}

	FacesUtils.addSuccessMessage("değişikliği doğrulamak için sana ilettiğim e-posta mesajındaki bağlantıya tıklamalısın");

	// force user to log out.
	context.invalidateSession();
	changed = true;
    }

    private String generateConfirmationKey(User _user) {
	String encodedVal = encoder.encodePassword(_user.getEmail(), saltSource(_user));
	return encodedVal;
    }

    public void verify() {
		if (log.isDebugEnabled()) {
		    log.debug(">>>>>call verify, comfirmation key @" + this.confirmationKey);
		}
		if (this.username == null) {
		    FacesUtils.addErrorMessage("kullanıcı adını bulunamadım. kullandığın bağlantı hatalı olabilir, e-posta mesajından tıkladığında bazen bağlantılar birden fazla satıra bölündüğünde böyle olabiliyor, bağlantının tamamını kopyalayıp tarayıcının adres satırına yapıştırarak denersen sorun kalmayacaktır");
		    return;
		}
		User _user = userService.findUserByUsername(this.username);
		if (_user == null) {
		    FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "user_notfound"));
		    return;
		}
	
		if (this.confirmationKey == null) {
		    FacesUtils.addErrorMessage("onay kodunu bulamadım. kullandığın bağlantı hatalı olabilir, e-posta mesajından tıkladığında bazen bağlantılar birden fazla satıra bölündüğünde böyle olabiliyor, bağlantının tamamını kopyalayıp tarayıcının adres satırına yapıştırarak denersen sorun kalmayacaktır");
		    return;
		}
	
		if (log.isDebugEnabled()) {
		    log.debug(">>>>>sent time @" + _user.getEmailVerification().getEmailVerificationSentTime());
		    log.debug(">>>>>sent time, long value@"
			    + _user.getEmailVerification().getEmailVerificationSentTime().getTime());
		}
		if (!this.confirmationKey.equals(generateConfirmationKey(_user))) {
		    FacesUtils.addErrorMessage("onay kodun geçersiz. kullandığın bağlantı hatalı olabilir, e-posta mesajından tıkladığında bazen bağlantılar birden fazla satıra bölündüğünde böyle olabiliyor, bağlantının tamamını kopyalayıp tarayıcının adres satırına yapıştırarak denersen sorun kalmayacaktır");
		    return;
		}
	
		_user.getEmailVerification().setEmailVerified(true);
		_user.setActive(true);
	
		userService.saveUser(_user);
	
		User userToken = (User)FacesUtils.getExternalContext().getSessionMap().get(WebConstants.CURRENT_USER_SESSION_KEY);
		if(userToken != null && userToken.getEmailVerification() != null) {
			userToken.getEmailVerification().setEmailVerified(true);
		}	
	
		FacesUtils.addSuccessMessage(" e-posta adres değişikliğini gerçekleştirildim, artık tüm işlemlerini yeni adresinle yapabilirsin");
    }

    private String saltSource(User _user) {
	return _user.getUsername();
    }

    @SuppressWarnings("unchecked")
	private void sendVerificationEmail(User _user, String link) throws MailException, MessagingException {
	@SuppressWarnings("rawtypes")
	Map model = new HashMap();
	model.put("baseurl", getBaseUrlWithPath());
	model.put("userfirstname", _user.getFirstName());
	model.put("link", link);
	mailService.sendEmail(this.email, "E-posta adresini doğrulaman gerekiyor", "change-email", model);
    }

    public boolean isChanged() {
	return changed;
    }

    public void setChanged(boolean changed) {
	this.changed = changed;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getConfirmationKey() {
	return confirmationKey;
    }

    public void setConfirmationKey(String confirmationKey) {
	this.confirmationKey = confirmationKey;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }
}
