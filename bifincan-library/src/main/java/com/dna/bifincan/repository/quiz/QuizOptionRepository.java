package com.dna.bifincan.repository.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.quiz.QuizOption;

public interface QuizOptionRepository extends JpaRepository<QuizOption, Long> {

	@Query("select count(q) from QuizOption q where q.quizQuestion.id = :quizQuestionId and q.correctOption = true")
	public Long countByCorrectFlag(@Param("quizQuestionId")Long quizQuestionId);
	
	@Query("select count(q) from QuizOption q where q.quizQuestion.id = ?1 and q.correctOption = true and q.id <> ?2")
	public Long countByCorrectFlagExcludingCurrent(Long quizQuestionId, Long quizOptionId);	
	

}
