package com.dna.bifincan.model.type;

public enum OperatorValueType {
	STRING("operatorValueType.string"), 
	INTEGER("operatorValueType.integer"), 
	DECIMAL("operatorValueType.decimal"), 
	BOOLEAN("operatorValueType.boolean");
	
	String label;
	
	OperatorValueType() { }
	
	OperatorValueType(String label) {
		this.label = label;
	}
	
	public String getName() {
		return label;
	}

	public String getLabel() {
		return label;
	}
}
