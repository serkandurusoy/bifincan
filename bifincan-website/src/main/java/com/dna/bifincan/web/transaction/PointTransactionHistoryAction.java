package com.dna.bifincan.web.transaction;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.Flash;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.model.dto.GroupedTotalPointsDTO;
import com.dna.bifincan.model.order.PointTransactionAccounting;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.service.PointTransactionAccountingService;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.WebConstants;
import com.dna.bifincan.util.DateUtils;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.spring.ScopeType;

@Named("pointHistoryAction")
@Scope(ScopeType.VIEW)  
public class PointTransactionHistoryAction implements Serializable {
	private static final long serialVersionUID = -2945970416423883826L;
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(PointTransactionHistoryAction.class);	
	
	private List<String> rangeOfDates;
	private List<PointTransactionAccounting> transactions;
	private long totalPoints, balance;
	private GroupedTotalPointsDTO groupedTotals;
	
	@Inject
	private PointTransactionAccountingService pointTransactionAccountingService;	
	@Inject
	private UserService userService;	
	
	public PointTransactionHistoryAction() { }
	
	@PostConstruct
	public void initialize() { 
		loadTransactions(null);
	}	

	public void loadTransactions(AjaxBehaviorEvent e) {
		// 1. find the current user entity from database
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
			
		String username = principal.getName();
		
		User user = userService.findUserByUsername(username);
		
		// 2. find the range of dates
		Date regDate = user.getCreateDate();
  		Calendar regDateCal = Calendar.getInstance();
  		regDateCal.setTime(regDate);
  		
		Date curDate = new Date();
  		Calendar curDateCal = Calendar.getInstance();
  		curDateCal.setTime(curDate);
  		
  		Date tempDate = null;
		List<String> dates = new ArrayList<String>();

  		do {
  			tempDate = curDateCal.getTime();
  			dates.add(DateUtils.formatDateToYYYYMMDD(tempDate));

  			if(curDateCal.get(Calendar.MONTH) == 0) {
  				curDateCal.set(Calendar.YEAR, curDateCal.get(Calendar.YEAR) - 1);
  			}
  			curDateCal.roll(Calendar.MONTH, false);	
 
  		} while( ((regDateCal.get(Calendar.MONTH) <= curDateCal.get(Calendar.MONTH)) && 
  				 (regDateCal.get(Calendar.YEAR) == curDateCal.get(Calendar.YEAR))) 
  				 ||
  		  		 (regDateCal.get(Calendar.YEAR) < curDateCal.get(Calendar.YEAR)));  	
  		
		this.setRangeOfDates(dates); // store the range of dates for rendering

		Flash flash = FacesUtils.getFlash();
		String selectionDate = (String)flash.get(WebConstants.SEL_DATE_KEY); 
		
		if(selectionDate == null) { // init the value
			selectionDate = DateUtils.formatDateToYYYYMMDD(curDate);
			flash.put(WebConstants.SEL_DATE_KEY, selectionDate);
		}
		
		// 3. Fetch the total points grouped by category (for all months)
		this.setBalance(user.getPointsBalance());
		Long sumOfPoints = pointTransactionAccountingService.calculateTotalGainedPoints(username); 
		this.setTotalPoints(sumOfPoints != null ? sumOfPoints : 0L);

		
		
		List<Object> groupedData = pointTransactionAccountingService.calculateGroupedTotalPoints(username);
		if(groupedData != null && !groupedData.isEmpty()) {
			Object[] data = (Object[])groupedData.get(0);

			GroupedTotalPointsDTO group = new GroupedTotalPointsDTO(data[0] != null ? (Long)data[0] : 0, 
					data[1] != null ? (Long)data[1] : 0, 
					data[2] != null ? (Long)data[2] : 0, 
					data[3] != null ? (Long)data[3] : 0, 
					data[4] != null ? (Long)data[4] : 0, 
					data[5] != null ? (Long)data[5] : 0, 
					data[6] != null ? (Long)data[6] : 0, 
					data[7] != null ? (Long)data[7] : 0, 
					data[8] != null ? (Long)data[8] : 0, 
					data[9] != null ? (Long)data[9] : 0, 
					data[10] != null ? (Long)data[10] : 0, 
					data[11] != null ? (Long)data[11] : 0,
					data[12] != null ? (Long)data[12] : 0);
			
			this.setGroupedTotals(group);
		}
	
		calculatePointTotalsPerMonth(username, selectionDate);
		
		// 3. Finally update the user's balance in session to reflect the true one (normally is not needed)
		Map<String, Object> values = FacesUtils.getExternalContext().getSessionMap();
		User userToken = (User)values.get(WebConstants.CURRENT_USER_SESSION_KEY);

		if(userToken != null) {
			userToken.setPointsBalance(user.getPointsBalance());
		}	
	}

