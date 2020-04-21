package com.dna.bifincan.model.products;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import com.dna.bifincan.model.BaseEntity;

@Entity
@Table(name = "product_price_source", uniqueConstraints = { @UniqueConstraint(name = "UQ_name", columnNames = "name"),
	@UniqueConstraint(name = "UQ_url", columnNames = "url") })
public class ProductPriceSource extends BaseEntity implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5153643527226746697L;

    @Column(name = "name", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    private String name;

    @Column(name = "url", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    @URL
    private String url;

    public ProductPriceSource() {
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((url == null) ? 0 : url.hashCode());
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
	ProductPriceSource other = (ProductPriceSource) obj;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	if (url == null) {
	    if (other.url != null)
		return false;
	} else if (!url.equals(other.url))
	    return false;
	return true;
    }

}