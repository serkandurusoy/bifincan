package com.dna.bifincan.model.address;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * The persistent class for the global_region database table.
 * 
 */
@Entity
@Table(name = "global_region", uniqueConstraints = @UniqueConstraint(name = "UQ_name", columnNames = "name"))
public class GlobalRegion extends com.dna.bifincan.model.BaseEntity implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3676888597457897596L;

    @Column(name = "name", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    private String name;

    // bi-directional many-to-one association to Country
    @OneToMany(mappedBy = "globalRegion", cascade = CascadeType.ALL)
    private Set<Country> countries = new HashSet<Country>();

    public GlobalRegion() {
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Set<Country> getCountries() {
	return this.countries;
    }

    public void setCountries(Set<Country> countries) {
	this.countries = countries;
    }

}