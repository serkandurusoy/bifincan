package com.dna.bifincan.model.type;

public enum ConfigurationType {
	NUM_INNVITATIONS_PER_DAY("numInvitationsPerDay"), 
	DAYS_REQUIRED_BEFORE_NEXT_ORDER("daysRequireBeforeNextOrder"),
	TIME_REQUIRED_BEFORE_PRODUCT_SURVEYS("timeRequiredBeforeProductSurveys"), 	
	MAX_NO_OF_WELCOME_PRODUCTS("maxNoOfWelcomeProducts"), 
	MAX_NO_OF_ORDERABLE_PRODUCTS("noOfOrderableProducts"),
	MAX_NO_OF_SURPRISE_PRODUCTS("maxNoOfSurpiseProducts"),
	DAYS_REQUIRED_BEFORE_PRODUCT_IS_ORDERABLE("daysRequireBeforeProdIsOrderable"),
	APPLICATION_LOCALE("applicationLocale"),
	APPLICATION_CANONICAL_URL("applicationCanonicalURL"),
	NO_OF_BONUS_ORDERABLE_PRODUCTS("noOfBonusOrderableProducts"),
	SMS_GATEWAY_USERNAME("smsGatewayUsername"),
	SMS_GATEWAY_PASSWORD("smsGatewayPassword");
	
	private String key;
	
	ConfigurationType(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return this.key;
	}
	
}
