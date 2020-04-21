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
 * The persistent class for the city database table.
 * 
 */
@Entity
@Table(name = "city"  /*, uniqueConstraints = @UniqueConstraint(name = "UQ_name", columnNames = "name")*/)
public class City extends com.dna.bifincan.model.BaseEntity implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5153643527226746697L;

    @Column(name = "household", nullable = false, columnDefinition = "INT unsigned")
    private Long household;

    @Column(name = "name", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    private String name;

    @Column(name = "population", nullable = false, columnDefinition = "INT unsigned")
    private Long population;

    // bi-directional many-to-one association to LocalRegion
    @ManyToOne
    @JoinColumn(name = "local_region", nullable = false)
    private LocalRegion localRegion;

    // bi-directional many-to-one association to County
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private Set<County> counties = new HashSet<County>();

    public City() {
    }

    public Long getHousehold() {
	return this.household;
    }

    public void setHousehold(Long household) {
	this.household = household;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Long getPopulation() {
	return this.population;
    }

    public void setPopulation(Long population) {
	this.population = population;
    }

    public LocalRegion getLocalRegion() {
	return this.localRegion;
    }

    public void setLocalRegion(LocalRegion localRegion) {
	this.localRegion = localRegion;
    }

    public Set<County> getCounties() {
	return this.counties;
    }

    public void setCounties(Set<County> counties) {
	this.counties = counties;
    }

}