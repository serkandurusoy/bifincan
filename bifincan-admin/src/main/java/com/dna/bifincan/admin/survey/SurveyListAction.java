package com.dna.bifincan.admin.survey;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;
import com.dna.bifincan.admin.util.spring.ScopeType;
import com.dna.bifincan.model.survey.Survey;
import com.dna.bifincan.model.survey.SurveyBrand;
import com.dna.bifincan.model.survey.SurveyBrandProductCategory;
import com.dna.bifincan.model.survey.SurveyProduct;
import com.dna.bifincan.model.survey.SurveyProductCategory;
import com.dna.bifincan.model.survey.SurveyProfile;
import com.dna.bifincan.model.type.SurveyType;
import com.dna.bifincan.service.SurveyService;

@Named("surveyListAction")
@Scope(ScopeType.VIEW)
public class SurveyListAction implements Serializable {
	private static final long serialVersionUID = 6332932042387309708L;
	private static final String SURVEY_TYPE_PARAM_EXPR = "#{param.type}";
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SurveyListAction.class);

	private LazyDataModel<Survey> dataModel; 
	private SurveyType type;  // default type
    private Survey currentSurvey;
    private DataTable dataTable;	
    
    @Inject
    private SurveyService surveyService;

    public SurveyListAction() { }

	@PostConstruct
    public void initialize() {
    	// get the type from menu (only when it is initialized)
    	if(type == null) { 
	    	Integer iType = (Integer)FacesUtils.getRequestParam(SURVEY_TYPE_PARAM_EXPR, Integer.class);
	    	if(iType != null && iType > 0) {
	    		this.type = SurveyType.values()[iType - 1];
	    	}
    	}
    	
    	this.dataModel = new SurveyDataModel(surveyService, this.type);
    }
    
    // --- Action methods and listeners --- //

    public void initAdd() {
        this.currentSurvey = null;

        switch(this.type) {
        	case PROFILE: this.currentSurvey = new SurveyProfile();
        		break;
        	case BRAND: this.currentSurvey = new SurveyBrand();
        		break;
        	case PROD_CAT: this.currentSurvey = new SurveyProductCategory();
    			break;        		
        	case BRAND_PROD_CAT: this.currentSurvey = new SurveyBrandProductCategory();
    			break;  
        	case PROD: this.currentSurvey = new SurveyProduct();
    			break;  
        }
    }

    public void initEdit(Survey survey) {
        this.currentSurvey = survey;
    }
    
    public String save() {
    	try {
	        if(this.surveyService.saveSurvey(this.currentSurvey)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,  
	        			WebConstants.SUCCESS_UPDATE_KEY));    
	        } 
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    	
    	return "surveyInfo?faces-redirect=true&amp;surveyId=" +this.currentSurvey.getId();
    }
    
    public void delete() {
    	try {
	        if(this.surveyService.deleteSurvey(this.currentSurvey)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"survey.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    
		
    	initialize();
    	this.dataTable.setFirst(0);
    }

    // --- Getters and Setters --- //
    
    public LazyDataModel<Survey> getDataModel() {
    	return this.dataModel; 
    }    
    
	public Survey getCurrentSurvey() {
		return currentSurvey;
	}

	public void setCurrentSurvey(Survey currentSurvey) {
		this.currentSurvey = currentSurvey;
	}

	public DataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}

	public SurveyType getType() {
		return type;
	}

	public void setType(SurveyType type) {
		this.type = type;
	} 

}
