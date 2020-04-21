package com.dna.bifincan.web.user;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.dna.bifincan.model.user.User;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;

@Named(value = "changePasswordAction")
@Scope("request")
public class ChangePasswordAction implements Serializable {
	private static final long serialVersionUID = 5533395347691916932L;

	private static final Logger log = LoggerFactory.getLogger(ChangePasswordAction.class);

    @Length(min = 6, max = 20)
    @NotNull
    @NotEmpty
    private String currentPassword;

    @Length(min = 6, max = 20)
    @NotNull
    @NotEmpty
    private String newPassword;

    @Length(min = 6, max = 20)
    @NotNull
    @NotEmpty
    private String confirmPassword;

    private boolean changed = false;

    @Inject
    UserService userService;

    @Inject
    PasswordEncoder passwordEncoder;

    public void changePassword() {
	Object userObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	if (log.isDebugEnabled()) {
	    log.debug("username is @" + userObject);
	}

	UserDetails userDetails = (UserDetails) userObject;
	User _user = userService.findUserByUsername(userDetails.getUsername());

	if (this.currentPassword != null
		&& !(passwordEncoder.encodePassword(this.currentPassword, _user.getSalt())).equals(_user.getPassword())) {
	    FacesUtils.addErrorMessage("şifren ile doğrulaması uyuşmuyor");
	    return;
	}

	if (this.newPassword != null && this.confirmPassword != null && !this.confirmPassword.equals(this.newPassword)) {
	    FacesUtils.addErrorMessage("şifren ile doğrulaması uyuşmuyor");
	    return;
	}

	_user.setPassword(passwordEncoder.encodePassword(this.newPassword, _user.getSalt()));
	userService.saveUser(_user);

	FacesUtils.addSuccessMessage("şifreni güncelledim");
	changed = true;
    }

    public boolean isChanged() {
	return changed;
    }

    public void setConfirmPassword(final String password) {
	confirmPassword = password;
    }

    public String getConfirmPassword() {
	return confirmPassword;
    }

    public String getCurrentPassword() {
	return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
	this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
	return newPassword;
    }

    public void setNewPassword(String newPassword) {
	this.newPassword = newPassword;
    }

    public void setChanged(boolean changed) {
	this.changed = changed;
    }

}
