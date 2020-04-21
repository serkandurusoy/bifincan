package com.dna.bifincan.model.products;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.dna.bifincan.model.BaseEntity;

@Entity
@Table(name = "product_category_3" /*, 
		uniqueConstraints = @UniqueConstraint(name = "UQ_name_parent", columnNames = { "product_category_2","name" } )*/)
public class ProductCategoryLevelThree extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6724384989149392174L;
	
	@NotNull
	@NotEmpty      
	@Column(name = "name", nullable = false, length = 250)  	
	private String name;

	@ManyToOne
	@JoinColumn(name = "product_category_2", nullable = false)
	private ProductCategoryLevelTwo parent;

	@Column(name = "prioritized", nullable = false, columnDefinition = "TINYINT(1) unsigned")
	private boolean prioritized;
	
	public ProductCategoryLevelThree() { }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProductCategoryLevelTwo getParent() {
		return parent;
	}

	public void setParent(ProductCategoryLevelTwo parent) {
		this.parent = parent;
	}

	public boolean isPrioritized() {
		return prioritized;
	}

	public void setPrioritized(boolean prioritized) {
		this.prioritized = prioritized;
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
		ProductCategoryLevelThree other = (ProductCategoryLevelThree) obj;
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
