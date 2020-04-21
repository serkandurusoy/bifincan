package com.dna.bifincan.security;


import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Phase listener for clear cache for all pages.
 */
public class NoCachePhaseListener implements PhaseListener {
    private static final long serialVersionUID = -8041955994475939713L;
    private static final Logger LOG = LoggerFactory.getLogger(NoCachePhaseListener.class);
    
    private static final String PRAGMA_KEY = "Pragma";   
    private static final String CACHE_CONTROL_KEY = "Cache-Control";
    private static final String NO_CACHE_VALUE = "no-cache"; 
    private static final String NO_STORE_VALUE = "no-store"; 
    private static final String MUST_REVALIDATE_VALUE = "must-revalidate"; 
    
    @Override
    public void afterPhase(PhaseEvent event) {
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        FacesContext fc = event.getFacesContext();
        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
        response.addHeader(PRAGMA_KEY, NO_CACHE_VALUE);
        response.addHeader(CACHE_CONTROL_KEY, NO_CACHE_VALUE);
        response.addHeader(CACHE_CONTROL_KEY, NO_STORE_VALUE);
        response.addHeader(CACHE_CONTROL_KEY, MUST_REVALIDATE_VALUE);

    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}