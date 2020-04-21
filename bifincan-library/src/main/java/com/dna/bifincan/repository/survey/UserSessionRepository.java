package com.dna.bifincan.repository.survey;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dna.bifincan.model.survey.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
	public UserSession findBySessionId(String sessionId);	
}
