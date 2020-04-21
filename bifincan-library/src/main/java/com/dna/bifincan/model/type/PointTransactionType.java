package com.dna.bifincan.model.type;

public enum PointTransactionType {
	INVITIATION_POINTS("invitationPoints"), 
	INVITIATION_SUCCESS_POINTS("invitationSuccessPoints"),
	SIGNUP_POINTS("signupPoints"), 	
	LOGIN_POINTS("loginPoints"), 
	RATING_POINTS("ratingPoints"),  
	BLOG_COMMENT_POINTS("blogCommentPoints"), 
	PRODUCT_COMMENT_POINTS("productCommentPoints"), 
	SOCIAL_POINTS("socialPoints"), 
	VOTING_POINTS(null), 
	
	PRODUCT_ORDER(null),
	PRODUCT_SURVEY(null),	
	QUIZ_POINTS(null),
	
	GENERIC_SURVEY_POINTS(null);
	
	private String key;
	
	PointTransactionType(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return this.key;
	}
}
