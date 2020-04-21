package com.dna.bifincan.model.survey;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.type.SurveyQuestionCriteriaType;

@Entity
@Table(name = "survey_question_criteria")
@NamedQueries({
	@NamedQuery(name="SurveyQuestionCriteria.findByQuestionIdAndAnswerOptionId", 
			query = "select s from SurveyQuestionCriteria s where s.question.id = :questionId and s.option.id = :optionId")	
})
public class SurveyQuestionCriteria extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 22485748733084103L;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private SurveyQuestion question;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private SurveyOption option;

    @Column(name = "rule", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private SurveyQuestionCriteriaType rule;

    public SurveyQuestionCriteria() { }
    
    public SurveyQuestion getQuestion() {
		return question;
	}

	public void setQuestion(SurveyQuestion question) {
		this.question = question;
	}

	public SurveyOption getOption() {
		return option;
	}

	public void setOption(SurveyOption option) {
		this.option = option;
	}

	public SurveyQuestionCriteriaType getRule() {
		return rule;
	}

	public void setRule(SurveyQuestionCriteriaType rule) {
		this.rule = rule;
	}

	@Override
    public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((option == null) ? 0 : option.hashCode());
		result = prime * result + ((question == null) ? 0 : question.hashCode());
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
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
		SurveyQuestionCriteria other = (SurveyQuestionCriteria) obj;
		if (option == null) {
		    if (other.option != null)
			return false;
		} else if (!option.equals(other.option))
		    return false;
		if (question == null) {
		    if (other.question != null)
			return false;
		} else if (!question.equals(other.question))
		    return false;
		if (rule != other.rule)
		    return false;
		return true;
    }

}