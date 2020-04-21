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
 * The persistent class for the district database table.
 * 
 */
@Entity
@Table(name = "district" /*, uniqueConstraints = @UniqueConstraint(name = "UQ_county_name", columnNames = { "county", "name" })*/)
public class District extends com.dna.bifincan.model.BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2516069871366405282L;

	@Column(name = "name", nullable = false, length = 250)
	@NotNull
	@NotEmpty
	private String name;

	@Column(name = "postal_code", nullable = false, length = 5)
	@NotNull
	@NotEmpty
	private String postalCode;

	// bi-directional many-to-one association to Area
	@OneToMany(mappedBy = "district", cascade = CascadeType.ALL)
	private Set<Area> areas = new HashSet<Area>();

	// bi-directional many-to-one association to County
	@ManyToOne
	@JoinColumn(name = "county", nullable = false)
	private County county;

	public District() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPostalCode() {
		return this.postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Set<Area> getAreas() {
		return this.areas;
	}

	public void setAreas(Set<Area> areas) {
		this.areas = areas;
	}

	public County getCounty() {
		return this.county;
	}

	public void setCounty(County county) {
		this.county = county;
	}

}