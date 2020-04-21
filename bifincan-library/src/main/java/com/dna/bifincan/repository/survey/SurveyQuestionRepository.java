package com.dna.bifincan.repository.survey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.survey.SurveyQuestion;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {
	
	@Query("select s from SurveyQuestion s " +
		   "where s.survey.id = :surveyId and s.position = " +
		   "(select min(s1.position) from SurveyQuestion s1 where s1.position > " +
		   ":position and s1.active=true and s1.survey.id = :surveyId)")
    public List<SurveyQuestion> findNextQuestionByPosition(@Param("position") int lastQuestionPosition, 
    		@Param("surveyId") Long surveyId);

	@Query("select count(o) from SurveyQuestion o where o.survey.id = :surveyId")
	public Long countBySurvey(@Param("surveyId")Long surveyId);
	
	@Query("select max(s.position) from SurveyQuestion s where s.survey.id = ?1")
	public Integer findMaxPositionBySurveyId(Long surveyId);
	
	@Query("select s from SurveyQuestion s where s.position < ?1 and s.survey.id = ?2 order by s.position asc")
	public List<SurveyQuestion> findQuestionsInLowerPosition(Integer position, Long surveyId);
	
	
}
