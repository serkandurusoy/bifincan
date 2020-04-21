package com.dna.bifincan.admin.util;

import java.io.IOException;
import java.util.Locale;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import org.springframework.http.HttpStatus;

public abstract class FacesUtils {

    public static String getBundleKey(String bundleName, String key) {
        return FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), bundleName).getString(key);
    }

    public static void addSuccessMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_INFO, msg);
    }

    public static void addErrorMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_ERROR, msg);
    }

    private static void addMessage(FacesMessage.Severity severity, String msg) {
        final FacesMessage facesMsg = new FacesMessage(severity, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String componentId, String msg) {
        addMessage(FacesMessage.SEVERITY_INFO, msg, componentId);
    }

    public static void addErrorMessage(String componentId, String msg) {
        addMessage(FacesMessage.SEVERITY_ERROR, msg, componentId);
    }

    private static void addMessage(FacesMessage.Severity severity, String msg, String componentId) {
        final FacesMessage facesMsg = new FacesMessage(severity, msg, msg);
        FacesContext.getCurrentInstance().addMessage(componentId, facesMsg);
    }

    public static String getParam(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static <T> T getRequestParam(String expr, Class<T> classz) {
        try {
            Application app = getFacesContext().getApplication();
            ValueExpression ve = app.getExpressionFactory().createValueExpression(getFacesContext().getELContext(), expr, classz);
            return (T) ve.getValue(getFacesContext().getELContext());
        } catch (RuntimeException e) {
            throw e;
        }
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
        } catch (IOException ex) {
            throw new RuntimeException(msg);
        }
    }

    public static Locale getCurrentLocale() {
        return getFacesContext().getViewRoot().getLocale();
    }
}
