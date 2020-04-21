package com.dna.bifincan.model.user;

public enum EmploymentStatusType {
	UNEMPLOYED("employmentStatusType.unemployed"), 
	PART_TIME("employmentStatusType.parttime"), 
	FULLTIME("employmentStatusType.fulltime"), 
	SELF_EMPLOYED("employmentStatusType.selfEmployed");
	
	String label;
	
	EmploymentStatusType() { }
	
	EmploymentStatusType(String label) {
		this.label = label;
	}
	
	public String getName() {
		return label;
	}

	public String getLabel() {
		return label;
	}	
}
