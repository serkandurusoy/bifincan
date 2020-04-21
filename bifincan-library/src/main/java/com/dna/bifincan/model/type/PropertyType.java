package com.dna.bifincan.model.type;

public enum PropertyType {
	/*
	 (18:16) Serkan Durusoy: pastproductquantity
(18:16) Serkan Durusoy: pastproductpointvalue
(18:16) Serkan Durusoy: both have <= and >= operands
(18:16) Serkan Durusoy: and value is integer
	 */
	AGE("propertyType.age"), 
	ADDRESS("propertyType.address"), 
	EDUCATION("propertyType.education"), 
	EMPLOYMENT_STATUS("propertyType.employmentStatus"), 
	GENDER("propertyType.gender"), 
	HAS_CHILDREN("propertyType.hasChildren"), 
	MARITAL_STATUS("propertyType.maritalStatus"), 
	MONTHLY_INCOME("propertyType.monthlyIncome"),
	PAST_PRODUCT_QUANTITY("propertyType.pastProductQuantity"),
	PAST_PRODUCT_POINT_VALUE("propertyType.pastProductPointValue"),
	ID_VERIFICATION("propertyType.idVerification"),
	ACTIVITY_LEVEL("propertyType.activityLevel");
	
	String label;
	
	PropertyType() { }
	
	PropertyType(String label) {
		this.label = label;
	}
	
	public String getName() {
		return label;
	}

	public String getLabel() {
		return label;
	}
}
