package com.dna.bifincan.model.dto;

import java.io.Serializable;
import java.util.List;

import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.model.products.Brand;
import com.dna.bifincan.model.products.ProductCategoryLevelThree;
import com.dna.bifincan.model.quiz.QuizOption;
import com.dna.bifincan.model.quiz.QuizQuestion;
import com.dna.bifincan.model.rating.Rating;
import com.dna.bifincan.model.survey.SurveyBrand;
import com.dna.bifincan.model.survey.SurveyBrandProductCategory;
import com.dna.bifincan.model.survey.SurveyOption;
import com.dna.bifincan.model.survey.SurveyProductCategory;
import com.dna.bifincan.model.survey.SurveyQuestion;

public class SurveyQuestionDTO implements Serializable {
	private static final String BRAND_PLACEHOLDER = "@@@brand@@@";
	private static final String PRODUCT_CATEGORY1_PLACEHOLDER = "@@@productCategory1@@@";
	private static final String PRODUCT_CATEGORY2_PLACEHOLDER = "@@@productCategory2@@@";
	private static final String PRODUCT_CATEGORY3_PLACEHOLDER = "@@@productCategory3@@@";
	
	// base properties
	private Long id;
	private String title;
	private Integer position;
	private boolean active;
	private boolean selectMultiple;
	private List<SurveyOptionDTO> surveyOptions;
	
	// convenient custom attributes for business logic (not always being set; only on demand)
	private SurveyQuestion surveyQuestion;
	private boolean surveyRestarted;
	
	private Brand brand;
	private String brandName;
	
	private String categoryName;
	private ProductCategoryLevelThree productCategory;
	
	private SurveyOption surveyOption;
	private List<SurveyOption> activeSurveyOptions;
	private List<QuizOption> activeQuizOptions;	
	private List<String> multiAnswers;
	
	private OrderDetails orderDetails;
	private Rating rating;
	private QuizQuestion quizQuestion;
	private QuizOption quizOption;
	
	private String message;
	private boolean nonSurveyItemCompleted;

	private Type type;
	private String productSlug;
	private String brandSlug;
	private boolean surveyStarted;
	private Long lastCycleId;
	private boolean needForNewCycle;
	
	private long expectedResponseTime;
	private long actualResponseTime;	
	private long createdTime;
	
	// -- end of custom attributes
	
	public SurveyQuestionDTO() { }

	public SurveyQuestion getSurveyQuestion() {
		return surveyQuestion;
	}

	public void setSurveyQuestion(SurveyQuestion surveyQuestion) {
		this.surveyQuestion = surveyQuestion;
	}

	public boolean isSurveyRestarted() {
		return surveyRestarted;
	}

	public void setSurveyRestarted(boolean surveyRestarted) {
		this.surveyRestarted = surveyRestarted;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public ProductCategoryLevelThree getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategoryLevelThree productCategory) {
		this.productCategory = productCategory;
	}

	public SurveyOption getSurveyOption() {
		return surveyOption;
	}

	public void setSurveyOption(SurveyOption surveyOption) {
		this.surveyOption = surveyOption;
	}

	public List<String> getMultiAnswers() {
		return multiAnswers;
	}

	public void setMultiAnswers(List<String> multiAnswers) {
		this.multiAnswers = multiAnswers;
	}

	public OrderDetails getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(OrderDetails orderDetails) {
		this.orderDetails = orderDetails;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isNonSurveyItemCompleted() {
		return message == null ? false : true;
	}

	public QuizQuestion getQuizQuestion() {
		return quizQuestion;
	}

	public void setQuizQuestion(QuizQuestion quizQuestion) {
		this.quizQuestion = quizQuestion;
	}

	public QuizOption getQuizOption() {
		return quizOption;
	}

	public void setQuizOption(QuizOption quizOption) {
		this.quizOption = quizOption;
	}
	
	public String getProductSlug() {
		return productSlug;
	}

	public void setProductSlug(String productSlug) {
		this.productSlug = productSlug;
	}

	public String getBrandSlug() {
		return brandSlug;
	}

	public void setBrandSlug(String brandSlug) {
		this.brandSlug = brandSlug;
	}

	public boolean isSurveyStarted() {
		return surveyStarted;
	}

	public void setSurveyStarted(boolean surveyStarted) {
		this.surveyStarted = surveyStarted;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public boolean isSelectMultiple() {
		return selectMultiple;
	}

	public void setSelectMultiple(boolean selectMultiple) {
		this.selectMultiple = selectMultiple;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<SurveyOption> getActiveSurveyOptions() {
		return activeSurveyOptions;
	}

	public void setActiveSurveyOptions(List<SurveyOption> activeSurveyOptions) {
		this.activeSurveyOptions = activeSurveyOptions;
	}

	public List<QuizOption> getActiveQuizOptions() {
		return activeQuizOptions;
	}

	public void setActiveQuizOptions(List<QuizOption> activeQuizOptions) {
		this.activeQuizOptions = activeQuizOptions;
	}
	
	public Long getLastCycleId() {
		return lastCycleId;
	}

	public void setLastCycleId(Long lastCycleId) {
		this.lastCycleId = lastCycleId;
	}

	public boolean isNeedForNewCycle() {
		return needForNewCycle;
	}

	public void setNeedForNewCycle(boolean needForNewCycle) {
		this.needForNewCycle = needForNewCycle;
	}

	
	public long getExpectedResponseTime() {
		return expectedResponseTime;
	}

	public void setExpectedResponseTime(long expectedResponseTime) {
		this.expectedResponseTime = expectedResponseTime;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	
	public long getActualResponseTime() {
		return actualResponseTime;
	}

	public void setActualResponseTime(long actualResponseTime) {
		this.actualResponseTime = actualResponseTime;
	}

	public String getFormattedQuestionText() {
		if(getSurveyQuestion() != null && getSurveyQuestion().getSurvey() != null) {
			if(getSurveyQuestion().getSurvey() instanceof SurveyBrand) {
				String title = getSurveyQuestion().getTitle();
				if(this.brandName != null) { 
					title = title.replaceAll(BRAND_PLACEHOLDER, this.brandName);
				}	
				return title;
			} else if(getSurveyQuestion().getSurvey() instanceof SurveyBrandProductCategory ||
						getSurveyQuestion().getSurvey() instanceof SurveyProductCategory) {
				String title = getSurveyQuestion().getTitle();
				if(this.getProductCategory() != null) {
					title = title.replaceAll(BRAND_PLACEHOLDER, this.brandName);
					title = title.replaceAll(PRODUCT_CATEGORY1_PLACEHOLDER, this.getProductCategory().getParent().getParent().getName());
					title = title.replaceAll(PRODUCT_CATEGORY2_PLACEHOLDER, this.getProductCategory().getParent().getName());
					title = title.replaceAll(PRODUCT_CATEGORY3_PLACEHOLDER, this.getProductCategory().getName());
				}
				return title;

			} else {
				return getSurveyQuestion().getTitle();
			}
		}
		return null;
	}
	
	public enum Type {
		GENERIC_SURVEY, PRODUCT_SURVEY, QUIZ, RATING 
	}	
}
