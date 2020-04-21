package com.dna.bifincan.model.dto;

import java.io.Serializable;

public class SurveyItemNodeDTO implements Serializable {
	private static final long serialVersionUID = -9191590878073202201L;

	private Long id;
	private Long parentId;	
	private String title;
	private boolean active;
	private boolean optionRandom;	
	private String nodeType;
	
	// related only to the question entity
	private Integer position;
	private Boolean selectMultiple;
	
	public SurveyItemNodeDTO() { }

	public SurveyItemNodeDTO(Long id, String title, boolean active, String nodeType) {
		super();
		this.id = id;
		this.title = title;
		this.active = active;
		this.nodeType = nodeType;
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

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Boolean getSelectMultiple() {
		return selectMultiple;
	}

	public void setSelectMultiple(Boolean selectMultiple) {
		this.selectMultiple = selectMultiple;
	}

	public boolean getOptionRandom() {
		return optionRandom;
	}

	public void setOptionRandom(boolean optionRandom) {
		this.optionRandom = optionRandom;
	}
		
}
