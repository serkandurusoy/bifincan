package com.dna.bifincan.repository.order;

import java.util.Date;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.order.PointTransactionAccounting;
import com.dna.bifincan.model.user.User;

public interface PointTransactionAccountingRepository extends JpaRepository<PointTransactionAccounting, Long>, PointTransactionAccountingRepositoryCustom {
	
	@Query("select o from PointTransactionAccounting o where o.createDate between :from and :to and o.user.username = :username")
	public PointTransactionAccounting findTransactionByUsernameAndRecentDate(@Param("username") String username, 
			@Param("from") Date from, @Param("to") Date to);

	@Query("select o from PointTransactionAccounting o where o.createDate between :from and :to and o.user.username = :username order by o.createDate desc")
	public List<PointTransactionAccounting> findTransactionsByUsernameAndDateSelection(@Param("username") String username, 
			@Param("from") Date from, @Param("to") Date to);
	
	@Query("select sum(o.invitationPoints) + sum(o.invitationSuccessPoints) + sum(o.signupPoints) + " +
			"sum(o.loginPoints) + sum(o.ratingPoints) + sum(o.productOrderPoints) + " +
			"sum(o.productSurveyPoints) + sum(o.quizPoints) + sum(o.genericSurveyPoints) + sum(o.blogCommentPoints) + sum(o.productCommentPoints) + sum(o.socialPoints) + sum(o.votingPoints)" +
			" from PointTransactionAccounting o where o.user.username = :username")
	public Long findTotalPointsByUsername(@Param("username") String username);	
	
	@Query("select sum(o.invitationPoints) + sum(o.invitationSuccessPoints) + sum(o.signupPoints) + " +
			"sum(o.loginPoints) + sum(o.ratingPoints) + " +
			"sum(o.productSurveyPoints) + sum(o.quizPoints) + sum(o.genericSurveyPoints) + sum(o.blogCommentPoints) + sum(o.productCommentPoints) + sum(o.socialPoints) + sum(o.votingPoints)" +
			" from PointTransactionAccounting o where o.user.username = :username")
	public Long findTotalGainedPointsByUsername(@Param("username") String username);	
	
	@Query("select sum(o.invitationPoints), sum(o.invitationSuccessPoints), sum(o.signupPoints), " +
			"sum(o.loginPoints), sum(o.ratingPoints), sum(o.productOrderPoints), " +
			"sum(o.productSurveyPoints), sum(o.quizPoints), sum(o.genericSurveyPoints), sum(o.blogCommentPoints), sum(o.productCommentPoints), sum(o.socialPoints), sum(o.votingPoints)" +
			" from PointTransactionAccounting o where o.user.username = :username")
	public List<Object> findGroupedTotalPoints(@Param("username") String username);	



	// @Query("select sum(o.invitationPoints+o.invitationSuccessPoints+o.signupPoints+o.loginPoints+o.ratingPoints+o.productSurveyPoints+o.quizPoints+o.genericSurveyPoints+o.blogCommentPoints+o.productCommentPoints+o.socialPoints+o.votingPoints) from PointTransactionAccounting o where o.createDate>= :from and o.createDate<= :to")
	// public Long weeklyTotalPoints(@Param("from")Date from, @Param("to")Date to);

	@Query("select sum(o.signupPoints+o.loginPoints+o.ratingPoints+o.productSurveyPoints+o.quizPoints+o.genericSurveyPoints+o.blogCommentPoints+o.productCommentPoints+o.socialPoints+o.votingPoints) from PointTransactionAccounting o where o.createDate>= :from and o.createDate<= :to")
	public Long weeklyTotalPointsWithoutInvitationPoints(@Param("from")Date from, @Param("to")Date to);

	@Query("select sum(o.signupPoints+o.loginPoints+o.ratingPoints+o.productSurveyPoints+o.quizPoints+o.genericSurveyPoints+o.blogCommentPoints+o.productCommentPoints+o.socialPoints+o.votingPoints+o.invitationPoints+o.invitationSuccessPoints) from PointTransactionAccounting o where o.createDate>= :from and o.createDate<= :to")
	public Long weeklyTotalPointsWithInvitationPoints(@Param("from")Date from, @Param("to")Date to);

	//@Query("select count(distinct o.user ) from PointTransactionAccounting o where o.createDate>= :from and o.createDate<= :to")
	//public Long weeklyTotalUsers(@Param("from")Date from, @Param("to")Date to);
        
        // @Query("select sum(o.invitationPoints+o.invitationSuccessPoints+o.signupPoints+o.loginPoints+o.ratingPoints+o.productSurveyPoints+o.quizPoints+o.genericSurveyPoints+o.blogCommentPoints+o.productCommentPoints+o.socialPoints+o.votingPoints) from PointTransactionAccounting o where o.createDate>= :from and o.createDate<= :to and o.user=:user")    
        // public Long weeklyUserTotalPoints(@Param("user") User user,@Param("from")Date from, @Param("to")Date to);
        
        @Query("select sum(o.signupPoints+o.loginPoints+o.ratingPoints+o.productSurveyPoints+o.quizPoints+o.genericSurveyPoints+o.blogCommentPoints+o.productCommentPoints+o.socialPoints+o.votingPoints) from PointTransactionAccounting o where o.createDate>= :from and o.createDate<= :to and o.user.username=:username")    
        public Long weeklyUserTotalPointsWithoutInvitationPoints(@Param("username") String user,@Param("from")Date from, @Param("to")Date to);
        
        @Query("select sum(o.signupPoints+o.loginPoints+o.ratingPoints+o.productSurveyPoints+o.quizPoints+o.genericSurveyPoints+o.blogCommentPoints+o.productCommentPoints+o.socialPoints+o.votingPoints+o.invitationPoints+o.invitationSuccessPoints) from PointTransactionAccounting o where o.createDate>= :from and o.createDate<= :to and o.user.username=:username")    
        public Long weeklyUserTotalPointsWithInvitationPoints(@Param("username") String user,@Param("from")Date from, @Param("to")Date to);
        
}
