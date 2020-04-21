package com.dna.bifincan.model.dto;

import com.dna.bifincan.model.products.Brand;
import com.dna.bifincan.model.products.ProductCategoryLevelThree;
import com.dna.bifincan.model.survey.SurveyAnswer;
import com.dna.bifincan.model.survey.SurveyQuestion;

public class AnsweredQuestionDTO {

//	private SurveyQuestionType surveyType;
	private Brand brand;
	private ProductCategoryLevelThree productCategory;
	private SurveyQuestion surveyQuestion;
	private SurveyAnswer surveyAnswer;
	
	public AnsweredQuestionDTO()
	{
		
	}

//	public SurveyQuestionType getSurveyType() {
	//	return surveyType;
//	}

//	public void setSurveyType(SurveyQuestionType surveyType) {
//		this.surveyType = surveyType;
//	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public ProductCategoryLevelThree getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategoryLevelThree productCategory) {
		this.productCategory = productCategory;
	}

	public SurveyQuestion getSurveyQuestion() {
		return surveyQuestion;
	}

	public void setSurveyQuestion(SurveyQuestion surveyQuestion) {
		this.surveyQuestion = surveyQuestion;
	}

	public SurveyAnswer getSurveyAnswer() {
		return surveyAnswer;
	}

	public void setSurveyAnswer(SurveyAnswer surveyAnswer) {
		this.surveyAnswer = surveyAnswer;
	}
}
