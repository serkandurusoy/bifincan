package com.dna.bifincan.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

@FacesValidator("notNullValidator")
public class NotNullValidator implements Validator {
	private static String ERR_MSG = "Null value is found.";
	
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
		if(value == null) {
		    FacesMessage message = new FacesMessage();
	
		    message.setDetail(ERR_MSG);
		    message.setSummary(ERR_MSG);
		    message.setSeverity(FacesMessage.SEVERITY_INFO);
	
		    throw new ValidatorException(message);
		}
    }
}
