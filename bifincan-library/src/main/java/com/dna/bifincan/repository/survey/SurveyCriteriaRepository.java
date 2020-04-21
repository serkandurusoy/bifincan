package com.dna.bifincan.repository.survey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.survey.SurveyCriteria;
import com.dna.bifincan.model.user.User;

public interface SurveyCriteriaRepository extends JpaRepository<SurveyCriteria, Long> {
	
	@Query("select s from SurveyCriteria s where s.survey.id = :surveyId")
	public List<SurveyCriteria> findBySurveyId(@Param("surveyId") Long surveyId);	
		   
}
