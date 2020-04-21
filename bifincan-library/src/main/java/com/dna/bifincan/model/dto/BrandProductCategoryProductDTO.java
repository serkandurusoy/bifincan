package com.dna.bifincan.model.dto;

import com.dna.bifincan.model.products.Brand;
import com.dna.bifincan.model.products.ProductCategoryLevelThree;


public class BrandProductCategoryProductDTO {

	//private BrandProductCategoryQuestion brandProductCategoryQuestion;
	private Brand brand;
	private ProductCategoryLevelThree productCategory;
	
	public BrandProductCategoryProductDTO()
	{
		
	}

//	public BrandProductCategoryQuestion getBrandProductCategoryQuestion() {
//		return brandProductCategoryQuestion;
//	}
//
//	public void setBrandProductCategoryQuestion(
//			BrandProductCategoryQuestion brandProductCategoryQuestion) {
//		this.brandProductCategoryQuestion = brandProductCategoryQuestion;
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
}
