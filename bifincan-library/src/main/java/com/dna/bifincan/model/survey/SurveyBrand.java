package com.dna.bifincan.model.survey;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Used for Brand Survey
 */
@Entity
@DiscriminatorValue("BRAND")
public class SurveyBrand extends Survey {
	private static final long serialVersionUID = 7679099976617254753L;

	public SurveyBrand() {
	}
}
