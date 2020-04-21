package com.dna.bifincan.repository.survey;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.products.Brand;
import com.dna.bifincan.model.products.ProductCategoryLevelThree;
import com.dna.bifincan.model.survey.Survey;
import com.dna.bifincan.model.survey.SurveyAnswer;
import com.dna.bifincan.model.survey.SurveyInstance;
import com.dna.bifincan.model.survey.SurveyOption;
import com.dna.bifincan.model.survey.SurveyQuestion;
import com.dna.bifincan.model.user.User;
import java.util.Date;

public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Long> {

	public List<SurveyAnswer> findByUser(User user, Pageable pageable);
	public List<SurveyAnswer> findByUserAndSurveyQuestionSurvey(User user, Survey survey, Pageable pageable);
	
	@Query("select s from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.instance = ?3 order by s.id desc")				
	public List<SurveyAnswer> findByUserAndMaxSurveyInstance(User user, Survey survey, SurveyInstance surveyInstance);	
	
	@Query("select count(s) from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.session.sessionId = ?3")
	public Long countSurveyAnswersByUserAndSurveyAndSessionId(User user, Survey survey, String sessionId);
	
	@Query("select count(s) from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.cycle.id = ?3")
	public Long countSurveyAnswersByUserAndSurveyAndCycleId(User user, Survey survey, Long cycleId);
	
	@Query("select count(s) from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2")
	public Long countSurveyAnswersByUserAndSurvey(User user, Survey survey);
	
	@Query("select count(s) from SurveyAnswer s where s.user = ?1 and s.attendanceTime > ?2")
	public Long countSurveyAnswersByUserSinceDate(User user, Date sinceDate);
	
	@Query("select max(s.id) from SurveyAnswer s where s.user = ?1")
	public Long checkAnySurveyAnswerByUser(User user);
	
	@Query("select count(s) from SurveyAnswer s where s.user = ?1 and s.session.sessionId = ?2 and s.surveyQuestion = ?3")
	public Long countSurveyAnswersByUserAndQuestion(User user, String sessionId, SurveyQuestion question);

	@Query("select count(s) from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.session.sessionId = ?3 and s.surveyQuestion.position > ?4")
	public Long countSurveyAnswersByUserAndQuestionPosition(User user, Survey survey, String sessionId, Integer position);
	
	@Query("select count(b) from Brand b where b.id not in " +
			"(select s.brand.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.brand is not null and s.session.sessionId = ?3)")				
	public Long countNonTakenBrandSurveysByUserAndSessionId(User user, Survey survey, String sessionId);
	
	@Query("select count(b) from Brand b where b.id not in " +
			"(select s.brand.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.brand is not null and s.cycle.id = ?3)")				
	public Long countNonTakenBrandSurveysByUserAndCycleId(User user, Survey survey, Long cycleId);
	
	@Query("select count(s) from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.brand.id = ?3 and s.session.sessionId = ?4)")				
	public Long countSurveysByBrandAndUserAndSessionId(User user, Survey survey, Long brandId, String sessionId);

	@Query("select count(b) from Brand b where b.id not in " +
			"(select s.brand.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.brand is not null)")				
	public Long countNonTakenBrandSurveysByUser(User user, Survey survey);
	
	@Query("select count(s) from SurveyAnswer s where s.user = ?1 and s.session.sessionId = ?2 and s.surveyQuestion = ?3 and s.brand = ?4)")				
	public Long coundBySurveyQuestionAndUserAndSessionIdAndBrand(User user, String sessionId, SurveyQuestion surveyQuestion, Brand brand);
	
	@Query("select count(s) from SurveyAnswer s where s.user = ?1 and s.session.sessionId = ?2 and s.surveyQuestion = ?3 and productCategory = ?4 and s.brand = ?5)")				
	public Long coundBySurveyQuestionAndUserAndSessionIdAndProductCategoryBrand(User user, String sessionId, SurveyQuestion surveyQuestion, ProductCategoryLevelThree productCategory, Brand brand);
	
	@Query("select count(s) from SurveyAnswer s where s.user = ?1 and s.session.sessionId = ?2 and s.surveyQuestion = ?3 and productCategory = ?4)")				
	public Long coundBySurveyQuestionAndUserAndSessionIdAndProductCategory(User user, String sessionId, SurveyQuestion surveyQuestion, ProductCategoryLevelThree productCategory);
	
	@Query("select count(s) from SurveyAnswer s where s.user = ?1 and s.session.sessionId = ?2 and s.surveyQuestion = ?3)")				
	public Long coundBySurveyQuestionAndUserAndSessionId(User user, String sessionId, SurveyQuestion surveyQuestion);
		
	@Query("select count(b) from Brand b where ?3 member of b.categories and b.id not in " +
			"(select s.brand.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.brand is not null and s.session.sessionId = ?4)")			
	public Long countNonTakenBrandSurveysByUserAndProductCategoryAndSessionId(User user, Survey survey, ProductCategoryLevelThree category, String sessionId);	
	
	@Query("select count(b) from Brand b where ?3 member of b.categories and b.id not in " +
			"(select s.brand.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.brand is not null and s.cycle.id = ?4)")			
	public Long countNonTakenBrandSurveysByUserAndProductCategoryAndCycleId(User user, Survey survey, ProductCategoryLevelThree category, Long cycleId);	
	

	@Query("select count(b) from Brand b where ?3 member of b.categories and b.id not in " +
			"(select s.brand.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.brand is not null)")			
	public Long countNonTakenBrandSurveysByUserAndProductCategory(User user, Survey survey, ProductCategoryLevelThree category);	
	
	//@Query("select count(b) from Brand b left join fetch b.categories c where c.id in (?3) and b.id not in " +
	//		"(select distinct s.brand.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.brand is not null)")			
	//public Long countNonTakenBrandSurveysByUserAndProductCategories(User user, Survey survey, List<ProductCategoryLevelThree> categories);
	
	//@Query("select distinct(s.brand.id) from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.brand is not null and s.session.sessionId = ?3")
	//public List<Long> fetchTakenSurveyBrandIdsByUserAndSessionId(User user, Survey survey, String sessionId);	
	
	@Query("select distinct(s.brand.id) from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.brand is not null and s.cycle.id = ?3")
	public List<Long> fetchTakenSurveyBrandIdsByUserAndCycleId(User user, Survey survey, Long cycleId);	
	
	@Query("select max(s) from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2")
	public SurveyAnswer fetchLastSurveyAnswerByUserAndSurvey(User user, Survey survey);	
	
	@Query("select distinct(s.brand.id) from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and ?3 member of s.brand.categories and s.cycle.id = ?4")
	public List<Long> fetchTakenSurveyBrandIdsByUserAndProductCategoryAndCycleId(User user, Survey survey, ProductCategoryLevelThree category, Long cycleId);		
	
	@Query("select distinct s.brand.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.productCategory in (?3) and s.cycle.id is null order by s.brand.prioritized desc")
	public List<Long> fetchTakenBrandIdsByUserAndProductCategories(User user, Survey survey, List<ProductCategoryLevelThree> categories);

	@Query("select distinct s.brand.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.productCategory in (?3) and s.cycle.id = ?4 order by s.brand.prioritized desc")
	public List<Long> fetchTakenBrandIdsByUserAndProductCategoriesAndCycleId(User user, Survey survey, List<ProductCategoryLevelThree> categories, Long cycleId);
	
	@Query("select distinct s.productCategory.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.brand.id = ?3 and s.cycle.id = ?4 order by s.productCategory.prioritized desc")
	public List<Long> fetchTakenSurveyProdCatIdsByUserAndBrandIdAndCycleId(User user, Survey survey, Long brandId, Long cycleId);

	@Query("select distinct s.productCategory.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.brand.id = ?3 and s.cycle.id is null order by s.productCategory.prioritized desc")
	public List<Long> fetchTakenSurveyProdCatIdsByUserAndBrandId(User user, Survey survey, Long brandId);
	
	//@Query("select distinct(s.brand.id) from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and ?3 member of s.brand.categories and s.session.sessionId = ?4")
	//public List<Long> fetchTakenSurveyBrandIdsByUserAndProductCategoryAndSessionId(User user, Survey survey, ProductCategoryLevelThree category, String sessionId);		
	
	@Query("select count(o) from SurveyAnswer o where o.brand.id = :brandId")
	public Long countByBrand(@Param("brandId")Long brandId);	
	
	@Query("select count(s) from SurveyAnswer s where :option member of s.surveyOptions")				
	public Long countBySurveyOption(@Param("option")SurveyOption option);	
	
	@Query("select count(s) from SurveyAnswer s where s.surveyQuestion.id = :questionId")				
	public Long countBySurveyQuestionId(@Param("questionId")Long questionId);	
        
	@Query("select count(s.id) from SurveyAnswer s"  /*" left join s.surveyOptions o "*/)	
	public Long countAnswerOptions();
	
	@Query("select max(s.cycle.id) from SurveyAnswer s where s.user.id = ?1 and s.cycle is not null")	
	public Long findLastCycleByUser(Long userId);	
	
//	@Query("select s.surveyQuestion.survey.id from SurveyAnswer s where s.cycle.id = ? group by s.instance.id order by s.attendanceTime ")	
//	public List<Long> findSurveyIdsByCycle(Long cycleId);	
	
	@Query("select s.surveyQuestion.survey.id, s.brand.id, s.productCategory.id  from SurveyAnswer s where s.cycle.id = :cycleId and s.surveyQuestion.survey.type <> 'PROD' group by s.instance.id, s.surveyQuestion.survey.id, s.brand.id, s.productCategory.id, s.attendanceTime  order by s.attendanceTime ")	
	public List<Object[]> findSurveyDataByCycle(@Param("cycleId")Long cycleId);	
	
	@Query("select max(o) from SurveyAnswer o where o.surveyQuestion = ?1")	
	public SurveyAnswer fetchLastAnswerByQuestion(SurveyQuestion question);
	
	@Query("select o from SurveyAnswer o where o.surveyQuestion.id = ?1 and o.user.id in (?2) and o.id = (select max(o1.id) from SurveyAnswer o1 where o1.surveyQuestion.id = o.surveyQuestion.id and o1.user.id = o.user.id)")	
	public List<SurveyAnswer> fetchLatestByQuestionIdAndUserList(Long questionId, List<Long> userIds);
	
	@Query("select count(s.session.sessionId) from SurveyAnswer s where s.session.sessionId = ?1")	
	public Long countSessionIdOccurences(String session);
	
	@Query("select distinct s.productCategory.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey.id = ?2 and s.productCategory is not null")				
	public List<Long> fetchTakenProdCatsByUserAndSurveyId(User user, Long surveyId);
	
	//@Query("select count(distinct s.productCategory) from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey.id = ?2 and s.productCategory is not null and s.cycle.id = ?3")				
	//public Long coundTakenProdCatsByUserAndSurveyIdAndCycleId(User user, Long surveyId, Long cycleId);

	@Query("select distinct s.productCategory from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey.id = ?2 and s.productCategory is not null and s.cycle.id = ?3")				
	public List<Long> fetchTakenProdCatsByUserAndSurveyIdAndCycleId(User user, Long surveyId, Long cycleId);
	
	@Query("select s.productCategory.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.cycle.id = ?3")	
	public List<Long> fetchTakenProdCatIdsByUserAndCycleId(User user, Survey survey, Long cycleId);		
	
	@Query("select s.productCategory.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.cycle.id is null")	
	public List<Long> fetchTakenProdCatIdsByUser(User user, Survey survey);		

	
}
