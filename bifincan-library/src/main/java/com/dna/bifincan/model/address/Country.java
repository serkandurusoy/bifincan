package com.dna.bifincan.model.address;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
 * The persistent class for the country database table.
 * 
 */
@Entity
@Table(name = "country" /*, uniqueConstraints = @UniqueConstraint(name = "UQ_name", columnNames = "name")*/)
public class Country extends com.dna.bifincan.model.BaseEntity implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5006461506245123972L;

    @Column(name = "name", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    private String name;

    // bi-directional many-to-one association to GlobalRegion
    @ManyToOne
    @JoinColumn(name = "global_region", nullable = false)
    private GlobalRegion globalRegion;

    // bi-directional many-to-one association to LocalRegion
    @OneToMany(mappedBy = "country")
    private Set<LocalRegion> localRegions = new HashSet<LocalRegion>();

    public Country() {
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public GlobalRegion getGlobalRegion() {
	return this.globalRegion;
    }

    public void setGlobalRegion(GlobalRegion globalRegion) {
	this.globalRegion = globalRegion;
    }

    public Set<LocalRegion> getLocalRegions() {
	return this.localRegions;
    }

    public void setLocalRegions(Set<LocalRegion> localRegions) {
	this.localRegions = localRegions;
    }

}