package com.dna.bifincan.web.survey;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.exception.DuplicateUserResponseException;
import com.dna.bifincan.exception.InvalidUserResponseException;
import com.dna.bifincan.exception.SurveyException;
import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.dto.ProductDTO;
import com.dna.bifincan.model.dto.SurveyQuestionDTO;
import com.dna.bifincan.model.order.ShoppingCart;
import com.dna.bifincan.model.products.Brand;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.products.ProductCategoryLevelOne;
import com.dna.bifincan.model.products.ProductCategoryLevelThree;
import com.dna.bifincan.model.products.ProductCategoryLevelTwo;
import com.dna.bifincan.model.quiz.QuizOption;
import com.dna.bifincan.model.survey.Survey;
import com.dna.bifincan.model.survey.SurveyAnswer;
import com.dna.bifincan.model.survey.SurveyBrand;
import com.dna.bifincan.model.survey.SurveyBrandProductCategory;
import com.dna.bifincan.model.survey.SurveyOption;
import com.dna.bifincan.model.survey.SurveyProductCategory;
import com.dna.bifincan.model.survey.SurveyProfile;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.type.ProductClassifier;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.OrderService;
import com.dna.bifincan.service.ProductService;
import com.dna.bifincan.service.SurveyService;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.WebConstants;
import com.dna.bifincan.util.spring.ScopeType;

