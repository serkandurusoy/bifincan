package com.dna.bifincan.web;

import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.type.ConfigurationType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

import com.dna.bifincan.model.user.User;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.MailService;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.StringUtils;
import com.dna.bifincan.util.WebConstants;
import com.dna.bifincan.util.spring.ScopeType;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Value;

@Named(value = "contactAction")
@Scope(ScopeType.VIEW)
public class ContactAction implements Serializable {
	private static final long serialVersionUID = 9043851816883932531L;

	private static final Logger log = LoggerFactory.getLogger(ContactAction.class);

    @NotNull
    @NotEmpty
    @Email
    private String email;

    private String name;

    @NotNull
    @NotEmpty
    @Size(min = 20)
    private String message;

    private String phone;

    @Inject
    UserService userService;

    @Inject
    MailService mailService;

    @Inject
    ConfigurationService configurationService;

    @Value("${mail.from}")
    private String from;

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
    User _logined;

    public void init() {
        if (log.isDebugEnabled()) {
            log.debug(">>>>>init @");
        }
        _logined = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(WebConstants.CURRENT_USER_SESSION_KEY);

        if (!FacesContext.getCurrentInstance().isPostback()) {
            initForm();
        }
    }

    private void initForm() {
        if (_logined != null) {
            this.setEmail(_logined.getEmail());
            this.setName(_logined.getFullName());
            this.setPhone(_logined.getPhone());
        } else {
            this.setEmail(null);
            this.setName(null);
            this.setPhone(null);
        }
        this.setMessage(null);
    }

    public void send() {
        if (log.isDebugEnabled()) {
            log.debug(">>>>>send @");
        }

        Map model = new HashMap();
        model.put("name", this.getName());
        model.put("email", this.getEmail());
        model.put("phone", this.getPhone());
        model.put("message", StringUtils.getCleanHTML(StringUtils.escapeHTML(this.getMessage())));
        model.put("baseurl", this.getBaseUrlWithPath());
        try {
            //mail to the commenter
            mailService.sendEmail(this.getEmail(), "Mesajını aldım. Tamam, bende ;)", "contact-form-confirmation", model);

            //mail to bifincan admin
            mailService.sendEmail(this.from, this.getEmail(), "Bifincan'a mesajım var", "contact-form", model);
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }

        //reset form information     
        //initForm();
        FacesUtils.addSuccessMessage(FacesUtils.getBundleKey("messages", "info.contact_sent"));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
