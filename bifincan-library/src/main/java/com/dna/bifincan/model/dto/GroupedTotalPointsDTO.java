package com.dna.bifincan.model.dto;

import java.io.Serializable;

public class GroupedTotalPointsDTO implements Serializable {
	private static final long serialVersionUID = 8558991023891011815L;
	
	private long invitationPoints;
	private long invitationSuccessPoints;
	private long signupPoints;
	private long loginPoints;
	private long ratingPoints;
	private long productOrderPoints;
	private long productSurveyPoints;
	private long quizPoints;
	private long genericSurveyPoints;	
	private long blogCommentPoints;
	private long productCommentPoints;
	private long socialPoints;
	private long votingPoints;
	
	public GroupedTotalPointsDTO() { }
	
	public GroupedTotalPointsDTO(long invitationPoints, long invitationSuccessPoints, long signupPoints, long loginPoints,
			long ratingPoints, long productOrderPoints, long productSurveyPoints, long quizPoints, long genericSurveyPoints, 
			long blogCommentPoints, long productCommentPoints, long socialPoints, long votingPoints) {
		this.invitationPoints = invitationPoints;
		this.invitationSuccessPoints = invitationSuccessPoints;
		this.signupPoints = signupPoints;
		this.loginPoints = loginPoints;
		this.ratingPoints = ratingPoints;
		this.productOrderPoints = productOrderPoints;
		this.productSurveyPoints = productSurveyPoints;
		this.quizPoints = quizPoints;
		this.genericSurveyPoints = genericSurveyPoints;
		this.blogCommentPoints = blogCommentPoints;
		this.productCommentPoints = productCommentPoints;
		this.socialPoints = socialPoints;
		this.votingPoints = votingPoints;
	}

	public long getInvitationPoints() {
		return invitationPoints;
	}

	public void setInvitationPoints(long invitationPoints) {
		this.invitationPoints = invitationPoints;
	}

	public long getInvitationSuccessPoints() {
		return invitationSuccessPoints;
	}

	public void setInvitationSuccessPoints(long invitationSuccessPoints) {
		this.invitationSuccessPoints = invitationSuccessPoints;
	}

	public long getSignupPoints() {
		return signupPoints;
	}

	public void setSignupPoints(long signupPoints) {
		this.signupPoints = signupPoints;
	}

	public long getLoginPoints() {
		return loginPoints;
	}

	public void setLoginPoints(long loginPoints) {
		this.loginPoints = loginPoints;
	}

	public long getRatingPoints() {
		return ratingPoints;
	}

	public void setRatingPoints(long ratingPoints) {
		this.ratingPoints = ratingPoints;
	}

	public long getProductOrderPoints() {
		return productOrderPoints;
	}

	public void setProductOrderPoints(long productOrderPoints) {
		this.productOrderPoints = productOrderPoints;
	}

	public long getProductSurveyPoints() {
		return productSurveyPoints;
	}

	public void setProductSurveyPoints(long productSurveyPoints) {
		this.productSurveyPoints = productSurveyPoints;
	}

	public long getQuizPoints() {
		return quizPoints;
	}

	public void setQuizPoints(long quizPoints) {
		this.quizPoints = quizPoints;
	}

	public long getGenericSurveyPoints() {
		return genericSurveyPoints;
	}

	public void setGenericSurveyPoints(long genericSurveyPoints) {
		this.genericSurveyPoints = genericSurveyPoints;
	}
	
	public long getBlogCommentPoints() {
		return blogCommentPoints;
	}

	public void setBlogCommentPoints(long blogCommentPoints) {
		this.blogCommentPoints = blogCommentPoints;
	}

	public long getProductCommentPoints() {
		return productCommentPoints;
	}

	public void setProductCommentPoints(long productCommentPoints) {
		this.productCommentPoints = productCommentPoints;
	}

	public long getSocialPoints() {
		return socialPoints;
	}

	public void setSocialPoints(long socialPoints) {
		this.socialPoints = socialPoints;
	}
	
	public long getVotingPoints() {
		return votingPoints;
	}

	public void setVotingPoints(long votingPoints) {
		this.votingPoints = votingPoints;
	}

	@Override
	public String toString() {
		return "GroupedTotalPointsDTO [invitationPoints=" + invitationPoints
				+ ", invitationSuccessPoints=" + invitationSuccessPoints
				+ ", signupPoints=" + signupPoints
                                + ", loginPoints=" + loginPoints 
                                + ", ratingPoints=" + ratingPoints
				+ ", productOrderPoints=" + productOrderPoints
				+ ", productSurveyPoints=" + productSurveyPoints
				+ ", quizPoints=" + quizPoints 
                                + ", genericSurveyPoints=" + genericSurveyPoints 
                                + ", blogCommentPoints=" + blogCommentPoints 
                                + ", productCommentPoints=" + productCommentPoints
                                + ", socialPoints=" + socialPoints 
                                + ", votingPoints=" + votingPoints                                 
                                + "]";
	}
		
}
