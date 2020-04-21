package com.dna.bifincan.model.address;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * The persistent class for the county database table.
 * 
 */
@Entity
@Table(name = "county" /*, uniqueConstraints = @UniqueConstraint(name = "UQ_county_name", columnNames = { "city", "name" })*/)
@NamedQueries({
	@NamedQuery(name="County.findAllByCity", query = "from County county where county.city = :city  order by county.name")
})	
public class County extends com.dna.bifincan.model.BaseEntity implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -694490491773396680L;

    @Column(name = "name", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    private String name;

    @Column(name = "phone_prefix", nullable = false, columnDefinition = "INT(7) unsigned")
    private Long phonePrefix;

    // bi-directional many-to-one association to City
    @ManyToOne
    @JoinColumn(name = "city", nullable = false)
    private City city;

    // bi-directional many-to-one association to District
    @OneToMany(mappedBy = "county", cascade = CascadeType.ALL)
    private Set<District> districts;

    public County() {
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Long getPhonePrefix() {
	return this.phonePrefix;
    }

    public void setPhonePrefix(Long phonePrefix) {
	this.phonePrefix = phonePrefix;
    }

    public City getCity() {
	return this.city;
    }

    public void setCity(City city) {
	this.city = city;
    }

    public Set<District> getDistricts() {
	return this.districts;
    }

    public void setDistricts(Set<District> districts) {
	this.districts = districts;
    }

}