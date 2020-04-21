package com.dna.bifincan.model.user;

public enum MonthlyNetTLIncomeType {
	NONE("monthlyIncomeType.none"), 
	TL_0_1000("monthlyIncomeType.tl_0_1000"), 
	TL_1001_2000("monthlyIncomeType.tl_1001_2000"), 
	TL_2001_3000("monthlyIncomeType.tl_2001_3000"), 
	TL_3001_5000("monthlyIncomeType.tl_3001_5000"), 
	TL_Above_5000("monthlyIncomeType.tl_above_1000");
	
	String label;
	
	MonthlyNetTLIncomeType() { }
	
	MonthlyNetTLIncomeType(String label) {
		this.label = label;
	}
	
	public String getName() {
		return label;
	}

	public String getLabel() {
		return label;
	}
}
