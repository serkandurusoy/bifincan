package com.dna.bifincan.model.survey;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "survey_cycle")
public class SurveyCycle implements Serializable {
	private static final long serialVersionUID = 5056821710811204303L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT unsigned", nullable = false)
    public Long id;

    public SurveyCycle() { }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	

}
