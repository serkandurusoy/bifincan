package com.dna.bifincan.util;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import org.springframework.http.HttpStatus;

public abstract class FacesUtils {

    public static String getBundleKey(String bundleName, String key) {
    	return FacesContext.getCurrentInstance().getApplication()
    		.getResourceBundle(FacesContext.getCurrentInstance(), bundleName).getString(key);
    }

    public static String getBundleKey(String bundleName, String key, Object[] args) { 
    	String message = getFacesContext().getApplication().getResourceBundle(getFacesContext(), 
    			bundleName).getString(key);
        // MessageFormat messageFormat = new MessageFormat(message, 
        //		getFacesContext().getViewRoot().getLocale());  
    	// messageFormat.format(args);  // NOTE: this method doesn't seem to work as expected  
    	if(args != null) {
	        for(int i=0; i < args.length ; i++) {
	        	message = message.replaceAll("\\{[\\s]*" + i + "[\\s]*\\}", args[i].toString());
	        }
    	}
        return message;
    }
    
    public static void addSuccessMessage(String msg) {
    	addMessage(FacesMessage.SEVERITY_INFO, msg);
    }

    public static void addWarningMessage(String msg) {
    	addMessage(FacesMessage.SEVERITY_WARN, msg);
    }
    
    public static void addErrorMessage(String msg) {
    	addMessage(FacesMessage.SEVERITY_ERROR, msg);
    }

    public static void addErrorMessage(String id, String msg) {
    	addMessage(id, FacesMessage.SEVERITY_ERROR, msg);
    }
    
    private static void addMessage(FacesMessage.Severity severity, String msg) {
        final FacesMessage facesMsg = new FacesMessage(severity, msg, msg);
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }
   
    private static void addMessage(String id, FacesMessage.Severity severity, String msg) {
        final FacesMessage facesMsg = new FacesMessage(severity, msg, msg);
		FacesContext.getCurrentInstance().addMessage(id, facesMsg);
    }
    
    public static String getParam(String key) {
    	return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }
    
    public static FacesContext getFacesContext() {
    	return FacesContext.getCurrentInstance();
    }
    
    public static ExternalContext getExternalContext() {
    	return getFacesContext().getExternalContext();
    }
    
    public static Flash getFlash() {
    	return getExternalContext().getFlash();
    }

    public static void sendHttpStatusCode(HttpStatus code, String msg) {
		try {
			FacesUtils.getExternalContext().responseSendError(code.value(), msg);
		} catch(IOException ex) {
			throw new RuntimeException(msg);
		}
    }
   
    public static Locale getCurrentLocale() {
    	return getFacesContext().getViewRoot().getLocale();
    }    
}
