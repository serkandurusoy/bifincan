package com.dna.bifincan.admin.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.dna.bifincan.admin.util.spring.ScopeType;
import com.dna.bifincan.model.address.City;
import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.order.Order;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.survey.SurveyOption;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.type.OperatorType;
import com.dna.bifincan.model.type.OperatorValueType;
import com.dna.bifincan.model.type.ProductClassifier;
import com.dna.bifincan.model.type.PropertyType;
import com.dna.bifincan.model.type.SurveyQuestionCriteriaType;
import com.dna.bifincan.model.user.EducationType;
import com.dna.bifincan.model.user.EmploymentStatusType;
import com.dna.bifincan.model.user.GenderType;
import com.dna.bifincan.model.user.HasChildrenType;
import com.dna.bifincan.model.user.MaritalStatusType;
import com.dna.bifincan.model.user.MonthlyNetTLIncomeType;
import com.dna.bifincan.service.AddressService;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.ProductService;
import com.dna.bifincan.service.SurveyService;

@Named("utilsBean")
@Scope(ScopeType.SINGLETON)
public class UtilsBean implements Serializable {
    private static final long serialVersionUID = 3344612973907407620L;

    @Inject
    private ConfigurationService configurationService;
    @Inject
    private ProductService productService;	
    @Inject
    private SurveyService surveyService;   
    @Inject
    private AddressService addressService;
	@Inject
	PasswordEncoder passwordEncoder;
	
    private List<OperatorType> operatorTypes;
    private List<OperatorValueType> operandValueTypes;
    private List<PropertyType> criteriaPropertyTypes;	
    private List<ProductClassifier> classifiers; 
    private List<Integer> ageOptions; 
    private List<EducationType> educationOptions; 
    private List<EmploymentStatusType> employmentStatusOptions;     
    private List<GenderType> genderOptions;     
    private List<HasChildrenType> hasChildrenOptions;  
    private List<MaritalStatusType> maritalStatusOptions;      
    private List<MonthlyNetTLIncomeType> monthlyIncomeOptions;      
    private List<SurveyQuestionCriteriaType> questionCriteriaTypes;     
    
    public UtilsBean() { }
	
