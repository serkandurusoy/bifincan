package com.dna.bifincan.model.survey;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.dna.bifincan.model.products.ProductCategoryLevelThree;

/**
 * 
 * Used for Brand + Product Category - Survey 
 *
 */
@Entity
@DiscriminatorValue("BRAND_PROD_CAT")
public class SurveyBrandProductCategory extends Survey {
	private static final long serialVersionUID = 1688125448841214220L;

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "survey_brand_product_category", joinColumns = @JoinColumn(name = "survey_id", referencedColumnName = "id"), 
			inverseJoinColumns = @JoinColumn(name = "product_category3_id", referencedColumnName = "id"))
	private List<ProductCategoryLevelThree> productCategories;

	public SurveyBrandProductCategory() {
	}

	public List<ProductCategoryLevelThree> getProductCategories() {
		return productCategories;
	}

	public void setProductCatrgories(List<ProductCategoryLevelThree> productCategories) {
		this.productCategories = productCategories;
	}

}
