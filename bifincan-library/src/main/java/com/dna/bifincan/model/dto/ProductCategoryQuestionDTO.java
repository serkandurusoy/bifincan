package com.dna.bifincan.model.dto;

import com.dna.bifincan.model.products.ProductCategoryLevelThree;


public class ProductCategoryQuestionDTO {

	// private ProductCategoryQuestion productCategoryQuestion;
	private ProductCategoryLevelThree productCategory;

	public ProductCategoryQuestionDTO() {

	}

//	public ProductCategoryQuestion getProductCategoryQuestion() {
//		return productCategoryQuestion;
//	}
//
//	public void setProductCategoryQuestion(ProductCategoryQuestion productCategoryQuestion) {
//		this.productCategoryQuestion = productCategoryQuestion;
//	}

	public ProductCategoryLevelThree getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategoryLevelThree productCategory) {
		this.productCategory = productCategory;
	}
}
