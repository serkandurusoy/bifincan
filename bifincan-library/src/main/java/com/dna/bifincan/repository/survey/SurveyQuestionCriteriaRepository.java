package com.dna.bifincan.repository.survey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.survey.SurveyOption;
import com.dna.bifincan.model.survey.SurveyQuestionCriteria;
import com.dna.bifincan.model.type.SurveyQuestionCriteriaType;

public interface SurveyQuestionCriteriaRepository extends JpaRepository<SurveyQuestionCriteria, Long> {
	
	@Query("select count(s) from SurveyQuestionCriteria s where s.question.id = :questionId and " +
		   "s.option in (:options) and s.rule = :rule")
	public Long findByQuestionIdAndAnswerOptionsAndRule(@Param("questionId") 
			Long questionId, @Param("options") List<SurveyOption> options, @Param("rule") SurveyQuestionCriteriaType rule);
	
	@Query("select count(s) from SurveyQuestionCriteria s where s.question.id = :questionId and s.rule = :rule")
	public Long findByQuestionIdAndRule(@Param("questionId") Long questionId, @Param("rule") SurveyQuestionCriteriaType rule);	
	
	@Query("select s from SurveyQuestionCriteria s where s.question.id = :questionId")
	public List<SurveyQuestionCriteria> findByQuestionId(@Param("questionId") Long questionId);		
}
