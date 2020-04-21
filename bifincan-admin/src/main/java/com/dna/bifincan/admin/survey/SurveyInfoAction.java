package com.dna.bifincan.admin.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;
import com.dna.bifincan.admin.util.spring.ScopeType;
import com.dna.bifincan.model.dto.ProductDTO;
import com.dna.bifincan.model.dto.SurveyDTO;
import com.dna.bifincan.model.dto.SurveyItemNodeDTO;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.products.ProductCategoryLevelThree;
import com.dna.bifincan.model.survey.Survey;
import com.dna.bifincan.model.survey.SurveyBrandProductCategory;
import com.dna.bifincan.model.survey.SurveyOption;
import com.dna.bifincan.model.survey.SurveyProduct;
import com.dna.bifincan.model.survey.SurveyProductCategory;
import com.dna.bifincan.model.survey.SurveyQuestion;
import com.dna.bifincan.model.survey.SurveyQuestionCriteria;
import com.dna.bifincan.model.type.SurveyType;
import com.dna.bifincan.service.ProductService;
import com.dna.bifincan.service.SurveyService;
import com.dna.bifincan.common.util.Constants;

@Named("surveyInfoAction")
@Scope(ScopeType.VIEW)
public class SurveyInfoAction implements Serializable {
	private static final long serialVersionUID = 6332932042387309708L;

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SurveyInfoAction.class);
	private static String SURVEY_PARAM_EXPR = "#{param.surveyId}";
	
    private SurveyDTO currentSurvey;  
    
    private TreeNode root; 
    private TreeNode currentNode; 
    private SurveyItemNodeDTO currentItem; 
    
    private List<SurveyQuestionCriteria> surveyQuestionCriteria;
    private SurveyQuestionCriteria currentCriterion;
    private List<SurveyQuestion> listOfQuestionsInLowerPosition;
    
    private List<ProductDTO> products;
    private Product currentProduct;
    private ProductDTO currentProductNode;
    
    private List<ProductCategoryLevelThree> categories;
    private ProductCategoryLevelThree currentCategory;

    private SurveyQuestion currentQuestionInLowerPosition;
    
    @Inject
    private SurveyService surveyService;
    @Inject
    private ProductService productService;
    
    public SurveyInfoAction() { }

	@PostConstruct
    public void initialize() {			
		initEdit();
	}
    
    // --- Action methods and listeners --- //
	
	 // ----- CODE RELATED TO THE CURRENT SURVEY ----- //
	/**
	 * Fetches the survey from the repository. 
	 * NOTE: It uses a REQUEST param that indicates the survey id 
	 * @return
	 */
    public String initEdit() {
    	Long surveyId = FacesUtils.getRequestParam(SURVEY_PARAM_EXPR, Long.class);
    	Survey survey = (Survey)surveyService.findSurveyById(surveyId);
    	
        this.currentSurvey = new SurveyDTO(survey.getId(), survey.getTitle(), 
        			survey.isActive(), survey.getCreateDate(), SurveyType.valueOf(survey.getType()), survey.isPrioritized());

        constructListOfQuestions(survey.getSurveyQuestions());
        
        if(survey instanceof SurveyProduct) {
        	SurveyProduct surveyProduct = (SurveyProduct)survey;
        	List<Product> prodList = surveyProduct.getProducts();
        	if(prodList != null && !prodList.isEmpty()) {
        		this.products = new ArrayList<ProductDTO>();
        		for(Product product : prodList) {
        			ProductDTO productDTO = new ProductDTO();
            		productDTO.setId(product.getId());
        			productDTO.setProductName(product.getName());
        			productDTO.setBrandName(product.getBrand().getName());
        			productDTO.setProductMoneyValue(product.getPriceMoney());
        			
        			this.products.add(productDTO);
        			
        		}
        	}
        	if(this.products == null) this.products = new ArrayList<ProductDTO>();
        }
        
        if(survey instanceof SurveyBrandProductCategory) {
        	SurveyBrandProductCategory surveyBrandProductCategory = (SurveyBrandProductCategory)survey;
        	this.categories = surveyBrandProductCategory.getProductCategories();
        	if(this.categories == null) this.categories = new ArrayList<ProductCategoryLevelThree>();
        }         
        
        if(survey instanceof SurveyProductCategory) {
        	SurveyProductCategory surveyProductCategory = (SurveyProductCategory)survey;
        	this.categories = surveyProductCategory.getProductCategories();
        	if(this.categories == null) this.categories = new ArrayList<ProductCategoryLevelThree>();
        }  
        
        return "surveyInfo";
    }
    
    /**
     * Updates the current survey (actually the main data)
     */
    public void update() {
    	try {
    		//Long id = this.currentSurvey.getId();
	        if(this.surveyService.updateSurvey(this.currentSurvey)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,  
	        			WebConstants.SUCCESS_UPDATE_KEY));    
	        } 
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    }
    // ----- END OF CODE RELATED TO THE CURRENT SURVEY ----- // 
    
    // ----- CODE RELATED TO THE MAIN GRID (nodes of questions and their child options)  ----- //
    /**
     * It initiates the edit process for a selected node item in the main grid (either question or child option).
     * @param item the node item
     */
    public void initEditNode(SurveyItemNodeDTO item) {
    	this.currentItem = item;
    	
        if(Constants.QUESTION_NODE_KEY.equals(item.getNodeType())) {
    		SurveyQuestion question = surveyService.findSurveyQuestion(this.currentItem.getId());

    		// fill in the remaining question's properties
    		this.currentItem.setPosition(question.getPosition());
    		this.currentItem.setSelectMultiple(question.isSelectMultiple());
    		this.currentItem.setOptionRandom(question.isOptionRandom());
    		
    		this.surveyQuestionCriteria = surveyService.findSurveyQuestionCriteriaByQuestionId(this.currentItem.getId());
    		
        } else if(Constants.OPTION_NODE_KEY.equals(item.getNodeType())) {
        	// do nothing at the moment
        }
    }
    
    /**
     * It initiates the delete process for a selected node item in the main grid (either question or child option).
     * @param item
     */
    public void initDeleteNode(SurveyItemNodeDTO item) {
    	this.currentItem = item;
        if(Constants.QUESTION_NODE_KEY.equals(item.getNodeType())) {
        	// do nothing at the moment
        } else if(Constants.OPTION_NODE_KEY.equals(item.getNodeType())) {
        	// do nothing at the moment
        }
    }
    
    /**
     * It initiates the add process for a NEW node item in the main grid (either question or child option).
     * @param parentItem
     */
    public void initAddNode(SurveyItemNodeDTO parentItem) {
    	this.currentItem = new SurveyItemNodeDTO();
    	if(parentItem == null) {  // it's a question
    		this.currentItem.setNodeType(Constants.QUESTION_NODE_KEY);
    		this.currentItem.setSelectMultiple(false);
    		this.currentItem.setOptionRandom(false);
    		
    	} else { // it's an option (since it has parent..)
    		this.currentItem.setNodeType(Constants.OPTION_NODE_KEY);
    		this.currentItem.setParentId(parentItem.getId());  // sets the parent question id
    	}

    }
   
    /**
     * Saves a node in the repository (either question or option).
     */
    public void saveNode() {
    	try {
    		boolean result = false;
    	
	    	if(this.currentItem.getParentId() == null) {  // it's a question
	    		SurveyQuestion question = new SurveyQuestion();
	    		question.setId(this.currentItem.getId());
	    		question.setTitle(this.currentItem.getTitle());
	    		question.setActive(this.currentItem.isActive());
	    		question.setSelectMultiple(this.currentItem.getSelectMultiple());
	    		question.setOptionRandom(this.currentItem.getOptionRandom());
	    		
	    		result = surveyService.saveOrUpdateSurveyQuestion(question, this.currentSurvey.getId());
	    		
	    	} else { // it's an option (since it has parent..)
	    		SurveyOption option = new SurveyOption();
	    		option.setId(this.currentItem.getId());
	    		option.setTitle(this.currentItem.getTitle());
	    		option.setActive(this.currentItem.isActive());
	    		
	    		result = surveyService.addSurveyQuestionOption(option, this.currentItem.getParentId());	
	    	}
	    	
	    	if(result) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,  
	        			WebConstants.SUCCESS_INSERT_KEY));    
	        }
	    	
	    	constructListOfQuestions(fetchSurveyQuestions(this.currentSurvey.getId())); // reload the questions of the main grid
	    	 
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	} 
    }
    
    /**
     * Updates a node in the repository (either question or option).
     */
    public void updateNode() {
    	try {
    		boolean result = false;
    	
	    	if(this.currentItem.getParentId() == null) {  // it's a question
	    		// at the moment the operation is handled in the saveNode() method (because the same button is used for both operations)
	    	} else { // it's an option (since it has parent..)
	    		SurveyOption option = new SurveyOption();
	    		
	    		option.setId(this.currentItem.getId());
	    		option.setTitle(this.currentItem.getTitle());
	    		option.setActive(this.currentItem.isActive());
	    		
	    		result = surveyService.updateSurveyQuestionOption(option);
	    		
	    		if(result) { // success 
		        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,  
		        			WebConstants.SUCCESS_UPDATE_KEY));    
		        }
	    	}
	    	
	    	 constructListOfQuestions(fetchSurveyQuestions(this.currentSurvey.getId())); // reload the questions of the main grid
	    	 
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	} 
    }
    
    /**
     * Deletes a node from the repository (either question or option).
     */
    public void deleteNode() {
    	try {
    		boolean result = false;
    	
	    	if(this.currentItem.getParentId() == null) {  // it's a question	
	    		result = surveyService.deleteSurveyQuestion(this.currentItem.getId(), this.currentSurvey.getId());
	    	} else { // it's an option (since it has parent..)
	    		result = surveyService.deleteSurveyQuestionOption(this.currentItem.getId(), this.currentItem.getParentId());
	    	}
	    	
    		if(result) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,  
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
 	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
 	        			(this.currentItem.getParentId() != null) ? "surveyOption.error.inUse" : "surveyQuestion.error.inUse"));
 	        }
    		
	    	constructListOfQuestions(fetchSurveyQuestions(this.currentSurvey.getId())); // reload the questions of the main grid
	    	 
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	} 
    }
    
    private List<SurveyQuestion> fetchSurveyQuestions(Long surveyId) {
    	return surveyService.findSurveyById(surveyId).getSurveyQuestions();
    }

    private void constructListOfQuestions(List<SurveyQuestion> questions) {
    	
    	this.root = new DefaultTreeNode("root", null);  
    	 
    	if(questions != null && !questions.isEmpty()) { 
    		
    		for(SurveyQuestion question : questions) {
    			SurveyItemNodeDTO questionItemNodeData = new SurveyItemNodeDTO(question.getId(), 
    					question.getTitle(), question.isActive(), Constants.QUESTION_NODE_KEY);
    			TreeNode questionNode = new DefaultTreeNode(questionItemNodeData, root); 
    			
    			List<SurveyOption> options = question.getOptions();
    			if(options != null && !options.isEmpty()) {
    				for(SurveyOption option : options) {
    					SurveyItemNodeDTO optionItemNodeData = new SurveyItemNodeDTO(option.getId(), 
    							option.getTitle(), option.isActive(), Constants.OPTION_NODE_KEY);
    					optionItemNodeData.setParentId(option.getQuestion().getId());
    					new DefaultTreeNode(optionItemNodeData, questionNode); 
    				}
    			}
    	
    		}
    	}
    	
    }
    // ----- END OF CODE RELATED TO THE MAIN GRID ----- // 
    
    // ----- CODE RELATED TO THE CRITERIA POPUP ----- //
    public void initAddCriterion() {
    	this.currentCriterion = new SurveyQuestionCriteria();
    	
    	this.listOfQuestionsInLowerPosition = surveyService.findQuestionsInLowerPosition(this.currentItem.getPosition(), 
    			this.currentSurvey.getId());
    	if(this.listOfQuestionsInLowerPosition != null && !this.listOfQuestionsInLowerPosition.isEmpty()) {
    		this.currentQuestionInLowerPosition = this.listOfQuestionsInLowerPosition.get(0);
    	}	
    }
    
    public void initEditCriterion(SurveyQuestionCriteria criterion) {
    	this.currentCriterion = criterion;
    	
    	this.listOfQuestionsInLowerPosition = surveyService.findQuestionsInLowerPosition(this.currentItem.getPosition(), 
    			this.currentSurvey.getId());
    	this.currentQuestionInLowerPosition = this.currentCriterion.getOption().getQuestion();	   
    }
    
    public void initDeleteCriterion(SurveyQuestionCriteria criterion) {
    	this.currentCriterion = criterion;
    }   
    
    public void saveOrUpdateCriterion() {
    	try {
    		boolean result = false;

    		Long id = this.currentCriterion.getId();
    		
	    	result = surveyService.saveSurveyQuestionCriterion(this.currentCriterion, this.currentItem.getId());
	    	
	    	if(result) { // success 
	    		FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));  
	        }
	    	
	    	initEditNode(this.currentItem);
	    	
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	} 
    }    
    
    public void deleteCriterion() {
    	try {
    		boolean result = surveyService.deleteSurveyQuestionCriterion(this.currentCriterion);

    		if(result) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,  
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
 	        	//FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
 	        	//		 "surveyQuestionCriterion.error.inUse"));  // not used at the moment
 	        }
    		
    		initEditNode(this.currentItem);
    		
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	} 
    }    
    
	//public void currentQuestionInLowerPositionHaChanged(AjaxBehaviorEvent e) { }
    
    // ----- END OF CODE RELATED TO THE CRITERIA POPUP ----- // 
    
    
 // ----- CODE RELATED TO PRODUCTS ASSOCIATED WITH PRODUCT SURVEYS ONLY ----- //
    
    public List<Product> completeProduct(String query) {  
        List<Product> results = null;  
        
        if(query.length() > 0) {
        	results = productService.findByName(query);
        }  
        
        return results;  
    } 
    
    public void addProductInSurvey() {
    	try {
	    	boolean result = surveyService.addProductInProductSurvey(this.currentProduct, 
	    			this.currentSurvey.getId());
	    	if(result) {
	    		ProductDTO productDTO = new ProductDTO();
	    		
	    		productDTO.setId(this.currentProduct.getId());
	    		productDTO.setProductName(this.currentProduct.getName());
				productDTO.setBrandName(this.currentProduct.getBrand().getName());
				productDTO.setProductMoneyValue(this.currentProduct.getPriceMoney());
				
				this.products.add(productDTO);
				
				FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,  
	        			WebConstants.SUCCESS_INSERT_KEY));   
			
	    	}
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    }
    
    public void initProduct(ProductDTO productDTO) {
    	this.currentProductNode = productDTO;
    	if(productDTO == null) { 
    		this.currentProduct = null;
    	}
    }
    
    public void deleteProductFromSurvey() {
    	try {
	    	boolean result = surveyService.deleteProductFromProductSurvey(this.currentProductNode.getId(), 
	    			this.currentSurvey.getId());
	    	if(result) {	
				this.products.remove(this.currentProductNode);
				
				FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,  
	        			WebConstants.SUCCESS_DELETION_KEY));
	    	}
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    }    
    // ----- END OF CODE RELATED TO PRODUCT ASSOCIATIONS ----- //
    
    // ----- CODE RELATED TO PRODUCT CATEGORIES ASSOCIATED WITH PRODUCT CATEGORY SURVEYS ONLY ----- //
    public void initCategory(ProductCategoryLevelThree category) {
    	this.currentCategory = category;
    }
    
    public void addCategoryInSurvey() {
    	try {
	    	boolean result = surveyService.addProductProductLevelThreeInProductCategorySurvey(this.currentCategory, 
	    			this.currentSurvey.getId());
	    	if(result) {				
				this.categories.add(this.currentCategory);
				
				FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,  
	        			WebConstants.SUCCESS_INSERT_KEY));   
			
	    	}
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    }
    
    
    public void deleteCategoryFromSurvey() {
    	try {
	    	boolean result = surveyService.deleteCategoryFromProductSurvey(this.currentCategory, 
	    			this.currentSurvey.getId());
	    	if(result) {	
				this.categories.remove(this.currentCategory);
				
				FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,  
	        			WebConstants.SUCCESS_DELETION_KEY));
	    	}
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    }
    // ----- END OF CODE RELATED TO PRODUCT CATEGORY ASSOCIATIONS ----- //
    
    // --- Getters and Setters --- // 
	public SurveyDTO getCurrentSurvey() {
		return currentSurvey;
	}

	public void setCurrentSurvey(SurveyDTO currentSurvey) {
		this.currentSurvey = currentSurvey;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(TreeNode currentNode) {
		this.currentNode = currentNode;
	}

	public SurveyItemNodeDTO getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(SurveyItemNodeDTO currentItem) {
		this.currentItem = currentItem;
	}

	public List<SurveyQuestionCriteria> getSurveyQuestionCriteria() {
		return surveyQuestionCriteria;
	}

	public void setSurveyQuestionCriteria(
			List<SurveyQuestionCriteria> surveyQuestionCriteria) {
		this.surveyQuestionCriteria = surveyQuestionCriteria;
	}

	public SurveyQuestionCriteria getCurrentCriterion() {
		return currentCriterion;
	}

	public void setCurrentCriterion(SurveyQuestionCriteria currentCriterion) {
		this.currentCriterion = currentCriterion;
	}

	public List<SurveyQuestion> getListOfQuestionsInLowerPosition() {
		return listOfQuestionsInLowerPosition;
	}

	public void setListOfQuestionsInLowerPosition(
			List<SurveyQuestion> listOfQuestionsInLowerPosition) {
		this.listOfQuestionsInLowerPosition = listOfQuestionsInLowerPosition;
	}

	public SurveyQuestion getCurrentQuestionInLowerPosition() {
		return currentQuestionInLowerPosition;
	}

	public void setCurrentQuestionInLowerPosition(
			SurveyQuestion currentQuestionInLowerPosition) {
		this.currentQuestionInLowerPosition = currentQuestionInLowerPosition;
	}

	public List<ProductDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDTO> products) {
		this.products = products;
	}

	public Product getCurrentProduct() {
		return currentProduct;
	}

	public void setCurrentProduct(Product currentProduct) {
		this.currentProduct = currentProduct;
	}

	public List<ProductCategoryLevelThree> getCategories() {
		return categories;
	}

	public void setCategories(List<ProductCategoryLevelThree> categories) {
		this.categories = categories;
	}

	public ProductCategoryLevelThree getCurrentCategory() {
		return currentCategory;
	}

	public void setCurrentCategory(ProductCategoryLevelThree currentCategory) {
		this.currentCategory = currentCategory;
	}


}
