package com.dna.bifincan.model.user;

public enum MaritalStatusType {
	MARRIED("maritalStatusType.married"), 
	SINGLE("maritalStatusType.single");
	
	String label;
	
	MaritalStatusType() { }
	
	MaritalStatusType(String label) {
		this.label = label;
	}
	
	public String getName() {
		return label;
	}

	public String getLabel() {
		return label;
	}	
}
