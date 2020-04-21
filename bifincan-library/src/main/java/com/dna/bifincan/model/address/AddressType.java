package com.dna.bifincan.model.address;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * The persistent class for the address_type database table.
 * 
 */
@Entity
@Table(name = "address_type", uniqueConstraints = @UniqueConstraint(name = "UQ_name", columnNames = "name"))
public class AddressType extends com.dna.bifincan.model.BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7036318402700769842L;

	@Column(name = "name", nullable = false, length = 250)
	@NotNull
	@NotEmpty
	private String name;

	// bi-directional many-to-one association to Address
	@OneToMany(mappedBy = "addressType")
	private Set<Address> addresses = new HashSet<Address>();

	public AddressType() {
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

}