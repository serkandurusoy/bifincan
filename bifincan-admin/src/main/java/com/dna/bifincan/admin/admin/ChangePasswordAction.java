package com.dna.bifincan.admin.admin;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.model.user.AdminUser;
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

import com.dna.bifincan.service.AdminUserService;

@Named(value = "changePasswordAction")
@Scope("request")
public class ChangePasswordAction {
    private static final Logger log = LoggerFactory.getLogger(ChangePasswordAction.class);

    @Length(min = 6, max = 20)
    @NotNull
    @NotEmpty
    private String newPassword;


    private boolean changed = false;

    @Inject
    AdminUserService userService;

    @Inject
    PasswordEncoder passwordEncoder;

    public void changePassword() {
	Object userObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	if (log.isDebugEnabled()) {
	    log.debug("username is @" + userObject);
	}

	UserDetails userDetails = (UserDetails) userObject;
	AdminUser _user = userService.findUserByUsername(userDetails.getUsername());

	_user.setPassword(passwordEncoder.encodePassword(this.newPassword, _user.getSalt()));
	userService.saveUser(_user);

	FacesUtils.addSuccessMessage("Password  updated successfully.");
	changed = true;
    }

    public boolean isChanged() {
	return changed;
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
