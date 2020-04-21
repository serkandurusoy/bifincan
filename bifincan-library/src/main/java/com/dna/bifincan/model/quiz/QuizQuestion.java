package com.dna.bifincan.model.quiz;

import java.io.Serializable;
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

import org.hibernate.validator.constraints.NotEmpty;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.products.Product;
import javax.persistence.Lob;

@Entity
@Table(name = "quiz_question")
public class QuizQuestion extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3333590384145452287L;

	@Column(name = "question", nullable = false, length = 512)
	@NotNull
	@NotEmpty
	private String question;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(name = "points", nullable = false)
	private int points;

	@Column(name = "active", nullable = false, columnDefinition = "TINYINT(1) unsigned")
	private boolean active;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "quizQuestion")
	private List<QuizOption> quizOptions;

	public QuizQuestion() {
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<QuizOption> getQuizOptions() {
		return quizOptions;
	}

	public void setQuizOptions(List<QuizOption> quizOptions) {
		this.quizOptions = quizOptions;
	}

	@Override
	public String toString() {
		return "QuizQuestion [question=" + question + ", product=" + product.getId() + ", points=" + points + ", active=" + active + "]";
	}

}
