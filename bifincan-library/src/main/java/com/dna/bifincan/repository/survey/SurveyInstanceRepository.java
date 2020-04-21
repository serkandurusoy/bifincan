package com.dna.bifincan.repository.survey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dna.bifincan.model.survey.Survey;
import com.dna.bifincan.model.survey.SurveyInstance;
import com.dna.bifincan.model.user.User;

public interface SurveyInstanceRepository extends JpaRepository<SurveyInstance, Long> {
	
	@Query("select max(s.instance) from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2")
	public SurveyInstance findLatestByUserAndSurvey(User user, Survey survey);
}
