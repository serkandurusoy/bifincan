package com.dna.bifincan.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.quiz.QuizAnswer;
import com.dna.bifincan.model.quiz.QuizOption;
import com.dna.bifincan.model.quiz.QuizQuestion;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.repository.quiz.QuizAnswerRepository;
import com.dna.bifincan.repository.quiz.QuizOptionRepository;
import com.dna.bifincan.repository.quiz.QuizQuestionRepository;

@Service
@Named("quizService")
public class QuizService {
    @SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(QuizService.class);

    @Inject
    private QuizQuestionRepository quizQuestionRepository;
    @Inject
    private QuizAnswerRepository quizAnswerRepository;
    @Inject
    private QuizOptionRepository quizOptionRepository;

    public QuizService() { }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<QuizQuestion> findAllQuizQuestionByProduct(OrderDetails orderDetail) {
        Product product = orderDetail.getProduct();

        return this.quizQuestionRepository.findByProduct(product, orderDetail);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public QuizQuestion findQuizQuestion(Long questionId) {
        return this.quizQuestionRepository.findOne(questionId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public QuizOption findQuizOption(Long optionId) {
        return this.quizOptionRepository.findOne(optionId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveQuizAnswer(QuizAnswer quizAnswer) {
        this.quizAnswerRepository.save(quizAnswer);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public long countQuizAnswersByOrderDetailsAndUserId(List<Long> orderItemIds, Long userId) {
        return quizAnswerRepository.countQuizAnswersByOrderDetailsAndUserId(orderItemIds, userId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public QuizAnswer findAnswerByOrderDetailAndUser(OrderDetails orderDetails, User user) {
        return quizAnswerRepository.findAnswerByOrderDetailAndUser(orderDetails, user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean saveQuizQuestion(QuizQuestion question, Product product) {
        if (question.getId() == null || question.getId() == 0) {
            Long total = quizQuestionRepository.countByProduct(product.getId());
            if (total == 0) {
                question.setProduct(product);
                product.setQuizQuestion(question);

                this.quizQuestionRepository.save(question);
            } else {
                return false;
            }
        } else {
            QuizQuestion tempQuizQuestion = this.quizQuestionRepository.findOne(question.getId());

            tempQuizQuestion.setPoints(question.getPoints());
            tempQuizQuestion.setActive(question.isActive());
            tempQuizQuestion.setQuestion(question.getQuestion());

            this.quizQuestionRepository.save(tempQuizQuestion);
        }
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteQuizQuestion(QuizQuestion quizQuestion) {
        Long total = quizAnswerRepository.countByQuizQuestion(quizQuestion.getId());
        if (total == 0) {
            // get a fresh copy of the entity
            QuizQuestion tempQuizQuestion = this.quizQuestionRepository.findOne(quizQuestion.getId());
            tempQuizQuestion.getProduct().setQuizQuestion(null);

            this.quizQuestionRepository.delete(tempQuizQuestion);
            return true;
        } else {
            return false;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean saveQuizOption(QuizOption quizOption, QuizQuestion quizQuestion) {

        QuizQuestion tempQuizQuestion = this.quizQuestionRepository.findOne(quizQuestion.getId());

        if (quizOption.getId() == null || quizOption.getId() == 0) {
            if (quizOption.isCorrectOption()) {
                Long total = quizOptionRepository.countByCorrectFlag(quizQuestion.getId());
                if (total >= 1) {
                    return false;
                }
            }

            quizOption.setQuizQuestion(tempQuizQuestion);
            quizOptionRepository.save(quizOption);

            List<QuizOption> options = tempQuizQuestion.getQuizOptions();
            if (options == null) {
                options = new ArrayList<QuizOption>();
            }
            options.add(quizOption);
            tempQuizQuestion.setQuizOptions(options);

            quizQuestion.setQuizOptions(options);
        } else {
            QuizOption tempQuizOption = quizOptionRepository.findOne(quizOption.getId());
            if (quizOption.isCorrectOption()) {
                Long total = quizOptionRepository.countByCorrectFlagExcludingCurrent(quizQuestion.getId(),
                        quizOption.getId());
                if (total >= 1) {
                    quizOption.setOptionText(tempQuizOption.getOptionText());
                    quizOption.setCorrectOption(tempQuizOption.isCorrectOption());
                    quizOption.setActive(tempQuizOption.isActive());

                    return false;
                }
            }

            tempQuizOption.setOptionText(quizOption.getOptionText());
            tempQuizOption.setCorrectOption(quizOption.isCorrectOption());
            tempQuizOption.setActive(quizOption.isActive());

            quizOptionRepository.save(tempQuizOption); // save the modifications			
        }

        return true;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteQuizOption(QuizOption quizOption, QuizQuestion quizQuestion) {

        Long total = quizAnswerRepository.countByQuizOption(quizOption.getId());
        if (total == 0) {
            QuizOption tempQuizOption = this.quizOptionRepository.findOne(quizOption.getId());
            QuizQuestion tempQuizQuestion = this.quizQuestionRepository.findOne(quizQuestion.getId());

            quizQuestion.getQuizOptions().remove(quizOption);
            tempQuizQuestion.getQuizOptions().remove(tempQuizOption);

            this.quizOptionRepository.delete(tempQuizOption);
            return true;
        } else {
            return false;
        }
    }

    public long noOfQuizAnswers() {
        return this.quizAnswerRepository.count();
    }
}
