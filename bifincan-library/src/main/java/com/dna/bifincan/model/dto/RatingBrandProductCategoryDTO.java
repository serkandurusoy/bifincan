package com.dna.bifincan.model.dto;

import com.dna.bifincan.model.products.Brand;
import com.dna.bifincan.model.products.ProductCategoryLevelThree;


public class RatingBrandProductCategoryDTO {

	private Brand brand;
	private ProductCategoryLevelThree productCategoryLevelThree;

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public ProductCategoryLevelThree getProductCategoryLevelThree() {
		return productCategoryLevelThree;
	}

	public void setProductCategoryLevelThree(ProductCategoryLevelThree productCategoryLevelThree) {
		this.productCategoryLevelThree = productCategoryLevelThree;
	}
}
