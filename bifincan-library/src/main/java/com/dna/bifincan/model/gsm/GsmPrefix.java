package com.dna.bifincan.model.gsm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The persistent class for the gsm_prefix database table.
 * 
 */
@Entity
@Table(name = "gsm_prefix", uniqueConstraints = @UniqueConstraint(name = "UQ_code", columnNames = "code"))
public class GsmPrefix extends com.dna.bifincan.model.BaseEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7267401055590043238L;
    @Column(name = "code", nullable = false, columnDefinition = "INT(4) unsigned")
    private Integer code;

    public GsmPrefix() {
    }

    public Integer getCode() {
	return code;
    }

    public void setCode(Integer code) {
	this.code = code;
    }

    @Override
    public String toString() {
	return "GsmPrefix [id=" + getId() + ",code=" + code + "]";
    }

}