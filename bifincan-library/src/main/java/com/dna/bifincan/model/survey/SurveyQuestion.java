package com.dna.bifincan.model.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;

import com.dna.bifincan.model.BaseEntity;

@Entity
@Table(name = "survey_question")
public class SurveyQuestion extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 668490854476974045L;

	@Column(name = "title", nullable = false, length = 512)
	@NotNull
	@NotEmpty
	private String title;

	@Column(name = "select_multiple", nullable = false, columnDefinition = "TINYINT(1) unsigned")
	private boolean selectMultiple = false;

	@Column(name = "active", nullable = false, columnDefinition = "TINYINT(1) unsigned")
	private boolean active = true;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "question", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<SurveyOption> options = new ArrayList<SurveyOption>();

	@Column(name = "position", nullable = false, columnDefinition = "INT unsigned")
	private int position;

	@ManyToOne
	@JoinColumn(name = "survey_id", nullable = false)
	private Survey survey;

	@Column(name = "random_option", nullable = false, columnDefinition = "TINYINT(1) unsigned")
	private boolean optionRandom = false;
	
	public SurveyQuestion() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isSelectMultiple() {
		return selectMultiple;
	}

	public void setSelectMultiple(boolean selectMultiple) {
		this.selectMultiple = selectMultiple;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<SurveyOption> getOptions() {
		return options;
	}

	public void setOptions(List<SurveyOption> options) {
		this.options = options;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
	
	public boolean isOptionRandom() {
		return optionRandom;
	}

	public void setOptionRandom(boolean optionRandom) {
		this.optionRandom = optionRandom;
	}

	public void addOption(SurveyOption _option) {
		if (!this.options.contains(_option)) {
			this.options.add(_option);
			_option.setQuestion(this);
		}
	}

	public void removeOption(SurveyOption _option) {
		if (this.options.contains(_option)) {
			this.options.remove(_option);
			_option.setQuestion(null);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + position;
		result = prime * result + (selectMultiple ? 1231 : 1237);
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SurveyQuestion other = (SurveyQuestion) obj;
		if (active != other.active)
			return false;
		if (position != other.position)
			return false;
		if (selectMultiple != other.selectMultiple)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}