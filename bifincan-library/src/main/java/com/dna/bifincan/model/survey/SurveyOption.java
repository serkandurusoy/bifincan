package com.dna.bifincan.model.survey;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.dna.bifincan.model.BaseEntity;

@Entity
@Table(name = "survey_option")
public class SurveyOption extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @NotEmpty    
    @Column(name = "title", nullable = false, length = 250)
    private String title;

    @Column(name = "active", nullable = false, columnDefinition = "TINYINT(1) unsigned")
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private SurveyQuestion question;

    public SurveyOption() { }
    

    public SurveyOption(Long id, String title, boolean active, SurveyQuestion question) {
		super();
		this.setId(id);
		this.title = title;
		this.active = active;
		this.question = question;
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
	public SurveyQuestion getQuestion() {
		return question;
	}

	public void setQuestion(SurveyQuestion question) {
		this.question = question;
	}

	@Override
    public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((question == null) ? 0 : question.hashCode());
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
	SurveyOption other = (SurveyOption) obj;
	if (question == null) {
	    if (other.question != null)
		return false;
	} else if (!question.equals(other.question))
	    return false;
	if (title == null) {
	    if (other.title != null)
		return false;
	} else if (!title.equals(other.title))
	    return false;
	return true;
    }

    @Override
    public String toString() {
    	return "SurveyOption [title=" + title + "]";
    }
}