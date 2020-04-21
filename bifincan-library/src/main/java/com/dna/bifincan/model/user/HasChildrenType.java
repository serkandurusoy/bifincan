package com.dna.bifincan.model.user;

public enum HasChildrenType {
	NO("hasChildrenType.no"), 
	YES("hasChildrenType.yes");
	
	String label;
	
	HasChildrenType() { }
	
	HasChildrenType(String label) {
		this.label = label;
	}
	
	public String getName() {
		return label;
	}

	public String getLabel() {
		return label;
	}
}
