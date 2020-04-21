package com.dna.bifincan.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesValidator("kaptchaValidator")
public class KaptchaValidator implements Validator {

    private static final Logger log = LoggerFactory.getLogger(KaptchaValidator.class);

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {

        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);

        String kaptchaExpected = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);

        String kaptchaReceived = (String) value;

        if (log.isDebugEnabled()) {
            log.debug("kaptchaReceived@" + kaptchaReceived);
            log.debug("kaptchaExpected@" + kaptchaExpected);
        }

        if (kaptchaReceived == null || !kaptchaReceived.equalsIgnoreCase(kaptchaExpected)) {
            ((UIInput) uiComponent).setValue("");
            FacesMessage message = new FacesMessage();

            message.setDetail(" kod hatalı");
            message.setSummary(" kod hatalı");
            message.setSeverity(FacesMessage.SEVERITY_INFO);

            throw new ValidatorException(message);
        }
    }
}