@Named("surveyAction")
@Scope(ScopeType.VIEW)
public class SurveyAction implements Serializable {
	private static final long serialVersionUID = -3336757756648881516L;
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SurveyAction.class);

	private SurveyQuestionDTO data;
	private ProductDTO availableProduct;
	
	@Inject
	private UserService userService;
	@Inject
	private SurveyService surveyService;
	@Inject
	protected ProductService productService;	
	@Inject
	protected OrderService orderService;	
	@Inject 
	protected ConfigurationService configurationService;
	
	public SurveyAction() { }

	@PostConstruct
	public void initialize() {		
		loadNextSurveyQuestion(null);
	}
	
	public void loadNextSurveyQuestion(ActionEvent evt) {
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		String username = principal.getName();
		
		try {
			User user = userService.findUserByUsername(username);
			
			HttpSession session = (HttpSession)FacesUtils.getExternalContext().getSession(false);
			
			//Boolean needForCycle = (Boolean)session.getAttribute(WebConstants.NEED_FOR_NEW_SURVEY_CYCLE);
			
			data = surveyService.getNextQuestionData(user, session.getId());
			if(data.getSurveyQuestion() != null) {
				if(data.getType() == SurveyQuestionDTO.Type.GENERIC_SURVEY || // check for generic and product surveys
						data.getType() == SurveyQuestionDTO.Type.PRODUCT_SURVEY) { 
					Survey survey = data.getSurveyQuestion().getSurvey();
					if (survey instanceof SurveyProfile) {  // # Profile Survey
						// do nothing at the moment
						data.setBrandName(user.getFirstName() + " " + user.getLastName());
						data.setCategoryName(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
								WebConstants.SURVEY_PROFILE_KEY));
					} else if(survey instanceof SurveyBrand) { // # Brand based Survey

						if(data.isSurveyStarted()) {
							Object[] randomBrandData = surveyService.fetchSurveyRandomUntakenBrandAndProductCategory(user, survey,  
									data.isSurveyRestarted(), session.getId(), data.getLastCycleId());
							data.setBrand((Brand)randomBrandData[0]);
							data.setCategoryName(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
									WebConstants.SURVEY_BRAND_KEY));
						} else {
							SurveyAnswer surveyAnswer = surveyService.fetchLastAnswer(user, 
									data.getSurveyQuestion().getSurvey());
							if(surveyAnswer != null) {
								data.setBrand(surveyAnswer.getBrand());
							} 
						}
				
						data.setBrandName((data.getBrand() != null) ? data.getBrand().getName() : "");  // normally this won't happen
						data.setBrandSlug((data.getBrand() != null) ? data.getBrand().getSlug() : "");
						
					} else if(survey instanceof SurveyBrandProductCategory) {  // # Brand Product Category based Survey
						if(data.isSurveyStarted()) {
							//log.debug("@@ SurveyBrandProductCategory : surveyStarted = true");
							Object[] randomBrandData = surveyService.fetchSurveyRandomUntakenBrandAndProductCategory(user, survey,  
									data.isSurveyRestarted(), session.getId(), data.getLastCycleId());
							data.setBrand((Brand)randomBrandData[0]);
							data.setProductCategory((ProductCategoryLevelThree)randomBrandData[1]);
						} else {
							//log.debug("@@ SurveyBrandProductCategory : surveyStarted = false");
							SurveyAnswer surveyAnswer = surveyService.fetchLastAnswer(user, 
									data.getSurveyQuestion().getSurvey());
							if(surveyAnswer != null) {
								data.setBrand(surveyAnswer.getBrand());
								data.setProductCategory(surveyAnswer.getProductCategory());
								
							} 
						}
				
						data.setBrandName((data.getBrand() != null) ? data.getBrand().getName() : "");  
						data.setBrandSlug((data.getBrand() != null) ? data.getBrand().getSlug() : "");
						
						data.setCategoryName((data.getProductCategory() != null) ? extractProductCategoryPath(data.getProductCategory()) : "" );
						
					} else if(survey instanceof SurveyProductCategory) {  // # Product Category Survey
						SurveyProductCategory prodCatSurvey = (SurveyProductCategory)survey; 
						// there is one 
						ProductCategoryLevelThree category = surveyService.fetchSurveyRandomUntakenProductCategory(user, survey,  data.getLastCycleId());
	
						data.setProductCategory(category);
						data.setCategoryName(extractProductCategoryPath(category));
						
						data.setBrandName(user.getFirstName() + " " + user.getLastName());
					} else {  // # Product Survey
						Product product = data.getOrderDetails().getProduct();
						data.setCategoryName((product != null) ? product.getName() : "");				
						data.setBrandName((product != null && product.getBrand() != null) ? 
								product.getBrand().getName() : ""); // normally this won't happen
						data.setProductSlug(product.getSlug());
					}
					
					// construct the list of active options
					int countOptionWordsPlus1 = 1;
					List<SurveyOption> options = data.getSurveyQuestion().getOptions();
					if(options != null && !options.isEmpty()) {
						List<SurveyOption> activeOptions = new ArrayList<SurveyOption>();
						for(SurveyOption option : options) {
							if(option.isActive()) {
								countOptionWordsPlus1 += option.getTitle().split(" ").length;
								activeOptions.add(option);
							}
						}
						
						// only for generic surveys
						if(data.getType() == SurveyQuestionDTO.Type.GENERIC_SURVEY && 
								data.getSurveyQuestion().isOptionRandom()) {
							Collections.shuffle(activeOptions);
						}
						
						data.setActiveSurveyOptions(activeOptions);
					}
					
					// calculate the expected response time and save it in session
					int countQuestionsWords = (data.getSurveyQuestion() != null) ? 
							data.getSurveyQuestion().getTitle().split(" ").length : 0; 
					long expectedResponseTimeMs = (long)Math.ceil( (((countQuestionsWords + countOptionWordsPlus1 + 2) - 3) / 6.0) + 1 ) - 1;		
					
					////log.debug(">> setting expectedResponseTimeMs2 = " + expectedResponseTimeMs);
					//session.setAttribute(WebConstants.QUESTION_CREATED_TIME, expectedResponseTimeMs*1000);

					data.setExpectedResponseTime(expectedResponseTimeMs*1000);
					data.setCreatedTime((new Date()).getTime());
				} 
			} else {
				if(data.getType() == SurveyQuestionDTO.Type.RATING) { // check for rating 
					Product product = data.getOrderDetails().getProduct();
					data.setCategoryName((product != null) ? product.getName() : "");				
					data.setBrandName((product != null && product.getBrand() != null) ? 
							product.getBrand().getName() : ""); // normally this won't happen
					data.setProductSlug(product.getSlug());
				} else if(data.getType() == SurveyQuestionDTO.Type.QUIZ) {  // check for quiz 
					Product product = data.getOrderDetails().getProduct();
					data.setCategoryName((product != null) ? product.getName() : "");				
					data.setBrandName((product != null && product.getBrand() != null) ? 
							product.getBrand().getName() : ""); // normally this won't happen
					data.setProductSlug(product.getSlug());
					
					// construct the list of active options
					List<QuizOption> options = data.getQuizQuestion().getQuizOptions();
					if(options != null && !options.isEmpty()) {
						List<QuizOption> activeOptions = new ArrayList<QuizOption>();
						for(QuizOption option : options) {
							if(option.isActive()) {
								activeOptions.add(option);
							}
						}
						data.setActiveQuizOptions(activeOptions);
					}					
				}
			}
			
			loadRandomAvailableProduct();
			
		} catch (SurveyException e) {
			log.debug(e.getMessage());
			if(e.getErrorCode() == SurveyException.NO_PRODUCT_SURVEY) {
				FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
					WebConstants.ERROR_NO_PRODUCT_SURVEY));
			} else if(e.getErrorCode() == SurveyException.NO_QUIZ_SURVEY) {
				FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						WebConstants.ERROR_NO_QUIZ_SURVEY));
			}		
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
					WebConstants.ERROR_GENERAL_ERROR_KEY));
		}	
	}

	public void saveAndNextQuestion() {
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		String username = principal.getName();

		try {
			
			data.setActualResponseTime((new Date()).getTime() - data.getCreatedTime());
			
			// check for already answered question
			User user = userService.findUserByUsername(username);
			
			HttpSession session = (HttpSession)FacesUtils.getExternalContext().getSession(false);
		
			int totalPoints = surveyService.saveSurveyAnswer(user, session.getId(), data); 
			
			updateUserPointsInSession(totalPoints);
			
			loadNextSurveyQuestion(null);
		} catch(DuplicateUserResponseException e) {
			log.debug(e.getMessage());
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
					WebConstants.ERROR_DUPLICATE_ANSWER_KEY));		
		} catch(InvalidUserResponseException e) {
			log.debug(e.getMessage());
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
					WebConstants.ERROR_INVALID_ANSWER_KEY));				
		} catch(Exception e) {
			e.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
					WebConstants.ERROR_GENERAL_ERROR_KEY));
		}
		
	}

	public void saveRating() {
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		String username = principal.getName();

		if(data.isNonSurveyItemCompleted() == false) {
			try {
				// check for already answered question
				User user = userService.findUserByUsername(username);

				int totalPoints = surveyService.saveRating(user, data);	
				updateUserPointsInSession(totalPoints);
				
				loadNextSurveyQuestion(null);  // load the next question
			} catch (DuplicateUserResponseException e) {
				log.debug(e.getMessage());
				FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						WebConstants.ERROR_DUPLICATE_RATING_KEY));	
			} catch (InvalidUserResponseException e) {
				log.debug(e.getMessage());
				FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						WebConstants.ERROR_INVALID_RATING_KEY));			
			} catch (Exception e) {
				e.printStackTrace();
				FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						WebConstants.ERROR_GENERAL_ERROR_KEY));
			}
		} else {
			loadNextSurveyQuestion(null);
		}
	}	
	
	public void saveQuizAnswer() {
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		String username = principal.getName();
		
		if(data.isNonSurveyItemCompleted() == false) {
			try {
				// check for already answered question
				User user = userService.findUserByUsername(username);

				Object[] results = surveyService.saveQuizAnswer(user, data);
				QuizOption correctOption = (QuizOption)results[0];
				
				int totalPoints = (Integer)results[1];
				if(totalPoints > 0) updateUserPointsInSession(totalPoints);  // update the balance in session 
				
				if(correctOption == null) {
					data.setMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						"quiz.success"));

				} else {
					data.setMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						"quiz.failure", new String[] { correctOption.getOptionText() }));

				}
				
			} catch (DuplicateUserResponseException e) {
				log.debug(e.getMessage());
				FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						WebConstants.ERROR_DUPLICATE_QUIZ_KEY));	
			} catch (InvalidUserResponseException e) {
				log.debug(e.getMessage());
				FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						WebConstants.ERROR_INVALID_QUIZ_KEY));			
			} catch (Exception e) {
				e.printStackTrace();
				FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						WebConstants.ERROR_GENERAL_ERROR_KEY));
			}
		} else {
			loadNextSurveyQuestion(null);
		}		
	}
	
	// --- other helper methods --- //	
	private void loadRandomAvailableProduct() {
		this.availableProduct = null;  // reset the random product variable
		
		// find the current user entity from database
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		
		if(principal != null) {
			String username = principal.getName();
			
			User user = userService.findUserByUsername(username);
			
			Product randomProduct = productService.findRandomAvailableProduct(user); // fetch a random product
			
			if(randomProduct != null) { 
				Map<String, Object> values = FacesUtils.getExternalContext().getSessionMap();		

				User userToken = (User)values.get(WebConstants.CURRENT_USER_SESSION_KEY);
				ShoppingCart userShoppingCart = userToken != null ? userToken.getShoppingCart() : null;
				
				int maxNoOfOrderableProducts = 0;
				Configuration config = configurationService.find(ConfigurationType.MAX_NO_OF_ORDERABLE_PRODUCTS.getKey());
				if(config != null) maxNoOfOrderableProducts = Integer.parseInt(config.getValue());
				
    			Configuration bounusOrderableConfig = configurationService.find(ConfigurationType.NO_OF_BONUS_ORDERABLE_PRODUCTS.getKey());
                int numberOfBonusOrderableProducts = Integer.parseInt(bounusOrderableConfig.getValue());
                
                if(user.getActivityLevel() == WebConstants.HIGH_USER_ACTIVITY)  maxNoOfOrderableProducts += numberOfBonusOrderableProducts;
                				
				// CHECK whether the user can place a new order  (order scope attributes)
				boolean canUserOrder = orderService.canUserPlaceNewOrder(user);
		
				ProductDTO productDTO = new ProductDTO();
				
				productDTO.setId(randomProduct.getId());
				productDTO.setBrandName(randomProduct.getBrand().getName());
				productDTO.setProductShortDescription(randomProduct.getShortDescription());
				productDTO.setProductMoneyValue(randomProduct.getPriceMoney());
				productDTO.setProductName(randomProduct.getName());
				productDTO.setProductPointValue(randomProduct.getPricePoints());
				productDTO.setProductSlug(randomProduct.getSlug());
				productDTO.setClassifier(randomProduct.getClassifier().toString());
				
	
				if(user.getActivityLevel() == WebConstants.LOW_USER_ACTIVITY) { // low lever activity
					productDTO.setAvailable(false);
				} else if(userShoppingCart != null && userShoppingCart.getProducts().contains(randomProduct.getSlug())) {
					productDTO.setRemovable(true);
					productDTO.setAvailable(false);
				} else if(userShoppingCart != null && userShoppingCart.getProducts().size() > 0 && 
						userShoppingCart.getProducts().size() >= maxNoOfOrderableProducts) {
					productDTO.setAvailable(false);
				} else {  // this concerns generic (order scope) criteria
					productDTO.setAvailable(canUserOrder);
				} 	
				
				this.availableProduct = productDTO;
			}
			
		}		
	}
	
	private String extractProductCategoryPath(ProductCategoryLevelThree productCategory) {
		String path = "";
		
		if(productCategory != null) {
			ProductCategoryLevelTwo parent2 = productCategory.getParent();
			ProductCategoryLevelOne parent1 = parent2.getParent();
			/*path += parent1.getName() + " - " + 
				parent2.getName() + " - " + productCategory.getName();*/
			path += productCategory.getName();
		}
		return path;	
	}
	
	private void updateUserPointsInSession(int totalPoints) {
		Map<String, Object> values = FacesUtils.getExternalContext().getSessionMap();
		User user = (User)values.get(WebConstants.CURRENT_USER_SESSION_KEY);
		
		if(user != null) {
			user.setPointsBalance(totalPoints);
		}	
	}
	
	// -- setters & getters -- /
	public void setData(SurveyQuestionDTO data) {
		this.data = data;
	}

	public SurveyQuestionDTO getData() {
		return data;
	}

	public ProductDTO getAvailableProduct() {
		return availableProduct;
	}

	public void setAvailableProduct(ProductDTO availableProduct) {
		this.availableProduct = availableProduct;
	}
		
}
