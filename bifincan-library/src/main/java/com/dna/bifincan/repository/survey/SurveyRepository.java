package com.dna.bifincan.repository.survey;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.survey.Survey;
import com.dna.bifincan.model.survey.SurveyInstance;
import com.dna.bifincan.model.user.User;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
	@Query("select count(s) from Survey s where s.active=true and s.type = ?1")
	public Long countActiveSurveysByType(String surveyType);

	@Query("select s.id from Survey s where s.active=true and s.type = ?1 order by s.id")
	public List<Long> findActiveSurveyIdsByType(String surveyType);
	
	@Query("select s from Survey s where s.type = ?1 and s.active = true ")
	public List<Survey> findByTypeAndActive(String surveyType, Pageable pageable);
	
	@Query("select count(s) from Survey s where s.active=true and s.type = ?1 and s.id not in (select distinct(a.surveyQuestion.survey) from SurveyAnswer a where a.session.sessionId = ?2)")
	public Long countActiveSurveysByTypeAndSessionId(String surveyType, String sessionId);

	@Query("select s from Survey s where s.type = ?1 and s.active = true and s.id not in (select distinct(a.surveyQuestion.survey) from SurveyAnswer a where a.session.sessionId = ?2)")
	public List<Survey> findByTypeAndActiveAndSessionId(String surveyType, String sessionId, Pageable pageable);

	//@Query("select s.surveyQuestion.survey.id, s.brand.id, s.productCategory.id from SurveyAnswer s where s.user.id = ? and s.surveyQuestion.survey.active=true and s.surveyQuestion.survey.type <> 'PROD' group by s.surveyQuestion.survey.id, s.brand.id, s.productCategory.id order by s.productCategory.prioritized desc, s.brand.prioritized desc, s.attendanceTime asc")
	//public List<Object[]> findInitialTakenSurveyDataByUser(Long userId);	

	@Query("select distinct s.surveyQuestion.survey.id, s.brand.id, s.productCategory.id from SurveyAnswer s where s.user.id = :userId and s.surveyQuestion.survey.active=true and s.surveyQuestion.survey.type <> 'PROD' order by s.attendanceTime asc")
	public List<Object[]> findInitialTakenSurveyDataByUser(@Param("userId")Long userId);
	
	//@Query("select s.surveyQuestion.survey.id from SurveyAnswer s where s.user.id = ? and s.surveyQuestion.survey.active = true group by s.surveyQuestion.survey.id order by s.attendanceTime")
	//public List<Long> findInitialTakenSurveyIdsByUser(Long userId);	
	
	@Query("select distinct s.surveyQuestion.survey from SurveyAnswer s where s.instance = :instance")
	public Survey findByInstance(@Param("instance")SurveyInstance instance);	
	
}
