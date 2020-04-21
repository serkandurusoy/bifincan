package com.dna.bifincan.admin.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.type.ProductClassifier;

@Named("classifierConverter")
public class ClassifierConverter implements Converter, Serializable {
	private static final long serialVersionUID = -4988297145196559041L;
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(ClassifierConverter.class);

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg) {
    	if(arg == null) return null;
    	
    	Object ret = null;
    	
    	int cl = Integer.parseInt(arg);
    	switch(cl) {
    		case 0: ret = ProductClassifier.NORMAL;
    			break;
    		case 1: ret = ProductClassifier.MEDICAL;
				break;
    		case 2: ret = ProductClassifier.TOBACCO;
				break;
    		case 3: ret = ProductClassifier.ALCOHOL;
				break;				
    	}

        return ret;
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg) {
    	if(arg == null) return "";
    	
    	if (!(arg instanceof ProductClassifier)) {
    	    throw new IllegalArgumentException("This converter only handles instances of ProductClassifier type ..." + 
    	    		arg.getClass().getName());
    	}
    	
        return String.valueOf(((ProductClassifier)arg).ordinal());
    }


}