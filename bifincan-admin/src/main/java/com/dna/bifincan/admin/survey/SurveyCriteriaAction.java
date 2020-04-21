package com.dna.bifincan.admin.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
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
import com.dna.bifincan.model.address.City;
import com.dna.bifincan.model.products.ProductOrderCriteria;
import com.dna.bifincan.model.survey.Survey;
import com.dna.bifincan.model.survey.SurveyCriteria;
import com.dna.bifincan.model.survey.SurveyOption;
import com.dna.bifincan.model.type.OperatorType;
import com.dna.bifincan.model.type.OperatorValueType;
import com.dna.bifincan.model.type.PropertyType;
import com.dna.bifincan.model.user.EducationType;
import com.dna.bifincan.model.user.EmploymentStatusType;
import com.dna.bifincan.model.user.GenderType;
import com.dna.bifincan.model.user.HasChildrenType;
import com.dna.bifincan.model.user.MaritalStatusType;
import com.dna.bifincan.model.user.MonthlyNetTLIncomeType;
import com.dna.bifincan.service.AddressService;
import com.dna.bifincan.service.ProductService;
import com.dna.bifincan.service.SurveyService;

@Named("surveyCriteriaAction")
@Scope(ScopeType.VIEW)
public class SurveyCriteriaAction implements Serializable {
	private static final long serialVersionUID = -3799369665084286745L;
	private static final Logger log = LoggerFactory.getLogger(SurveyCriteriaAction.class);

	private LazyDataModel<SurveyCriteria> dataModel;
    private SurveyCriteria currentSurveyCriteria;
    private DataTable dataTable;
    private List<Survey> surveys;   
    	
	private List<OperatorType> operatorTypes;
	private List<OperatorValueType> operandValueTypes;
	private City selectedCity;
	private int selectedEnumOption;
	
    @Inject
    private SurveyService surveyService;    
    @Inject
    private ProductService productService;
    @Inject
    private AddressService addressService;
    
    @PostConstruct
    public void initialize() {
    	this.dataModel = new SurveyCriteriaDataModel(surveyService);
    	
    	if(this.surveys == null) initializeSurveys();
    }

    public void initAdd() {
        this.currentSurveyCriteria = new SurveyCriteria();
        this.currentSurveyCriteria.setPropertyType(PropertyType.ADDRESS); // the 1st option
        
    	setupListOfOperatorTypes();
    	setupListOfOperandValueTypes(); // currently not needed
    }

    public void initEdit(SurveyCriteria surveyCriteria) {
        this.currentSurveyCriteria = surveyCriteria;
        
    	setupListOfOperatorTypes();
    	setupListOfOperandValueTypes(); // currently not needed
    	
    	if(this.currentSurveyCriteria.getPropertyType() == PropertyType.ADDRESS) {
    		setSelectedCity(addressService.findCity(Long.valueOf(currentSurveyCriteria.getOperatorValue())));
    	}        
    }
    
    public void save() {
    	try {
    		Long id = this.currentSurveyCriteria.getId();
    	    if(this.currentSurveyCriteria.getOperatorValue() == null) {
    	    	this.currentSurveyCriteria.setOperatorType(null);
    	    	this.currentSurveyCriteria.setOperatorValueType(null);
    	    	this.currentSurveyCriteria.setPropertyType(null);
    	    }
    		
        	if(this.currentSurveyCriteria.getPropertyType() == PropertyType.ADDRESS) {
        		this.currentSurveyCriteria.setOperatorValue(String.valueOf(this.selectedCity.getId()));
        	} 
        	
	        if(this.surveyService.saveSurveyCriteria(this.currentSurveyCriteria)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			WebConstants.ERROR_GEN_ERROR_KEY));  // change this in the future if needed... 
	        }
	        