	@SuppressWarnings("unused")
	private void calculatePointTotalsPerMonth(String username, String selectionDate) {
		// fetch the point transaction history for a specific date (and for the current user) 
		List<PointTransactionAccounting> results = pointTransactionAccountingService.
				findTransactionsByDate(username, DateUtils.formatYYYYMMDDTokenToDate(selectionDate)); 
		
		if(results != null && !results.isEmpty()) { // if there are results
			// construct the last entry of table about the sums of points
			PointTransactionAccounting totalEntry = new PointTransactionAccounting();
			
			// calculate the sums for the last entry
			int invitationPoints = 0, invitationSuccessPoints = 0, loginPoints = 0, productOrderPoints = 0;
			int productSurveyPoints = 0, quizPoints = 0, ratingPoints = 0, signupPoints = 0, surveyPoints = 0;
			int blogCommentPoints = 0, productCommentPoints = 0, socialPoints = 0, votingPoints = 0;
			
			for(PointTransactionAccounting entry : results) {
				invitationPoints += entry.getInvitationPoints();
				invitationSuccessPoints += entry.getInvitationSuccessPoints();
				loginPoints += entry.getLoginPoints();
				productOrderPoints += entry.getProductOrderPoints();
				productSurveyPoints += entry.getProductSurveyPoints();
				quizPoints += entry.getQuizPoints();
				ratingPoints += entry.getRatingPoints();
				signupPoints += entry.getSignupPoints();	
				surveyPoints += entry.getGenericSurveyPoints();
				blogCommentPoints += entry.getBlogCommentPoints();
				productCommentPoints += entry.getProductCommentPoints();
				socialPoints += entry.getSocialPoints();
				votingPoints += entry.getVotingPoints();
			}
			
			// fill in the record's properties with the individual sums
			totalEntry.setInvitationPoints(invitationPoints);
			totalEntry.setInvitationSuccessPoints(invitationSuccessPoints);
			totalEntry.setLoginPoints(loginPoints);
			totalEntry.setProductOrderPoints(productOrderPoints);
			totalEntry.setProductSurveyPoints(productSurveyPoints);
			totalEntry.setQuizPoints(quizPoints);
			totalEntry.setRatingPoints(ratingPoints);
			totalEntry.setSignupPoints(signupPoints);
			totalEntry.setGenericSurveyPoints(surveyPoints);
			totalEntry.setBlogCommentPoints(blogCommentPoints);
			totalEntry.setProductCommentPoints(productCommentPoints);
			totalEntry.setSocialPoints(socialPoints);
			totalEntry.setVotingPoints(votingPoints);
			
			// add the last entry into the results list
			results.add(totalEntry);
		}
		
		this.setTransactions(results); // store the results for rendering
	}
	
	public String formatDateToMonthAndYear(String source) {
		return DateUtils.formatDateToMonthAndYear(source) ;
	}

	public int extractDateOfMonth(Date source) {
		return DateUtils.extractDateOfMonth(source);
	}
	
	// -- setters & getters -- /
	public List<PointTransactionAccounting> getTransactions() {
		return transactions;
	}

	public List<String> getRangeOfDates() {
		return rangeOfDates;
	}

	public void setRangeOfDates(List<String> rangeOfDates) {
		this.rangeOfDates = rangeOfDates;
	}

	public void setTransactions(List<PointTransactionAccounting> transactions) {
		this.transactions = transactions;
	}

	public long getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(long totalPoints) {
		this.totalPoints = totalPoints;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public GroupedTotalPointsDTO getGroupedTotals() {
		return groupedTotals;
	}

	public void setGroupedTotals(GroupedTotalPointsDTO groupedTotals) {
		this.groupedTotals = groupedTotals;
	}
	
}
