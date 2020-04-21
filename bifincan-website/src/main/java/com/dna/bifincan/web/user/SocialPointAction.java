package com.dna.bifincan.web.user;

import java.io.Serializable;
import java.security.Principal;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.model.user.SocialPoint;
import com.dna.bifincan.model.user.SocialPointType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.WebConstants;
import com.dna.bifincan.util.spring.ScopeType;

@Named("socialPointAction")
@Scope(ScopeType.VIEW)
public class SocialPointAction implements Serializable {
	private static final long serialVersionUID = -5740502428943593225L;
	private static final Logger log = LoggerFactory.getLogger(SocialPointAction.class);
	
	@Inject
	private UserService userService;

	public SocialPointAction() { }

	public void savePoints(ActionEvent e) {
		String action = FacesUtils.getParam("action");
		String prettyurl = FacesUtils.getParam("prettyurl");
		
		if(action != null && !action.isEmpty()) {
			try {
				// get the action type
				SocialPointType actionType = SocialPointType.valueOf(action);
				if(actionType != null) {
					
					// get the user
					Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
					String username = principal.getName();
					User user = userService.findUserByUsername(username);
					
					// get the path from the url
					//String url = FacesUtils.getExternalContext().getRequestServletPath(); // this returns the actual servlet path (not the pretified one)

					SocialPoint socialPoint = new SocialPoint();
					
					socialPoint.setAction(actionType);
					socialPoint.setUrl(prettyurl);
					socialPoint.setUser(user);

					Integer totalPoints = userService.saveSocialPoint(socialPoint);
					if(totalPoints != null && totalPoints > 0) updateUserPointsInSession(totalPoints);  // update the balance in session
					
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private void updateUserPointsInSession(int totalPoints) {
		Map<String, Object> values = FacesUtils.getExternalContext().getSessionMap();
		User user = (User)values.get(WebConstants.CURRENT_USER_SESSION_KEY);
		
		if(user != null) {
			user.setPointsBalance(totalPoints);
		}	
	}	

}

