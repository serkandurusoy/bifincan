/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.admin;

import com.dna.bifincan.admin.util.WebConstants;
import com.dna.bifincan.admin.util.spring.ScopeType;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;


@Named("homeAction")
@Scope(ScopeType.VIEW)
public class HomeAction implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(HomeAction.class);

    String redirectTo = "/index.xhtml";

    public String detectSession() {
        Object currentUser = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(WebConstants.CURRENT_USER_SESSION_KEY);
        log.debug("@@@@@@@@currentUser@" + (currentUser != null));
        if (currentUser != null) {
            this.redirectTo = "/user/home.xhtml";
        }
        return this.redirectTo;
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }
}
