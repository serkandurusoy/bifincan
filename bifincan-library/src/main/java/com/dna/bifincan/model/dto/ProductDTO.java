package com.dna.bifincan.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductDTO implements Serializable {
	private static final long serialVersionUID = 5370020312360642680L;

	private Long id;
	private String productName;
	private Integer productPointValue;
	private Integer stockQuantity;
	private Integer orderQuantity;
	private Integer loss;
	private String productDescription;
	private String productKeywords;
	private String productShortDescription;
	private BigDecimal productMoneyValue;
	private String facebookLink;
	private String twitterLink;
	private String productSlug;
	private String brandName;
	private boolean orderable;
	private boolean available;
	private boolean removable;
	private String classifier;
	
	public ProductDTO() { }

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

	public Integer getProductPointValue() {
		return productPointValue;
	}

	public void setProductPointValue(Integer productPointValue) {
		this.productPointValue = productPointValue;
	}

	public Integer getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public Integer getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public Integer getLoss() {
		return loss;
	}

	public void setLoss(Integer loss) {
		this.loss = loss;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductKeywords() {
		return productKeywords;
	}

	public void setProductKeywords(String productKeywords) {
		this.productKeywords = productKeywords;
	}

	public BigDecimal getProductMoneyValue() {
		return productMoneyValue;
	}

	public void setProductMoneyValue(BigDecimal productMoneyValue) {
		this.productMoneyValue = productMoneyValue;
	}

	public String getFacebookLink() {
		return facebookLink;
	}

	public void setFacebookLink(String facebookLink) {
		this.facebookLink = facebookLink;
	}

	public String getTwitterLink() {
		return twitterLink;
	}

	public void setTwitterLink(String twitterLink) {
		this.twitterLink = twitterLink;
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

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isRemovable() {
		return removable;
	}

	public void setRemovable(boolean removable) {
		this.removable = removable;
	}

	public boolean isOrderable() {
		return orderable;
	}

	public void setOrderable(boolean orderable) {
		this.orderable = orderable;
	}
	
	public String getClassifier() {
		return classifier;
	}

	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((productName == null) ? 0 : productName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductDTO other = (ProductDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		return true;
	}

	
}
