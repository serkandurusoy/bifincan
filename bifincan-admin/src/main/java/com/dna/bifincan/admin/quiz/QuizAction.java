package com.dna.bifincan.admin.quiz;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.admin.product.ProductDataModel;
import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;
import com.dna.bifincan.admin.util.spring.ScopeType;
import com.dna.bifincan.model.dto.SurveyItemNodeDTO;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.quiz.QuizOption;
import com.dna.bifincan.model.quiz.QuizQuestion;
import com.dna.bifincan.service.ProductService;
import com.dna.bifincan.service.QuizService;
import com.dna.bifincan.common.util.Constants;

@Named("quizAction")
@Scope(ScopeType.VIEW)
public class QuizAction implements Serializable {
	private static final long serialVersionUID = -8510768311275414713L;
	private static final Logger log = LoggerFactory.getLogger(QuizAction.class);

	private LazyDataModel<Product> dataModel;
    private Product currentProduct;
    private QuizQuestion currentQuizQuestion;
    private QuizOption currentQuizOption;
    private Long currentQuizQuestionId, currentQuizOptionId;

    private DataTable dataTable;

    @Inject
    private ProductService productService;
    @Inject    
    private QuizService quizService;
    
    @PostConstruct
    public void initialize() {
    	this.dataModel = new ProductDataModel(productService);
    	currentQuizQuestion = new QuizQuestion();
    }

    public void initAddQuestion(Product product) {
    	this.currentProduct = product;    	
    	this.currentQuizQuestion = new QuizQuestion();
    }

    public void initEditQuestion(Product product) {
    	this.currentProduct = product;
        this.currentQuizQuestion = product.getQuizQuestion();
    }
    
    public void initAddOption() {
    	this.currentQuizOption = new QuizOption();
    }    
    
    public void initEditOption(QuizOption quizOption) {
    	this.currentQuizOption = quizOption;
    }  
    
    public void save() {
    	try {
    		Long id = this.currentQuizQuestion.getId();
   		
	        if(this.quizService.saveQuizQuestion(this.currentQuizQuestion, this.currentProduct)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"quizQuestion.error.duplicate"));  
	        }
	        
	        this.currentQuizQuestion = null;
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
	        if(this.quizService.deleteQuizQuestion(this.currentQuizQuestion)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"quizQuestion.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    

		initialize();
    	this.dataTable.setFirst(0);
    } 
    
    
    public void saveOption() {
    	try {
    		Long id = this.currentQuizOption.getId();
   		
	        if(this.quizService.saveQuizOption(this.currentQuizOption, this.currentQuizQuestion)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"quizOption.error.correctFlag"));  // not in use currently
	        }
	        
	        this.currentQuizOption = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}  
    }     
    
    
    public void deleteOption() {
    	try {
	        if(this.quizService.deleteQuizOption(this.currentQuizOption, this.currentQuizQuestion)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"quizOption.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    
    }
    
    // --- Getters and Setters --- //
    public LazyDataModel<Product> getDataModel() {
    	return this.dataModel; 
    }
    
    public Product getCurrentProduct() {
        return currentProduct;
    }
    
	public void setCurrentProduct(Product currentProduct) {
        this.currentProduct = currentProduct;
    }
	
	public DataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}

	public QuizQuestion getCurrentQuizQuestion() {
		return currentQuizQuestion;
	}

	public void setCurrentQuizQuestion(QuizQuestion currentQuizQuestion) {
		this.currentQuizQuestion = currentQuizQuestion;
	}

	public QuizOption getCurrentQuizOption() {
		return currentQuizOption;
	}

	public void setCurrentQuizOption(QuizOption currentQuizOption) {
		this.currentQuizOption = currentQuizOption;
	}

	public Long getCurrentQuizQuestionId() {
		return currentQuizQuestionId;
	}

	public void setCurrentQuizQuestionId(Long currentQuizQuestionId) {
		this.currentQuizQuestionId = currentQuizQuestionId;
	}

	public Long getCurrentQuizOptionId() {
		return currentQuizOptionId;
	}

	public void setCurrentQuizOptionId(Long currentQuizOptionId) {
		this.currentQuizOptionId = currentQuizOptionId;
	}

	
}
