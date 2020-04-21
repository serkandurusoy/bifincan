package com.dna.bifincan.repository.survey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dna.bifincan.model.survey.Survey;
import com.dna.bifincan.model.survey.SurveyCycle;
import com.dna.bifincan.model.survey.SurveyInstance;
import com.dna.bifincan.model.user.User;

public interface SurveyCycleRepository extends JpaRepository<SurveyCycle, Long> {
	
	@Query("select max(s.cycle) from SurveyAnswer s where s.user = ?1")
	public SurveyCycle findLatestByUser(User user);
}
