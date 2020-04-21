package com.dna.bifincan.model.survey;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 
 * Used for Profile Survey
 *
 */
@Entity
@DiscriminatorValue("PROFILE")
public class SurveyProfile extends Survey implements Serializable {
	private static final long serialVersionUID = -8512221928323622629L;

	public SurveyProfile() {
	}

}
