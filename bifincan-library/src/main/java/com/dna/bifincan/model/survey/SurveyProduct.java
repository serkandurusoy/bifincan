package com.dna.bifincan.model.survey;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.dna.bifincan.model.products.Product;

/**
 * 
 * Used for Product Stock Survey 
 *
 */
@Entity
@DiscriminatorValue("PROD")
public class SurveyProduct extends Survey {
	private static final long serialVersionUID = -527554436117371320L;

	@ManyToMany
	@JoinTable(name = "survey_product", joinColumns = @JoinColumn(name = "survey_id", referencedColumnName = "id"), 
			inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
	private List<Product> products;

	public SurveyProduct() {
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}
