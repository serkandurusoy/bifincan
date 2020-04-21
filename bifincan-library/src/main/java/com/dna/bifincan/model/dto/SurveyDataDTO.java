package com.dna.bifincan.model.dto;

import java.io.Serializable;

import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.model.quiz.QuizQuestion;
import com.dna.bifincan.model.rating.Rating;
import com.dna.bifincan.model.survey.Survey;

public class SurveyDataDTO implements Serializable {
	private static final long serialVersionUID = -4964994904370873718L;
	
	private Survey survey;
	private Rating rating;
	private QuizQuestion quizQuestion;
	private OrderDetails orderDetails;
	private boolean surveyAlreadyTaken;
	private boolean surveyStarted;
	public Long lastCycleId;
	
	public SurveyDataDTO() { }
	
	public Survey getSurvey() {
		return survey;
	}
	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
	public Boolean isSurveyAlreadyTaken() {
		return surveyAlreadyTaken;
	}
	public void setSurveyAlreadyTaken(boolean surveyAlreadyTaken) {
		this.surveyAlreadyTaken = surveyAlreadyTaken;
	}
	public OrderDetails getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(OrderDetails orderDetails) {
		this.orderDetails = orderDetails;
	}
	public Rating getRating() {
		return rating;
	}
	public void setRating(Rating rating) {
		this.rating = rating;
	}
	public QuizQuestion getQuizQuestion() {
		return quizQuestion;
	}
	public void setQuizQuestion(QuizQuestion quizQuestion) {
		this.quizQuestion = quizQuestion;
	}
	public boolean isSurveyStarted() {
		return surveyStarted;
	}
	public void setSurveyStarted(boolean surveyStarted) {
		this.surveyStarted = surveyStarted;
	}
	public Long getLastCycleId() {
		return lastCycleId;
	}
	public void setLastCycleId(Long lastCycleId) {
		this.lastCycleId = lastCycleId;
	}	
}
