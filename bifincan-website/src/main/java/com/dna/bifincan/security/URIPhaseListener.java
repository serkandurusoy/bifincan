package com.dna.bifincan.security;


import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URIPhaseListener implements PhaseListener {
	private static final long serialVersionUID = -8686174564073067845L;
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(URIPhaseListener.class);

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    @Override
    public void beforePhase(PhaseEvent event) { 	
    	try {
    		// Note: the cast to a HttpServletRequest class assumes that the application always runs in a servlet based environment
	    	if(event.getFacesContext().getExternalContext().getResourceAsStream(((HttpServletRequest)event.getFacesContext().
	    			getExternalContext().getRequest()).getRequestURI().substring(event.getFacesContext().
	    			getExternalContext().getRequestContextPath().length())) == null) {
	    		event.getFacesContext().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, null);
	    		event.getFacesContext().responseComplete();
	    	} 
    	} catch(Exception ex) { }
    }

    @Override
    public void afterPhase(PhaseEvent event) {  }
}
