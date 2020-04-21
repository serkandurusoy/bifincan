package com.dna.bifincan.model.order;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.survey.Survey;
import com.dna.bifincan.model.user.User;

@Entity
@Table(name = "survey_product_status_for_order_details")
public class SurveyProductStatusForOrderDetails extends BaseEntity implements Serializable  {

	private static final long serialVersionUID = 2141857292981274668L;

	@ManyToOne
	@JoinColumn(name = "survey_id", nullable = false)
	private Survey survey;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "order_detail_id", nullable = false)
	private OrderDetails orderDetail;
	
	@Column(name = "survey_completed", nullable = false, columnDefinition = "TINYINT(1) unsigned")
	private boolean surveyCompleted;
	
	public SurveyProductStatusForOrderDetails()
	{
		
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public OrderDetails getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(OrderDetails orderDetail) {
		this.orderDetail = orderDetail;
	}

	public boolean isSurveyCompleted() {
		return surveyCompleted;
	}

	public void setSurveyCompleted(boolean surveyCompleted) {
		this.surveyCompleted = surveyCompleted;
	}
	
}
