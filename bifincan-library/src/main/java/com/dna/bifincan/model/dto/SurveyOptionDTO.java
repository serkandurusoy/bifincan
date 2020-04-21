package com.dna.bifincan.model.dto;

import java.io.Serializable;

public class SurveyOptionDTO implements Serializable {
	private static final long serialVersionUID = -447485753507016841L;
	
	private Long id;
	private String title;
	private boolean active;
	
	public SurveyOptionDTO() { }

	public SurveyOptionDTO(Long id, String title, boolean active) {
		super();
		this.id = id;
		this.title = title;
		this.active = active;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
			
}
