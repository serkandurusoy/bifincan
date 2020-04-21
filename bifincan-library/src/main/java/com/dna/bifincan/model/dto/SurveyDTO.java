package com.dna.bifincan.model.dto;

import java.io.Serializable;
import java.util.Date;

import com.dna.bifincan.model.type.SurveyType;

public class SurveyDTO implements Serializable {
	private static final long serialVersionUID = 3602950575180947509L;
	
	private Long id;
	private String title;
	private boolean active;
	private Date createDate;
	private SurveyType type;
	private boolean prioritized;
	
	public SurveyDTO() { }
	
	public SurveyDTO(Long id, String title, boolean active, Date createDate, SurveyType type) {
		super();
		this.id = id;
		this.title = title;
		this.active = active;
		this.createDate = createDate;
		this.type = type;
	}

	public SurveyDTO(Long id, String title, boolean active, Date createDate, SurveyType type, boolean prioritized) {
		super();
		this.id = id;
		this.title = title;
		this.active = active;
		this.createDate = createDate;
		this.type = type;
		this.prioritized = prioritized;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public SurveyType getType() {
		return type;
	}

	public void setType(SurveyType type) {
		this.type = type;
	}

	public boolean isPrioritized() {
		return prioritized;
	}

	public void setPrioritized(boolean prioritized) {
		this.prioritized = prioritized;
	}
	
}
