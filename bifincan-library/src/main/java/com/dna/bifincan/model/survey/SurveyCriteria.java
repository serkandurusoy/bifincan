package com.dna.bifincan.model.survey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.config.Criterion;
import com.dna.bifincan.model.type.OperatorType;
import com.dna.bifincan.model.type.OperatorValueType;
import com.dna.bifincan.model.type.PropertyType;

@Entity
@Table(name = "survey_criteria")
public class SurveyCriteria extends BaseEntity implements Criterion {
	private static final long serialVersionUID = 6859902106744042448L;

	public SurveyCriteria() { }
	
    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;   

    @Enumerated(EnumType.ORDINAL)
	@Column(name = "property_type")
    private PropertyType propertyType;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "operator_type")
    private OperatorType operatorType;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "operator_value_type")
    private OperatorValueType operatorValueType;  
    
    @Column(name = "operator_value")
    private String operatorValue;
   
	@ManyToOne
    @JoinColumn(name = "option_id")
    private SurveyOption surveyOption;
	
	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public OperatorType getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(OperatorType operatorType) {
		this.operatorType = operatorType;
	}

	public String getOperatorValue() {
		return operatorValue;
	}

	public void setOperatorValue(String operatorValue) {
		this.operatorValue = operatorValue;
	}

	public PropertyType getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(PropertyType propertyType) {
		this.propertyType = propertyType;
	}

	public OperatorValueType getOperatorValueType() {
		return operatorValueType;
	}

	public void setOperatorValueType(OperatorValueType operatorValueType) {
		this.operatorValueType = operatorValueType;
	}
	
	public SurveyOption getSurveyOption() {
		return surveyOption;
	}

	public void setSurveyOption(SurveyOption surveyOption) {
		this.surveyOption = surveyOption;
	}	
	
	public String getFullDescription() {
		return this.surveyOption != null ?  this.surveyOption.getTitle() + ", " + 
					this.surveyOption.getQuestion().getTitle() : "";
	}	
}
