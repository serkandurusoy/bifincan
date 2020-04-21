package com.dna.bifincan.repository.survey;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.survey.Survey;
import com.dna.bifincan.model.survey.SurveyProduct;
import com.dna.bifincan.model.user.User;

public interface SurveyProductRepository extends JpaRepository<SurveyProduct, Long> {
	@Query("select s from SurveyProduct s where s.active=true and s.id not in (select sps.survey.id from SurveyProductStatusForOrderDetails sps where sps.user = :user and sps.orderDetail.id = :orderDetailID and sps.surveyCompleted = true)")
	public List<SurveyProduct> findActiveProductSurveyByUserAndOrderDetailIDNotUsed(@Param("user") User user, @Param("orderDetailID") Long orderDetailID);
	
	@Query("select count(s.survey.id) from SurveyProductStatusForOrderDetails s where s.user.id = :userId and s.orderDetail.id in (:orderItemIds) and s.surveyCompleted = true")
	public Long countClosedSurveysByOrderDetailsAndUserId(@Param("orderItemIds") List<Long> orderItemIds, @Param("userId") Long userId);
	
	@Query("select s from SurveyProduct s where s.active = true and ?1 member of s.products")
	public List<SurveyProduct> findProductSurveysByProduct(Product product);
	
	@Query("select s.id from SurveyProduct s where s.active = true and ?1 member of s.products")
	public List<Long> findProductSurveyIdsByProduct(Product product);
	
}
