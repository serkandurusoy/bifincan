package com.dna.bifincan.model.quiz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.dna.bifincan.model.BaseEntity;
import javax.persistence.Lob;

@Entity
@Table(name = "quiz_option")
public class QuizOption extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 2497173361572083998L;

	@Column(name = "option_text", nullable = false, length = 512)
	@NotNull
	@NotEmpty
	private String optionText;

	@ManyToOne
	@JoinColumn(name = "quiz_question_id", nullable = false)
	private QuizQuestion quizQuestion;

	@Column(name = "active", nullable = false, columnDefinition = "TINYINT(1) unsigned")
	private boolean active;

	@Column(name = "correct_option", nullable = false, columnDefinition = "TINYINT(1) unsigned")
	private boolean correctOption;

	public QuizOption() {
	}

	public String getOptionText() {
		return optionText;
	}

	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}

	public QuizQuestion getQuizQuestion() {
		return quizQuestion;
	}

	public void setQuizQuestion(QuizQuestion quizQuestion) {
		this.quizQuestion = quizQuestion;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isCorrectOption() {
		return correctOption;
	}

	public void setCorrectOption(boolean correctOption) {
		this.correctOption = correctOption;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + (correctOption ? 1231 : 1237);
		result = prime * result
				+ ((optionText == null) ? 0 : optionText.hashCode());
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
		QuizOption other = (QuizOption) obj;
		if (active != other.active)
			return false;
		if (correctOption != other.correctOption)
			return false;
		if (optionText == null) {
			if (other.optionText != null)
				return false;
		} else if (!optionText.equals(other.optionText))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QuizOption [optionText=" + optionText + ", quizQuestion=" + quizQuestion.getId() + ", active=" + active + ", correctOption="
				+ correctOption + "]";
	}

}
