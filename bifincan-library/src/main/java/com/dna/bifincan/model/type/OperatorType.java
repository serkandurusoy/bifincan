package com.dna.bifincan.model.type;

public enum OperatorType {
	EQUAL("operatorType.equal"), 
	GREATER("operatorType.greater"), 
	LESS("operatorType.less"), 
	LIKE("operatorType.like"), 
	GREATER_OR_EQUAL("operatorType.greaterOrEqual"),  // 4 
	LESS_OR_EQUAL("operatorType.lessOrEqual"),   // 5,
	NOT_EQUAL("operatorType.notEqual");   // 6
	
	String label;
	
	OperatorType() { }
	
	OperatorType(String label) {
		this.label = label;
	}
	
	public String getName() {
		return label;
	}

	public String getLabel() {
		return label;
	}

}
