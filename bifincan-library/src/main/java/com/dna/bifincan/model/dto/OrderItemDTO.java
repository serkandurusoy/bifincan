package com.dna.bifincan.model.dto;

import java.io.Serializable;

public class OrderItemDTO implements Serializable {
	private static final long serialVersionUID = 5724871963609470383L;
	
	private Long productId;
	private int points;
	private String name;
	private String brandName;
	private boolean welcomeProduct;
	private boolean surpriseProduct;
	private String slug;
	
	public OrderItemDTO() { }

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isWelcomeProduct() {
		return welcomeProduct;
	}

	public void setWelcomeProduct(boolean welcomeProduct) {
		this.welcomeProduct = welcomeProduct;
	}

	public boolean isSurpriseProduct() {
		return surpriseProduct;
	}

	public void setSurpriseProduct(boolean surpriseProduct) {
		this.surpriseProduct = surpriseProduct;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
}