    public String getThisYear() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String yearAsString = Integer.toString(year);
        if (year > 2012) return "2012 - " + yearAsString;
        return yearAsString;
    }

    public String getApplicationLocale() {
        String applicationLocale = "tr";
        Configuration localeConfig = configurationService.find((ConfigurationType.
                        APPLICATION_LOCALE.getKey()));
        if(localeConfig != null) applicationLocale = localeConfig.getValue();
        return applicationLocale;
    }

    public String getApplicationCanonicalURL() {
        String applicationCanonicalURL = "https://www.bifincan.com";
        Configuration canonicalUrlConfig = configurationService.find((ConfigurationType.
                        APPLICATION_CANONICAL_URL.getKey()));
        if(canonicalUrlConfig != null) applicationCanonicalURL = canonicalUrlConfig.getValue();
        return applicationCanonicalURL;
    }
        
    @PostConstruct
    public void initialize() {
    	setupListOfClassifiers();
    	
    	setupListOfOperatorTypes();
    	setupListOfOperandValueTypes();
    	setupListOfCriteriaPropertyTypes();
    	
    	setupListOfAgeOptions();
    	setupListOfEducationOptions();
    	setupListOfEmploymentStatusOptions();  
    	setupListOfGenderOptions();
    	setupListOfHasChildrenOptions();   
    	setupListOfMaritalStatusOptions();     	
    	setupListOfMontlyIncomeOptions();       
    	
    	setupQuestionCriteriaTypes();
    }
    
    private void setupListOfMontlyIncomeOptions() {
    	monthlyIncomeOptions = new ArrayList<MonthlyNetTLIncomeType>();
    	
    	for(MonthlyNetTLIncomeType incomeOption : MonthlyNetTLIncomeType.values()) {
    		monthlyIncomeOptions.add(incomeOption);
    	}
    }
    
    private void setupListOfMaritalStatusOptions() {
    	maritalStatusOptions = new ArrayList<MaritalStatusType>();
    	
    	for(MaritalStatusType maritalStatus : MaritalStatusType.values()) {
    		maritalStatusOptions.add(maritalStatus);
    	}
    }
    
    private void setupListOfHasChildrenOptions() {
    	hasChildrenOptions = new ArrayList<HasChildrenType>();
    	
    	for(HasChildrenType option : HasChildrenType.values()) {
    		hasChildrenOptions.add(option);
    	}
    }
    
    private void setupListOfEducationOptions() {
    	educationOptions = new ArrayList<EducationType>();
    	
    	for(EducationType education : EducationType.values()) {
    		educationOptions.add(education);
    	}
    }
    
    private void setupListOfGenderOptions() {
    	genderOptions = new ArrayList<GenderType>();
    	
    	for(GenderType gender : GenderType.values()) {
    		genderOptions.add(gender);
    	}
    }
   
    private void setupQuestionCriteriaTypes() {
    	questionCriteriaTypes = new ArrayList<SurveyQuestionCriteriaType>();
    	
    	for(SurveyQuestionCriteriaType type : SurveyQuestionCriteriaType.values()) {
    		questionCriteriaTypes.add(type);
    	}
    }
    
    private void setupListOfEmploymentStatusOptions() {
    	employmentStatusOptions = new ArrayList<EmploymentStatusType>();
    	
    	for(EmploymentStatusType status : EmploymentStatusType.values()) {
    		employmentStatusOptions.add(status);
    	}
    }
    
    private void setupListOfAgeOptions() {
    	ageOptions = new ArrayList<Integer>();
    	
    	for(int i = 1;i <= 100;i++) {
    		ageOptions.add(i);
    	}
    }
    
    private void setupListOfOperatorTypes() {
    	operatorTypes = new ArrayList<OperatorType>();
		
    	operatorTypes.add(OperatorType.EQUAL);
    	operatorTypes.add(OperatorType.GREATER);
		operatorTypes.add(OperatorType.GREATER_OR_EQUAL);
		operatorTypes.add(OperatorType.LESS);
		operatorTypes.add(OperatorType.LESS_OR_EQUAL);
		operatorTypes.add(OperatorType.LIKE);		
    }
    
    private void setupListOfOperandValueTypes() {
    	operandValueTypes = new ArrayList<OperatorValueType>();
		
    	operandValueTypes.add(OperatorValueType.BOOLEAN);
    	operandValueTypes.add(OperatorValueType.DECIMAL);
    	operandValueTypes.add(OperatorValueType.INTEGER);
    	operandValueTypes.add(OperatorValueType.STRING);	
    }
    
    private void setupListOfCriteriaPropertyTypes() {
    	criteriaPropertyTypes = new ArrayList<PropertyType>();
		
    	criteriaPropertyTypes.add(PropertyType.ADDRESS);
    	criteriaPropertyTypes.add(PropertyType.AGE);
    	criteriaPropertyTypes.add(PropertyType.EDUCATION);
    	criteriaPropertyTypes.add(PropertyType.EMPLOYMENT_STATUS);
    	criteriaPropertyTypes.add(PropertyType.GENDER);
    	criteriaPropertyTypes.add(PropertyType.HAS_CHILDREN);
    	criteriaPropertyTypes.add(PropertyType.MARITAL_STATUS);
    	criteriaPropertyTypes.add(PropertyType.MONTHLY_INCOME); 

    	criteriaPropertyTypes.add(PropertyType.PAST_PRODUCT_QUANTITY);
    	criteriaPropertyTypes.add(PropertyType.PAST_PRODUCT_POINT_VALUE);
    	
    	criteriaPropertyTypes.add(PropertyType.ID_VERIFICATION);
    	criteriaPropertyTypes.add(PropertyType.ACTIVITY_LEVEL);
    }
    
    private void setupListOfClassifiers() {
		classifiers = new ArrayList<ProductClassifier>();
		
		classifiers.add(ProductClassifier.NORMAL);
		classifiers.add(ProductClassifier.MEDICAL);
		classifiers.add(ProductClassifier.TOBACCO);
		classifiers.add(ProductClassifier.ALCOHOL);
    }

    public List<Product> completeProduct(String query) {  
        List<Product> results = new ArrayList<Product>();  
        
        if(query.length() > 0) {
        	results = productService.findByName(query);  
        }  
        
        return results;  
    } 
    
    public List<SurveyOption> completeSurveyOption(String query) {  
        List<SurveyOption> results = new ArrayList<SurveyOption>();  
        
        if(query.length() > 0) {
        	results = surveyService.findProfileOptionsByTitle(query);  
        }  
        
        return results;  
    } 
    
    
    public List<City> completeCity(String query) {  
        List<City> results = new ArrayList<City>();  
        
        if(query.length() > 0) {
        	results = addressService.findCitiesByName(query);  
        }  
        
        return results;  
    } 
    
    public String extractCode(Order order) {
    	if(order != null && order.getPlacedTime() != null) {
			String orderConfirmationCode =  passwordEncoder.encodePassword(
					(String.valueOf(order.getPlacedTime().getTime() + order.getId())), null);
			return orderConfirmationCode.substring(0, 8);
    	}
		return null;
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


	public List<PropertyType> getCriteriaPropertyTypes() {
		return criteriaPropertyTypes;
	}

	public void setCriteriaPropertyTypes(List<PropertyType> criteriaPropertyTypes) {
		this.criteriaPropertyTypes = criteriaPropertyTypes;
	}

	public List<ProductClassifier> getClassifiers() {
		return classifiers;
	}

	public void setClassifiers(List<ProductClassifier> classifiers) {
		this.classifiers = classifiers;
	}

	public List<Integer> getAgeOptions() {
		return ageOptions;
	}

	public void setAgeOptions(List<Integer> ageOptions) {
		this.ageOptions = ageOptions;
	}

	public List<EducationType> getEducationOptions() {
		return educationOptions;
	}

	public void setEducationOptions(List<EducationType> educationOptions) {
		this.educationOptions = educationOptions;
	}

	public List<EmploymentStatusType> getEmploymentStatusOptions() {
		return employmentStatusOptions;
	}

	public void setEmploymentStatusOptions(
			List<EmploymentStatusType> employmentStatusOptions) {
		this.employmentStatusOptions = employmentStatusOptions;
	}

	public List<GenderType> getGenderOptions() {
		return genderOptions;
	}

	public void setGenderOptions(List<GenderType> genderOptions) {
		this.genderOptions = genderOptions;
	}

	public List<HasChildrenType> getHasChildrenOptions() {
		return hasChildrenOptions;
	}

	public void setHasChildrenOptions(List<HasChildrenType> hasChildrenOptions) {
		this.hasChildrenOptions = hasChildrenOptions;
	}

	public List<MaritalStatusType> getMaritalStatusOptions() {
		return maritalStatusOptions;
	}

	public void setMaritalStatusOptions(List<MaritalStatusType> maritalStatusOptions) {
		this.maritalStatusOptions = maritalStatusOptions;
	}

	public List<MonthlyNetTLIncomeType> getMonthlyIncomeOptions() {
		return monthlyIncomeOptions;
	}

	public void setMonthlyIncomeOptions(
			List<MonthlyNetTLIncomeType> monthlyIncomeOptions) {
		this.monthlyIncomeOptions = monthlyIncomeOptions;
	}

	public List<SurveyQuestionCriteriaType> getQuestionCriteriaTypes() {
		return questionCriteriaTypes;
	}

	public void setQuestionCriteriaTypes(
			List<SurveyQuestionCriteriaType> questionCriteriaTypes) {
		this.questionCriteriaTypes = questionCriteriaTypes;
	}   

  
}
