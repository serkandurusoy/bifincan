package com.dna.bifincan.repository.survey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.survey.SurveyOption;
import com.dna.bifincan.model.survey.SurveyQuestionCriteria;
import com.dna.bifincan.model.type.SurveyQuestionCriteriaType;

public interface SurveyOptionRepository extends JpaRepository<SurveyOption, Long> {
	
	@Query("select o from SurveyOption o where o.question.survey.type = 'PROFILE' and o.title like :name")
	public List<SurveyOption> findProfileOptionsByTitle(@Param("name")String name);
	
	//@Query("select distinct s.id, b.id from SurveyAnswer s left join fetch s.surveyOptions b where s.surveyQuestion.id = ?1")	
	//public List<Object[]> fetchAllAnsweredOptionsByQuestion(Long questionId);		
	
	
	@Query("select distinct o from SurveyOption o where o in (select b from SurveyAnswer s join  s.surveyOptions b where s.surveyQuestion.id = ?1)")
	public List<SurveyOption> fetchAllAnsweredOptionsByQuestion(Long questionId);	
}
