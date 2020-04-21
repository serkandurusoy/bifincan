package com.dna.bifincan.model.type;

public enum SurveyQuestionCriteriaType {
    OPTION_REQUIRED_BY_QUESTION("questionCriteriaType.optionRequired"), 
    OPTION_RESTRICTS_QUESTION("questionCriteriaType.optionRestricts");
    
	String label;
	
	SurveyQuestionCriteriaType() { }
	
	SurveyQuestionCriteriaType(String label) {
		this.label = label;
	}
	
	public String getName() {
		return label;
	}

	public String getLabel() {
		return label;
	}
}