	        this.currentSurveyCriteria = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}  
    	
    	initialize();
    	this.dataTable.setFirst(0);
    }
   
    public void delete() {
    	try {
	        if(this.surveyService.deleteSurveyCriteria(this.currentSurveyCriteria)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.ERROR_GEN_ERROR_KEY));  // change this in the future if needed... 
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    

		initialize();
    	this.dataTable.setFirst(0);
    } 
    

    private void setupListOfOperatorTypes() {
    	if(currentSurveyCriteria != null) {
	    	if(operatorTypes == null) operatorTypes = new ArrayList<OperatorType>();
	    	else operatorTypes.clear();
			
	    	if(currentSurveyCriteria.getPropertyType() == null || currentSurveyCriteria.getPropertyType() != PropertyType.AGE) {
	    		operatorTypes.add(OperatorType.EQUAL);
	    		operatorTypes.add(OperatorType.NOT_EQUAL);
	    	}	
	    	if(currentSurveyCriteria == null || currentSurveyCriteria.getPropertyType() == PropertyType.AGE) 
	    		operatorTypes.add(OperatorType.GREATER_OR_EQUAL);
	    	if(currentSurveyCriteria == null || currentSurveyCriteria.getPropertyType() == PropertyType.AGE) 
	    		operatorTypes.add(OperatorType.LESS_OR_EQUAL);
	    	
	    	/* TODO: probably enhance this in the feature by adding the 
	    			proper business logic first in CriteriaService */
			//operatorTypes.add(OperatorType.GREATER);
			//operatorTypes.add(OperatorType.LESS);
			//operatorTypes.add(OperatorType.LIKE);
    	}
    }
    
    private void setupListOfOperandValueTypes() {
    	if(operandValueTypes == null) operandValueTypes = new ArrayList<OperatorValueType>();
    	else operandValueTypes.clear();

    	operandValueTypes.add(OperatorValueType.INTEGER);
    	
    	/* TODO: probably enhance this in the feature by adding the 
		proper business logic first in CriteriaService */
    	//operandValueTypes.add(OperatorValueType.BOOLEAN);
    	//operandValueTypes.add(OperatorValueType.DECIMAL);
    	//operandValueTypes.add(OperatorValueType.STRING);	
    }
    
	public void updateExpessionParts(AjaxBehaviorEvent e) {
		setupListOfOperatorTypes();
		//setupListOfOperandValueTypes(); // currently not needed
	}
	
	public String displayLabelValue(SurveyCriteria criterion) {
		String result = "";
		
		try {
			if(criterion != null) {
				if(criterion.getOperatorValue() != null) {
					Integer pk = Integer.parseInt(criterion.getOperatorValue());
					switch(criterion.getPropertyType()) {
						case ADDRESS : 
							City city = addressService.findCity(Long.parseLong(criterion.getOperatorValue()));
							if(city != null) result = city.getName() + ", " + city.getLocalRegion().getName(); 
							break;
						case AGE : result = criterion.getOperatorValue(); break;
						case EDUCATION : result = (EducationType.values()[pk]).getLabel(); break;
						case EMPLOYMENT_STATUS : result = (EmploymentStatusType.values()[pk]).getLabel(); break;
						case GENDER : result = (GenderType.values()[pk]).getLabel(); break;
						case HAS_CHILDREN : result = (HasChildrenType.values()[pk]).getLabel(); break;
						case MARITAL_STATUS : result = (MaritalStatusType.values()[pk]).getLabel(); break;
						case MONTHLY_INCOME : result = (MonthlyNetTLIncomeType.values()[pk]).getLabel(); break;				
					}
				}
			}
		} catch(Exception ex) { }
		
		return result;
	}
	
    
    private void initializeSurveys() {
    	this.surveys = surveyService.findAll();
    }
    
    // --- Getters and Setters --- //
    public LazyDataModel<SurveyCriteria> getDataModel() {
    	return this.dataModel; 
    }
   
	public SurveyCriteria getCurrentSurveyCriteria() {
		return currentSurveyCriteria;
	}

	public void setCurrentSurveyCriteria(SurveyCriteria currentSurveyCriteria) {
		this.currentSurveyCriteria = currentSurveyCriteria;
	}

	public DataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}

	public List<Survey> getSurveys() {
		return surveys;
	}

	public void setSurveys(List<Survey> surveys) {
		this.surveys = surveys;
	}

	public List<OperatorType> getOperatorTypes() {
		return operatorTypes;
	}

	public void setOperatorTypes(List<OperatorType> operatorTypes) {
		this.operatorTypes = operatorTypes;
	}

	public List<OperatorValueType> getOperandValueTypes() {
		return operandValueTypes;
	}

	public void setOperandValueTypes(List<OperatorValueType> operandValueTypes) {
		this.operandValueTypes = operandValueTypes;
	}

	public City getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(City selectedCity) {
		this.selectedCity = selectedCity;
	}

	public int getSelectedEnumOption() {
		return selectedEnumOption;
	}

	public void setSelectedEnumOption(int selectedEnumOption) {
		this.selectedEnumOption = selectedEnumOption;
	}
		
}
