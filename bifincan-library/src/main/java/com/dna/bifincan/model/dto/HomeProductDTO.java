package com.dna.bifincan.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class HomeProductDTO implements Serializable {
	private static final long serialVersionUID = -4833933684548076765L;
	
	private Long id;
	private String productName;
	private String productShortDescription;
	private String productSlug;
	private String brandName;
	private String questionText;
	private String optionText;
	private Integer percentage;
	
	public HomeProductDTO() { }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductSlug() {
		return productSlug;
	}

	public void setProductSlug(String productSlug) {
		this.productSlug = productSlug;
	}

	public String getProductShortDescription() {
		return productShortDescription;
	}

	public void setProductShortDescription(String productShortDescription) {
		this.productShortDescription = productShortDescription;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getOptionText() {
		return optionText;
	}

	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}
	
}
