/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.web.user;

import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.type.ConfigurationType;

import java.io.Serializable;
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
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.MailService;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.common.util.RandomStringGenerator;
import com.dna.bifincan.model.gsm.GsmPrefix;
import java.util.Date;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "resetPasswordAction")
@Scope("view")
public class ResetPasswordAction implements Serializable {
	private static final long serialVersionUID = -6920605459294242936L;

	@SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(ResetPasswordAction.class);

    @NotNull
    @Email
    private String email;

    @NotNull
    private Date birthDate;

    @NotNull
    private GsmPrefix gsmPrefix;

    @NotNull
    private String gsmNumber;

    @Inject
    UserService userService;

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
        Configuration canonicalUrlConfig = configurationService.find((ConfigurationType.APPLICATION_CANONICAL_URL.getKey()));
        if (canonicalUrlConfig != null) {
            applicationCanonicalURL = canonicalUrlConfig.getValue();
        }
        String baseUrlWithPath = applicationCanonicalURL + ctxPath;
        return baseUrlWithPath;
    }

    @PostConstruct
    public void initialize() {
    }

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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGsmNumber() {
        return gsmNumber;
    }

    public void setGsmNumber(String gsmNumber) {
        this.gsmNumber = gsmNumber;
    }


    public GsmPrefix getGsmPrefix() {
        return gsmPrefix;
    }

    public void setGsmPrefix(GsmPrefix gsmPrefix) {
        this.gsmPrefix = gsmPrefix;
    }

    public void resetPassword() {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            FacesUtils.addErrorMessage(" böyle bir e-posta adresi bulamadım");
            this.reset = false;
            return;
        }
        if (!user.isActive()) {
            FacesUtils.addErrorMessage(" üyeliğin artık aktif değil :( sebebini de sana daha önce detaylı bir e-posta mesajı ile anlatmıştım");
            this.reset = false;
            return;
        }
        if (!birthDate.equals(user.getBirthday())) {
            FacesUtils.addErrorMessage(" doğum tarihi hatalı");
            this.reset = false;
            return;
        }

        if (gsmPrefix.getCode().intValue() != user.getGsmVerification().getGsmPrefix().getCode().intValue()) {
            FacesUtils.addErrorMessage(" gsm alan kodu hatalı");
            this.reset = false;
            return;
        }

        if (!gsmNumber.equals(user.getGsmVerification().getGsmNumber())) {
            FacesUtils.addErrorMessage(" gsm numarası hatalı");
            this.reset = false;
            return;
        }

        String newPassword = passwordGenerator.randomString(6);
        user.setPassword(passwordEncoder.encodePassword(newPassword, user.getSalt()));
        userService.saveUser(user);
        this.reset = true;
        try {
            sendNewPassword(user, newPassword);
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(" bir sorun var gibi, daha sonra tekrar dene istersen, ben de mühendis arkadaşlara durumu iletiyorum hemen");
            return;
        }
        FacesUtils.addSuccessMessage("yeni şifreni oluşturup e-posta adresine mesaj olarak ilettim, yeni şifrenle giriş yapabilirsin. şifreni rahat hatırlayabileceğin bir başka şifre ile değiştirmeni tavsiye ederim ;)");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void sendNewPassword(final User user, final String password) throws MailException, MessagingException {
        Map model = new HashMap();
        model.put("baseurl", getBaseUrlWithPath());
        model.put("userfirstname", user.getFirstName());
        model.put("username", user.getUsername());
        model.put("password", password);
        mailService.sendEmail(this.email, "Yeni bifincan şifreni oluşturdum", "reset-password", model);
    }
}
