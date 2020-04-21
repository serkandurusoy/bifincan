package com.dna.bifincan.model.products;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.address.Country;

@Entity
@Table(name = "brand", uniqueConstraints = @UniqueConstraint(name = "UQ_name", columnNames = "name"))
public class Brand extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -5153643527226746697L;

    @Column(name = "name", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    private String name;

    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToMany
    @JoinTable(name = "brand_product_category_3", 
    		joinColumns = { 
			@JoinColumn(name = "brand_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "categories_id", 
					nullable = false, updatable = false) })    
    private List<ProductCategoryLevelThree> categories = new ArrayList<ProductCategoryLevelThree>();

    @Column(name = "logo")
    private Long logo;

    @Column(name = "url_blog", length = 512)
    @URL
    private String urlBlog;

    @Column(name = "url_website", length = 512)
    @URL
    private String urlWebsite;

    @Column(name = "url_twitter", length = 512)
    @URL
    private String urlTwitter;

    @Column(name = "url_facebook", length = 512)
    @URL
    private String urlFacebook;

    @Column(name = "slug", nullable = false, length = 250)
    @NotNull
    @NotEmpty    
    private String slug;
    
	@Column(name = "prioritized", nullable = false, columnDefinition = "TINYINT(1) unsigned")
	private boolean prioritized;
	
    public Brand(String _name) {
	this.name = _name;
    }

    public Brand() {
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    public Country getCountry() {
	return country;
    }

    public void setCountry(Country country) {
	this.country = country;
    }

	public Long getLogo() {
		return logo;
	}

	public void setLogo(Long logo) {
		this.logo = logo;
	}

	public String getUrlBlog() {
	return urlBlog;
    }

    public void setUrlBlog(String urlBlog) {
	this.urlBlog = urlBlog;
    }

    public String getUrlWebsite() {
	return urlWebsite;
    }

    public void setUrlWebsite(String urlWebsite) {
	this.urlWebsite = urlWebsite;
    }

    public String getUrlTwitter() {
	return urlTwitter;
    }

    public void setUrlTwitter(String urlTwitter) {
	this.urlTwitter = urlTwitter;
    }

    public String getUrlFacebook() {
	return urlFacebook;
    }

    public void setUrlFacebook(String urlFacebook) {
	this.urlFacebook = urlFacebook;
    }

    public List<ProductCategoryLevelThree> getCategories() {
	return categories;
    }

    public void setCategories(List<ProductCategoryLevelThree> categories) {
	this.categories = categories;
    }

    public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	@Override
    public String toString() {
	return "Brand [name=" + name + "]";
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
	result = prime * result + ((company == null) ? 0 : company.hashCode());
	result = prime * result + ((country == null) ? 0 : country.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
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
	Brand other = (Brand) obj;
	if (company == null) {
	    if (other.company != null)
		return false;
	} else if (!company.equals(other.company))
	    return false;
	if (country == null) {
	    if (other.country != null)
		return false;
	} else if (!country.equals(other.country))
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }

}