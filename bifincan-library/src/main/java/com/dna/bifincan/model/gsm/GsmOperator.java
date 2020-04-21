package com.dna.bifincan.model.gsm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * The persistent class for the gsm_operator database table.
 * 
 */
@Entity
@Table(name = "gsm_operator", uniqueConstraints = @UniqueConstraint(name = "UQ_name", columnNames = "name"))
public class GsmOperator extends com.dna.bifincan.model.BaseEntity implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4526023839028617293L;

    @Column(name = "name", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    private String name;

    public GsmOperator() {
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	return "GsmOperator [id=" + this.getId() + ",name=" + name + "]";
    }

}