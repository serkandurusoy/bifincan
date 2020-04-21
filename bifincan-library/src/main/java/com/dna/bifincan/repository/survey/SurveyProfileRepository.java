package com.dna.bifincan.repository.survey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dna.bifincan.model.survey.SurveyProfile;

public interface SurveyProfileRepository extends JpaRepository<SurveyProfile, Long> {
	@Query("select s.id from SurveyProfile s where s.active = true and s.prioritized = ?1")
	public List<Long> findActiveByPriority(boolean priority);
	
}
