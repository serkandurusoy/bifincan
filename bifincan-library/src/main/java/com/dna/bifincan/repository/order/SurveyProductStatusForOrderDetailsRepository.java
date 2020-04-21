package com.dna.bifincan.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.model.order.SurveyProductStatusForOrderDetails;
import com.dna.bifincan.model.survey.Survey;
import com.dna.bifincan.model.user.User;

public interface SurveyProductStatusForOrderDetailsRepository extends JpaRepository<SurveyProductStatusForOrderDetails, Long> {

	public SurveyProductStatusForOrderDetails findBySurveyAndUserAndOrderDetail(Survey survey, User user, 
			OrderDetails orderDetail);
	
	public SurveyProductStatusForOrderDetails findByUserAndOrderDetail(User user, OrderDetails orderDetail);
	
	@Query("select s from SurveyProductStatusForOrderDetails s where s.survey = ?1 and s.user = ?2 and s.surveyCompleted = false")
	public List<SurveyProductStatusForOrderDetails> findBySurveyAndUser(Survey survey, User user);	
	
	
}
