package com.dna.bifincan.model.survey;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;

import com.dna.bifincan.model.BaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "survey")
public class Survey extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 199880081408379322L;

    @NotNull
    @NotEmpty	
    @Column(name = "title", nullable = false, length = 250)	
	private String title;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "survey")
    @Fetch(FetchMode.SUBSELECT)   
	private List<SurveyQuestion> surveyQuestions = new ArrayList<SurveyQuestion>();
	
    @Column(name = "active", nullable = false, columnDefinition = "TINYINT(1) unsigned")	
	private boolean active;
	
    @Column(name = "type", updatable=false, insertable=false)
    private String type;
    
    @Temporal(TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;
    
	@Column(name = "prioritized", nullable = false, columnDefinition = "TINYINT(1) unsigned")
	private boolean prioritized;
	
	public Survey() { }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<SurveyQuestion> getSurveyQuestions() {
		return surveyQuestions;
	}

	public void setSurveyQuestions(List<SurveyQuestion> surveyQuestions) {
		this.surveyQuestions = surveyQuestions;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public boolean isPrioritized() {
		return prioritized;
	}

	public void setPrioritized(boolean prioritized) {
		this.prioritized = prioritized;
	}

	public void addSurveyQuestion(SurveyQuestion surveyQuestion) {
		if (!this.surveyQuestions.contains(surveyQuestion)) {
		    this.surveyQuestions.add(surveyQuestion);
		    surveyQuestion.setSurvey(this);
		}
    }

    public void removeSurveyQuestion(SurveyQuestion surveyQuestion) {
		if (this.surveyQuestions.contains(surveyQuestion)) {
		    this.surveyQuestions.remove(surveyQuestion);
		    surveyQuestion.setSurvey(null);
		}
    }	
        
}
