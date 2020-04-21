package com.dna.bifincan.model.products;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.dna.bifincan.model.BaseEntity;

@Entity
@Table(name = "product_category_2"/*, 
		uniqueConstraints = @UniqueConstraint(name = "UQ_name_parent", columnNames = { "product_category_1","name" } )*/)
public class ProductCategoryLevelTwo extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 2457458024121139364L;
	
        @NotNull
        @NotEmpty      
        @Column(name = "name", nullable = false, length = 250)  	
        private String name;

        @ManyToOne
        @JoinColumn(name = "product_category_1", nullable = false)	
        private ProductCategoryLevelOne parent;
    
	public ProductCategoryLevelTwo() { }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ProductCategoryLevelOne getParent() {
		return parent;
	}

	public void setParent(ProductCategoryLevelOne parent) {
		this.parent = parent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductCategoryLevelTwo other = (ProductCategoryLevelTwo) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}

}
