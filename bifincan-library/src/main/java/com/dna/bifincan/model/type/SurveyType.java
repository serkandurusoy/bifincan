package com.dna.bifincan.model.type;

public enum SurveyType {
    PROFILE("surveyType.profile"), 
    BRAND("surveyType.brand"), 
    PROD_CAT("surveyType.prodcat"), 
    BRAND_PROD_CAT("surveyType.brandprodcat"), 
    PROD("surveyType.prod");
    
	String label;
	String discriminator;
	
	SurveyType() { }
	
	SurveyType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
