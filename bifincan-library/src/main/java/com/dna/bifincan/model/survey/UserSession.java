package com.dna.bifincan.model.survey;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dna.bifincan.model.BaseEntity;

@Entity
@Table(name = "user_session")	
public class UserSession extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -3727964024110964126L;

	@Column(name = "session_id", nullable = false)
	private String sessionId;
	
	public UserSession() { }
	
	public UserSession(String sessionId) {
		super();
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
