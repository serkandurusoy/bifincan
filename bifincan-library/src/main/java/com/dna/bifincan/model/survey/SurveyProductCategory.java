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
 * Used for Product Category Survey 
 *
 */
@Entity
@DiscriminatorValue("PROD_CAT")
public class SurveyProductCategory extends Survey {
	private static final long serialVersionUID = -3906527857278193717L;

	@ManyToMany
	@JoinTable(name = "survey_product_category", joinColumns = @JoinColumn(name = "survey_id", referencedColumnName = "id"), 
			inverseJoinColumns = @JoinColumn(name = "product_category3_id", referencedColumnName = "id"))
	private List<ProductCategoryLevelThree> productCategories;

	public SurveyProductCategory() {
	}

	public List<ProductCategoryLevelThree> getProductCategories() {
		return productCategories;
	}

	public void setProductCatrgories(List<ProductCategoryLevelThree> productCategories) {
		this.productCategories = productCategories;
	}

}
