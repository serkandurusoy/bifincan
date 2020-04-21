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
 * The persistent class for the area database table.
 * 
 */
@Entity
@Table(name = "area" /*, uniqueConstraints = @UniqueConstraint(name = "UQ_district_name", columnNames = { "district", "name" })*/)
public class Area extends com.dna.bifincan.model.BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2614511613606097977L;

	@Column(name = "name", nullable = false, length = 250)
	@NotNull
	@NotEmpty
	private String name;

	// bi-directional many-to-one association to Address
	@OneToMany(mappedBy = "area")
	private Set<Address> addresses = new HashSet<Address>();

	// bi-directional many-to-one association to District
	@ManyToOne
	@JoinColumn(name = "district", nullable = false)
	// @org.hibernate.annotations.Index(name="idx_district")
	private District district;

	public Area() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Address> getAddresses() {
		return this.addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public District getDistrict() {
		return this.district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

}