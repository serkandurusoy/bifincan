package com.dna.bifincan.repository.quiz;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.quiz.QuizQuestion;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

	@Query(value = "select q from QuizQuestion q where q.product = :product and q.id not in (select qa.quizOption.quizQuestion.id from QuizAnswer qa where qa.orderDetail = :orderDetail)")
	public List<QuizQuestion> findByProduct(@Param(value = "product") Product product, @Param(value = "orderDetail") OrderDetails orderDetail);
	
	@Query(value = "select count(q) from QuizQuestion q where q.product.id = :productId")
	public Long countByProduct(@Param("productId")Long productId);
	
	@Query(value = "select q from QuizQuestion q where q.product.id = :productId")
	public QuizQuestion findByProduct(@Param("productId")Long productId);	
	
}
