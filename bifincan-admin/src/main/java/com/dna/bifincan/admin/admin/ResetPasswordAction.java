/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.admin.admin;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.user.AdminUser;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.dna.bifincan.model.user.User;
import com.dna.bifincan.service.AdminUserService;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.MailService;
import com.dna.bifincan.common.util.RandomStringGenerator;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;


@Named(value = "resetPasswordAction")
@Scope("request")
public class ResetPasswordAction {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ResetPasswordAction.class);

	@NotNull
	@Email
	private String email;

	@Inject
	AdminUserService userService;

	private boolean reset;

	@Inject
	private MailService mailService;

	@Inject
	RandomStringGenerator passwordGenerator;

	@Inject
	PasswordEncoder passwordEncoder;

	@Inject
	private ConfigurationService configurationService;

        public String getBaseUrlWithPath() {
            String applicationCanonicalURL = "https://www.bifincan.com";
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            String ctxPath = context.getRequestContextPath();
            Configuration canonicalUrlConfig = configurationService.find((ConfigurationType.
                            APPLICATION_CANONICAL_URL.getKey()));
            if(canonicalUrlConfig != null) applicationCanonicalURL = canonicalUrlConfig.getValue();
            String baseUrlWithPath = applicationCanonicalURL + ctxPath;
            return baseUrlWithPath;
        }
        
	@PostConstruct
	public void initialize() { }

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void resetPassword() {
		AdminUser user = userService.findUserByEmail(email);
		if (user == null) {
			FacesUtils.addErrorMessage(" User email does not exist.");
			return;
		}
		String newPassword = passwordGenerator.randomString(6);
		user.setPassword(passwordEncoder.encodePassword(newPassword, user.getSalt()));
		userService.saveUser(user);
		this.reset = true;
		try {
			sendNewPassword(user, newPassword);
		} catch (Exception ex) {
			FacesUtils.addErrorMessage(" Email sending failed, please contact administrator");
			return;
		}
		FacesUtils.addSuccessMessage("New password was sent to your email, please check it later.");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void sendNewPassword(final AdminUser user, final String password) throws MailException, MessagingException {
		Map model = new HashMap();
		model.put("username", user.getUsername());
		model.put("password", password);
		mailService.sendEmail(this.email, "Yeni bifincan şifreni oluşturdum", "reset-password", model);
	}
}
