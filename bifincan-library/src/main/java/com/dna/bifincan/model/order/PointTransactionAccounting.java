package com.dna.bifincan.model.order;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.repository.Query;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.user.User;

@Entity
@Table(name = "point_transaction_accounting")
public class PointTransactionAccounting extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3667982209244719326L;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "create_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	//--- point fields ---//
	@Column(name = "invitation_points", nullable=false)
	private int invitationPoints;
	@Column(name = "invitation_success_points", nullable=false)	
	private int invitationSuccessPoints;
	@Column(name = "signup_points", nullable=false)	
	private int signupPoints;
	@Column(name = "login_points", nullable=false)		
	private int loginPoints;
	@Column(name = "rating_points", nullable=false)		
	private int ratingPoints;
	@Column(name = "product_order", nullable=false)		
	private int productOrderPoints;
	@Column(name = "product_survey", nullable=false)		
	private int productSurveyPoints;
	@Column(name = "quiz_points", nullable=false)		
	private int quizPoints;
	@Column(name = "generic_survey_points", nullable=false)		
	private int genericSurveyPoints;
	@Column(name = "blog_comment_points", nullable=false)		
	private int blogCommentPoints;
	@Column(name = "product_comment_points", nullable=false)		
	private int productCommentPoints;
	@Column(name = "social_points", nullable=false)		
	private int socialPoints;
	@Column(name = "voting_points", nullable=false)		
	private int votingPoints;
	
	// .. add more if needed
	// --- end --- //
	
	public PointTransactionAccounting() { }
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getInvitationPoints() {
		return invitationPoints;
	}

	public void setInvitationPoints(int invitationPoints) {
		this.invitationPoints = invitationPoints;
	}

	public int getInvitationSuccessPoints() {
		return invitationSuccessPoints;
	}

	public void setInvitationSuccessPoints(int invitationSuccessPoints) {
		this.invitationSuccessPoints = invitationSuccessPoints;
	}

	public int getSignupPoints() {
		return signupPoints;
	}

	public void setSignupPoints(int signupPoints) {
		this.signupPoints = signupPoints;
	}

	public int getLoginPoints() {
		return loginPoints;
	}

	public void setLoginPoints(int loginPoints) {
		this.loginPoints = loginPoints;
	}

	public int getRatingPoints() {
		return ratingPoints;
	}

	public void setRatingPoints(int ratingPoints) {
		this.ratingPoints = ratingPoints;
	}

	public int getProductOrderPoints() {
		return productOrderPoints;
	}

	public void setProductOrderPoints(int productOrderPoints) {
		this.productOrderPoints = productOrderPoints;
	}

	public int getProductSurveyPoints() {
		return productSurveyPoints;
	}

	public void setProductSurveyPoints(int productSurveyPoints) {
		this.productSurveyPoints = productSurveyPoints;
	}

	public int getQuizPoints() {
		return quizPoints;
	}

	public void setQuizPoints(int quizPoints) {
		this.quizPoints = quizPoints;
	}

	public int getGenericSurveyPoints() {
		return genericSurveyPoints;
	}

	public void setGenericSurveyPoints(int genericSurveyPoints) {
		this.genericSurveyPoints = genericSurveyPoints;
	}

	public int getBlogCommentPoints() {
		return blogCommentPoints;
	}

	public void setBlogCommentPoints(int blogCommentPoints) {
		this.blogCommentPoints = blogCommentPoints;
	}

	public int getProductCommentPoints() {
		return productCommentPoints;
	}

	public void setProductCommentPoints(int productCommentPoints) {
		this.productCommentPoints = productCommentPoints;
	}
	
	public int getSocialPoints() {
		return socialPoints;
	}

	public void setSocialPoints(int socialPoints) {
		this.socialPoints = socialPoints;
	}

	public int getVotingPoints() {
		return votingPoints;
	}

	public void setVotingPoints(int votingPoints) {
		this.votingPoints = votingPoints;
	}

	@Override
	public String toString() {
		return "PointTransactionAccounting [user=" + user
                                + ", createDate=" + createDate
                                + ", invitationPoints=" + invitationPoints
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
