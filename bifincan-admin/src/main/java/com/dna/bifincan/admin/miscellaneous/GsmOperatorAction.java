package com.dna.bifincan.admin.miscellaneous;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;
import com.dna.bifincan.admin.util.spring.ScopeType;
import com.dna.bifincan.model.address.GlobalRegion;
import com.dna.bifincan.model.gsm.GsmOperator;
import com.dna.bifincan.service.GSMService;

@Named("gsmOperatorAction")
@Scope(ScopeType.VIEW)
public class GsmOperatorAction implements Serializable {
	private static final long serialVersionUID = 4743843398719471751L;

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(GsmOperatorAction.class);

    private List<GsmOperator> operators;
    private GsmOperator currentOperator;
    
    @Inject
    GSMService gsmService;

    public GsmOperatorAction() { }

    @PostConstruct
    public void initialize() {
    	loadOperators();
    }
    
    // --- Action methods and listeners --- //
    public void loadOperators() {
    	try {
    		this.operators = gsmService.getGsmOperators();
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    }

    public void initAdd() {
        this.currentOperator = new GsmOperator();
    }

    public void initEdit(GsmOperator operator) {
        this.currentOperator = operator;
    }

    public void save() {
    	try {
    		Long id = this.currentOperator.getId();
	        if(this.gsmService.saveGsmOperator(this.currentOperator)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"gsmOperator.error.duplicateName"));
	        }
	        
	        this.currentOperator = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    	
    	loadOperators();    
    }
    
    public void delete() {
    	try {
	        if(this.gsmService.deleteGsmOperator(this.currentOperator)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"gsmOperator.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    
		
		loadOperators();
    }
    
    // --- Getters and Setters --- //
    public List<GsmOperator> getOperators() {
        return this.operators;
    }
    
    public void setOperators(List<GsmOperator> operators) {
		this.operators = operators;
	}

	public GsmOperator getCurrentOperator() {
		return currentOperator;
	}

	public void setCurrentOperator(GsmOperator currentOperator) {
		this.currentOperator = currentOperator;
	}
}
