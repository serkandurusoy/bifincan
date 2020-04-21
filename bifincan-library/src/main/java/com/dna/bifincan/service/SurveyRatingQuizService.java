package com.dna.bifincan.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.dna.bifincan.model.dto.ProductRating;
import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.model.order.SurveyProductStatusForOrderDetails;
import com.dna.bifincan.model.quiz.QuizQuestion;
import com.dna.bifincan.model.rating.Rating;
import com.dna.bifincan.model.survey.SurveyProduct;
import com.dna.bifincan.model.survey.SurveyQuestion;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.repository.order.SurveyProductStatusForOrderDetailsRepository;

@Service
@Named("surveyRatingQuizService")
public class SurveyRatingQuizService
{

	@Inject
	private SurveyService surveyService;

	@Inject
	private RatingService ratingService;

	@Inject
	private QuizService quizService;

	@Inject
	private OrderService orderService;

	@Inject
	private SurveyProductStatusForOrderDetailsRepository surveyProductStatusForOrderDetailsRepository;

	@Inject
	private UserService userService;
	
	// Steps
	// 1. Complete previous started survey
	// 2. Fetch Product IDs from OrderService.class method getOrderItemsForSurveyableOrder(User user) which returns OrderDetails
	// 3. If Product IDs are not null, fetch the first product ID
	// 4. Use the productID to perform the Product Survey(s) - (if exists)
	// 5. Use the productID to perform the rating of product
	// 6. Use the productID to perform the quiz(s) - (if exists)
	// 7. mark the productID used using OrderService.class method disableOrderDetailSurveyableFlag(OrderDetails orderDetails)
	// 8. Again go to step 2
	// 9. Else of step 3, fetch random survey.

	public Object next(User _user)
	{

		// 1. Complete previous started survey
		SurveyQuestion surveyQuestion = this.surveyService.getLastStartedSurveyQuestionIfSurveyNotCompleted(_user);
		if (surveyQuestion != null)
		{
			return surveyQuestion; // return surveyQuestion of last started survey.
		}
		// 2. Fetch Product IDs from OrderService.class method getOrderItemsForSurveyableOrder(User user) which returns OrderDetails
		else
		{
			List<OrderDetails> orderDetails = this.orderService.getOrderItemsForSurveyableOrder(_user);
			
			// 3. If Product IDs are not null, fetch them and start the product survey, rating and quiz
			if (orderDetails != null && orderDetails.size() > 0)
			{
				for (OrderDetails orderDetail : orderDetails)
				{
					Object obj = getNextSurveyProductOrRatingOrQuiz(orderDetail, _user);
					if (obj == null)
					{
						// Mark Order Detail used
						this.orderService.disableOrderDetailSurveyableFlag(orderDetail); 
					}
					else
					{
						return obj;
					}
				}
			}
			
			// 9. Else of step 3, fetch random survey.
			try {
			surveyQuestion = this.surveyService.getNextQuestionData(_user, null).getSurveyQuestion();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			return surveyQuestion;
		}
	}

	public long[] findTotalOpenSurveyItemsByUser(String username) {
		long totalOpenSurveys = 0L, totalOpenQuizes = 0L, totalOpenRatings = 0L;
		
		User user = userService.findUserByUsername(username);
		List<Long> orderItemIds = this.orderService.getOrderItemIdsForSurveyableOrder(user);
		
		if (orderItemIds != null && !orderItemIds.isEmpty()) {
			totalOpenSurveys = orderItemIds.size() - surveyService.
					countClosedSurveysByOrderDetailsAndUserId(orderItemIds, user.getId());
			totalOpenRatings = orderItemIds.size() - ratingService.
					countRatingsByOrderDetailsAndUserId(orderItemIds, user.getId());
			totalOpenQuizes = orderItemIds.size() - quizService.
					countQuizAnswersByOrderDetailsAndUserId(orderItemIds, user.getId());			
		}
		
		return new long[] { totalOpenSurveys, totalOpenRatings, totalOpenQuizes };
	}
	
	private SurveyQuestion startSurveyProducts(List<SurveyProduct> surveyProducts, User _user, OrderDetails orderDetail)
	{
		SurveyQuestion surveyQuestion = null;
		if (surveyProducts != null)
			for (SurveyProduct surveyProduct : surveyProducts)
			{
				surveyQuestion = this.surveyService.getNextSurveyQuestion(surveyProduct, _user, false);

				SurveyProductStatusForOrderDetails surveyProductStatusForOrderDetails = new SurveyProductStatusForOrderDetails();
				surveyProductStatusForOrderDetails.setOrderDetail(orderDetail);
				surveyProductStatusForOrderDetails.setSurvey(surveyProduct);
				surveyProductStatusForOrderDetails.setUser(_user);
				surveyProductStatusForOrderDetails.setVersion(1L);

				if (surveyQuestion != null)
				{
					surveyProductStatusForOrderDetails.setSurveyCompleted(false);
					this.surveyProductStatusForOrderDetailsRepository.save(surveyProductStatusForOrderDetails);
					return surveyQuestion;
				}
				else
				{
					surveyProductStatusForOrderDetails.setSurveyCompleted(true);
					this.surveyProductStatusForOrderDetailsRepository.save(surveyProductStatusForOrderDetails);
				}
			}

		return null;
	}

	private Object getNextSurveyProductOrRatingOrQuiz(OrderDetails orderDetail, User _user)
	{
		SurveyQuestion surveyQuestion = null;

		// 4. Use the productID to perform the Product
		List<SurveyProduct> surveyProducts = this.surveyService.getSurveyProduct(_user, orderDetail.getId());

		// We must need a status table in which we will persist which Product Surveys are utilized by the user.
		// Structure of that table will contain (SurveyID, OrderDetailID, UserID, Completed (flag)). SurveyProductStatusForOrderDetails.class
		surveyQuestion = startSurveyProducts(surveyProducts, _user, orderDetail);

		if (surveyQuestion != null) // product survey exist
		{
			// Start the survey;
			return surveyQuestion;
		}
		else
		{
			// 5. Use the productID to perform the rating of product
			Rating rating = ratingService.findByOrderDetailAndUser(orderDetail, _user);
			if (rating == null)
			{
				// return rating
				ProductRating productRating = new ProductRating();
				productRating.setOrderDetail(orderDetail);
				return productRating;
			}
			else
			{
				// 6. Use the productID to perform the quiz(s) - (if exists)
				List<QuizQuestion> quizQuestions = this.quizService.findAllQuizQuestionByProduct(orderDetail);
				if (quizQuestions != null && quizQuestions.size() > 0)
				{
					return quizQuestions.get(0);
				}
				else
				{
					return null;
				}
			}
		}

	}
}
