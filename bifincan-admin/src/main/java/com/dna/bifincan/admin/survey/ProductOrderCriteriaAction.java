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

@Named("productOrderCriteriaAction")
@Scope(ScopeType.VIEW)
public class ProductOrderCriteriaAction implements Serializable {
	private static final long serialVersionUID = 4508397466530276107L;
	private static final Logger log = LoggerFactory.getLogger(ProductOrderCriteriaAction.class);

	private static final String BUNDLE_KEY_NAME = "messages", OPTION_TRUE_KEY = "option.true";
	
	private LazyDataModel<ProductOrderCriteria> dataModel;
    private ProductOrderCriteria currentProductOrderCriteria;
    private DataTable dataTable;
    		
	private List<OperatorType> operatorTypes;
	private List<OperatorValueType> operandValueTypes;
	private City selectedCity;
	private int selectedEnumOption;
	
    @Inject
    private ProductService productService;
    @Inject
    private AddressService addressService;
    
    @PostConstruct
    public void initialize() {
    	this.dataModel = new ProductOrderCriteriaDataModel(productService);
    }

    public void initAdd() {
        this.currentProductOrderCriteria = new ProductOrderCriteria();
        this.currentProductOrderCriteria.setPropertyType(PropertyType.ADDRESS); // the 1st option
        
    	setupListOfOperatorTypes();
    	setupListOfOperandValueTypes(); 
    }

    public void initEdit(ProductOrderCriteria productOrderCriteria) {
        this.currentProductOrderCriteria = productOrderCriteria;
       
    	setupListOfOperatorTypes();
    	setupListOfOperandValueTypes(); 
    	
    	if(this.currentProductOrderCriteria.getPropertyType() == PropertyType.ADDRESS) {
    		setSelectedCity(addressService.findCity(Long.valueOf(productOrderCriteria.getOperatorValue())));
    	}
    }
    
    public void save() {
    	try {
    		Long id = this.currentProductOrderCriteria.getId();
    		
        	if(this.currentProductOrderCriteria.getPropertyType() == PropertyType.ADDRESS) {
        		this.currentProductOrderCriteria.setOperatorValue(String.valueOf(this.selectedCity.getId()));
        	}

	        if(this.productService.saveProductOrderCriteria(this.currentProductOrderCriteria)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			WebConstants.ERROR_GEN_ERROR_KEY));  // change this in the future if needed... 
	        }
	        
	        this.currentProductOrderCriteria = null;
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
	        if(this.productService.deleteProductOrderCriteria(this.currentProductOrderCriteria)) { // success 
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
    	if(currentProductOrderCriteria != null) {
    		
	    	if(operatorTypes == null) {
	    		operatorTypes = new ArrayList<OperatorType>();
	    	} else {
	    		operatorTypes.clear();
	    	}
			
	    	PropertyType type = currentProductOrderCriteria.getPropertyType();
	    	
	    	if(type == null || (type != PropertyType.AGE && 
	    				(type != PropertyType.PAST_PRODUCT_POINT_VALUE && type != PropertyType.PAST_PRODUCT_QUANTITY) &&
	    				(type != PropertyType.ID_VERIFICATION && type != PropertyType.ACTIVITY_LEVEL) 
	    			)
	    	  ) {
	    		operatorTypes.add(OperatorType.EQUAL);
	    		operatorTypes.add(OperatorType.NOT_EQUAL);
	    	}
	    	
	    	if(type == PropertyType.AGE || type == PropertyType.PAST_PRODUCT_POINT_VALUE || 
	    			type == PropertyType.PAST_PRODUCT_QUANTITY || type == PropertyType.ACTIVITY_LEVEL) {
	    		operatorTypes.add(OperatorType.GREATER_OR_EQUAL);
	    		operatorTypes.add(OperatorType.LESS_OR_EQUAL);
	    	}
	    	
	    	if(type == PropertyType.ID_VERIFICATION) {
	    		operatorTypes.add(OperatorType.EQUAL);	    
	    	}	
	    	

	    	
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

    	if(currentProductOrderCriteria.getPropertyType() == PropertyType.ID_VERIFICATION) {
    		operandValueTypes.add(OperatorValueType.BOOLEAN);    
    	} else {
    		operandValueTypes.add(OperatorValueType.INTEGER);
    	}
    	
    	
    	/* TODO: probably enhance this in the feature by adding the 
		proper business logic first in CriteriaService */
    	//operandValueTypes.add(OperatorValueType.BOOLEAN);
    	//operandValueTypes.add(OperatorValueType.DECIMAL);
    	//operandValueTypes.add(OperatorValueType.STRING);	
    }
    
	public void updateExpessionParts(AjaxBehaviorEvent e) {
		setupListOfOperatorTypes();
		setupListOfOperandValueTypes(); 
	}
	
	
	public String displayLabelValue(ProductOrderCriteria criterion) {
		String result = "";
		
		try {
			if(criterion != null) {
				if(criterion.getOperatorValue() != null) {
					Integer pk = null;
					if(criterion.getOperatorValueType() == OperatorValueType.INTEGER) { 
						pk = Integer.parseInt(criterion.getOperatorValue());
					}
					switch(criterion.getPropertyType()) {
						case ADDRESS : 
							City city = addressService.findCity(Long.parseLong(criterion.getOperatorValue()));
							if(city != null) result = city.getName() + ", " + city.getLocalRegion().getName(); 
							break;
						case AGE : result = criterion.getOperatorValue(); break;
						case EDUCATION : result = FacesUtils.getBundleKey(BUNDLE_KEY_NAME, (EducationType.values()[pk]).getLabel()); break;
						case EMPLOYMENT_STATUS : result = FacesUtils.getBundleKey(BUNDLE_KEY_NAME, (EmploymentStatusType.values()[pk]).getLabel()); break;
						case GENDER : result = FacesUtils.getBundleKey(BUNDLE_KEY_NAME, (GenderType.values()[pk]).getLabel()); break;
						case HAS_CHILDREN : result =  FacesUtils.getBundleKey(BUNDLE_KEY_NAME, (HasChildrenType.values()[pk]).getLabel()); break;
						case MARITAL_STATUS : result = FacesUtils.getBundleKey(BUNDLE_KEY_NAME, (MaritalStatusType.values()[pk]).getLabel()); break;
						case MONTHLY_INCOME : result = FacesUtils.getBundleKey(BUNDLE_KEY_NAME, (MonthlyNetTLIncomeType.values()[pk]).getLabel()); break;		
						case PAST_PRODUCT_POINT_VALUE : result = criterion.getOperatorValue(); break;	
						case PAST_PRODUCT_QUANTITY : result = criterion.getOperatorValue(); break;	
						case ID_VERIFICATION : result = FacesUtils.getBundleKey(BUNDLE_KEY_NAME, OPTION_TRUE_KEY); break;	
						case ACTIVITY_LEVEL : result = criterion.getOperatorValue(); break;	
					}
				}
			}
		} catch(Exception ex) { }
		
		return result;
	}
	
    // --- Getters and Setters --- //
    public LazyDataModel<ProductOrderCriteria> getDataModel() {
    	return this.dataModel; 
    }
    
    public ProductOrderCriteria getCurrentProductOrderCriteria() {
        return currentProductOrderCriteria;
    }
    
	public void setCurrentProductOrderCriteria(ProductOrderCriteria currentProductOrderCriteria) {
        this.currentProductOrderCriteria = currentProductOrderCriteria;
    }
	
	public DataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
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
