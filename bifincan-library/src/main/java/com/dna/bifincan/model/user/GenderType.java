package com.dna.bifincan.model.user;

public enum GenderType {
	FEMALE("genderType.female"), 
	MALE("genderType.male");
	
	String label;
	
	GenderType() { }
	
	GenderType(String label) {
		this.label = label;
	}
	
	public String getName() {
		return label;
	}

	public String getLabel() {
		return label;
	}
}
