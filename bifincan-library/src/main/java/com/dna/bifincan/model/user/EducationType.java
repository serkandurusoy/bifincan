package com.dna.bifincan.model.user;

public enum EducationType {
	NONE("educationType.none"), 
	PRIMARY_SCHOOL("educationType.primarySchool"), 
	HIGH_SCHOOL("educationType.highSchool"), 
	HIGHER_EDUCATION("educationType.higherEducation"), 
	UNIVERSITY("educationType.university"), 
	GRADUATE("educationType.graduate"), 
	DOCTORATE("educationType.doctorate");
	
	String label;
	
	EducationType() { }
	
	EducationType(String label) {
		this.label = label;
	}
	
	public String getName() {
		return label;
	}

	public String getLabel() {
		return label;
	}

}
