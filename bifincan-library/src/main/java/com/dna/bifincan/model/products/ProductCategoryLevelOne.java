package com.dna.bifincan.model.products;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.type.ProductCategoryType;

@Entity
@Table(name = "product_category_1", uniqueConstraints = @UniqueConstraint(name = "UQ_name", columnNames = "name"))
public class ProductCategoryLevelOne extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 192313714131943187L;

    @NotNull
    @NotEmpty      
    @Column(name = "name", nullable = false, length = 250)  	
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "business_type", nullable = false, length = 60)	    
    private ProductCategoryType type;
    
	public ProductCategoryLevelOne() { }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProductCategoryType getType() {
		return type;
	}

	public void setType(ProductCategoryType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		ProductCategoryLevelOne other = (ProductCategoryLevelOne) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
