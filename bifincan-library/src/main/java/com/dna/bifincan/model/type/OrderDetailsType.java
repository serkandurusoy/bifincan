package com.dna.bifincan.model.type;

public enum OrderDetailsType {
	WELCOME_PRODUCT("orderDetailsType.welcome"), 
	SURPRISE_PRODUCT("orderDetailsType.surprise"), 
	ORDERABLE_PRODUCT("orderDetailsType.orderable");
	
	String label;
	
	OrderDetailsType() { }
	
	OrderDetailsType(String label) {
		this.label = label;
	}
	
	public String getName() {
		return label;
	}

	public String getLabel() {
		return label;
	}	
}
