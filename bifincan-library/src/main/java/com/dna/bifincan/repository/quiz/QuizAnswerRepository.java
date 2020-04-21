package com.dna.bifincan.repository.quiz;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.model.quiz.QuizAnswer;
import com.dna.bifincan.model.user.User;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {
	
	@Query("select q from QuizAnswer q where q.orderDetail = ?1 and q.user = ?2")
	public QuizAnswer findAnswerByOrderDetailAndUser(OrderDetails orderDetail, User user);
	
	@Query("select count(q.id) from QuizAnswer q where q.orderDetail.id in (:orderItemIds) and q.user.id = :userId")
	public Long countQuizAnswersByOrderDetailsAndUserId(@Param("orderItemIds") List<Long> orderItemIds, @Param("userId") Long userId);
	
	@Query("select count(q) from QuizAnswer q where q.quizOption.quizQuestion.id = :quizQuestionId")
	public Long countByQuizQuestion(@Param("quizQuestionId")Long quizQuestionId);	
	
	@Query("select count(q) from QuizAnswer q where q.quizOption.id = :quizOptionId")
	public Long countByQuizOption(@Param("quizOptionId")Long quizOptionId);		
	
}
