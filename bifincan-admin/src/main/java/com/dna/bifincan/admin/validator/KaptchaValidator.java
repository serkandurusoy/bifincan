package com.dna.bifincan.admin.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

@FacesValidator("kaptchaValidator")
public class KaptchaValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {

	HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);

	String kaptchaExpected = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);

	String kaptchaReceived = (String) value;

	if (kaptchaReceived == null || !kaptchaReceived.equalsIgnoreCase(kaptchaExpected)) {
	    FacesMessage message = new FacesMessage();

	    message.setDetail("Invalid Security Code.");
	    message.setSummary("Invalid security code.");
	    message.setSeverity(FacesMessage.SEVERITY_INFO);

	    throw new ValidatorException(message);
	}
    }
}
