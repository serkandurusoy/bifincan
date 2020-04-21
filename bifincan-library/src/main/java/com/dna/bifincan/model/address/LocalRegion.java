package com.dna.bifincan.model.address;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * The persistent class for the local_region database table.
 * 
 */
@Entity
@Table(name = "local_region" /*, uniqueConstraints = @UniqueConstraint(name = "UQ_name", columnNames = "name")*/)
public class LocalRegion extends com.dna.bifincan.model.BaseEntity implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1386409914483892440L;

    @Column(name = "name", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    private String name;

    // bi-directional many-to-one association to City
    @OneToMany(mappedBy = "localRegion", cascade = CascadeType.ALL)
    private Set<City> cities = new HashSet<City>();

    // bi-directional many-to-one association to Country
    @ManyToOne
    @JoinColumn(name = "country", nullable = false)
    private Country country;

    public LocalRegion() {
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Set<City> getCities() {
	return this.cities;
    }

    public void setCities(Set<City> cities) {
	this.cities = cities;
    }

    public Country getCountry() {
	return this.country;
    }

    public void setCountry(Country country) {
	this.country = country;
    }

}