package com.dna.bifincan.model.products;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.config.Criterion;
import com.dna.bifincan.model.survey.SurveyOption;
import com.dna.bifincan.model.type.OperatorType;
import com.dna.bifincan.model.type.OperatorValueType;
import com.dna.bifincan.model.type.PropertyType;
@Entity
@Table(name = "product_order_criteria")
@NamedQueries({
})
public class ProductOrderCriteria extends BaseEntity implements Criterion {
	private static final long serialVersionUID = -5153643527226746697L;

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

	@JoinColumn(name = "product_id", nullable = false)
	@ManyToOne
	Product product;

	@ManyToOne
	@JoinColumn(name = "survey_option_id")
	private SurveyOption surveyOption;

	public ProductOrderCriteria() { }
	
	public ProductOrderCriteria(Product product) {
		super();
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public PropertyType getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(PropertyType propertyType) {
		this.propertyType = propertyType;
	}

	public OperatorType getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(OperatorType operatorType) {
		this.operatorType = operatorType;
	}

	public OperatorValueType getOperatorValueType() {
		return operatorValueType;
	}

	public void setOperatorValueType(OperatorValueType operatorValueType) {
		this.operatorValueType = operatorValueType;
	}

	public String getOperatorValue() {
		return operatorValue;
	}

	public void setOperatorValue(String operatorValue) {
		this.operatorValue = operatorValue;
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