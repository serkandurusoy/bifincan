package com.dna.bifincan.web.user;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import com.dna.bifincan.model.user.User;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.WebConstants;

@Named(value = "pointBalanceAction")
@Scope("view")
public class PointBalanceAction implements Serializable {
	private static final long serialVersionUID = 8351703630120218597L;
	@Inject
	private UserService userService;
	
	public PointBalanceAction() { }
	
	public void updateBalance() {
		if(FacesUtils.getExternalContext() != null && FacesUtils.getExternalContext().getUserPrincipal() != null) {
			if(FacesUtils.getExternalContext().getSessionMap().get(WebConstants.CURRENT_USER_SESSION_KEY) != null ) {
				User user = userService.findUserByUsername(FacesUtils.getExternalContext().getUserPrincipal().getName());
				if(user != null) {
					((User)FacesUtils.getExternalContext().getSessionMap().
							get(WebConstants.CURRENT_USER_SESSION_KEY)).setPointsBalance(user.getPointsBalance() != null ? user.getPointsBalance() : 0);
				}
			}
		}
	}
}
