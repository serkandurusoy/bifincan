package com.dna.bifincan.web.order;

import java.io.Serializable;
import java.security.Principal;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.model.order.Order;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.OrderService;
import com.dna.bifincan.service.ProductService;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.WebConstants;
import com.dna.bifincan.util.spring.ScopeType;

@Named("orderStatusAction")
@Scope(ScopeType.REQUEST)  
public class OrderStatusAction implements Serializable {
	private static final long serialVersionUID = 5085382355568366448L;

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(OrderStatusAction.class);	
	
    private Date lastOrderReceivedDate;  
    private Date lastOrderSentDate;     
    private long remainingDaysForNextOrder;  
    private long remainingDaysForSurveys;      
    private boolean lastOrderSurveyable;  
    private String message;
    private String cartDisplay;
    
	@Inject
	private UserService userService;
	@Inject
	protected ProductService productService;	
	@Inject
	protected OrderService orderService;	
	@Inject 
	protected ConfigurationService configurationService;
	
	public OrderStatusAction() { }
	
	@PostConstruct
	public void initialize() { 
		loadStatusData();
	}	

	/**
	 * This method is used to fetch the current information about user's ordering status.
	 * Here are the existing cases:			
	 * A. When the order is being prepared
 	 *    1) when a user makes an order, the message displays "your last order is being prepared"
	 * B. When the order is in delivery
	 *	  2) when the sentTime is not null, the message displays "your last order is in delivey, 
	 *			please conifrm your delivery when you receive it"
	 *			when the order is received by user (confirmed)
	 *	  3) when receivedTime is not null and remaining days until surveys > 0 the message displays 
	 *			"you'll be presented with your product surveys in XX days"
	 *			when 'timeRequiredBeforeProductSurveys' passed and surveys begin but not completed
	 *	  4) when remaining days until surveys <= 0 and all surveys not complete the message displays 
	 *			"you need to finish product surveys"
	 *			when surveys are completed, but there is still some days of 'daysRequireBeforeNextOrder' left
	 *	  5) when all surveys ARE complete, and if there are still remaining days until next order > 0 
	 *			message displays "you have to wait another XX days until you can place your new order"
	 * C. All checks passed
	 *	 6) when all surveys are complete and rmaining days until next order <=0 , no more message, 
	 *			user can place order
	 */
	public void loadStatusData() {
		// find the current user entity from database
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		
		if(principal != null) {
			String username = principal.getName();
			
			User user = userService.findUserByUsername(username);
			
			// fetch the data and set the flags
			Order order = orderService.getLastOrder(user);
			
			if(order != null) {
				setLastOrderReceivedDate(order.getReceivedTime());
				setLastOrderSentDate(order.getSentTime());
			}
			
			setRemainingDaysForNextOrder(orderService.getRemainingDaysForNextAction(user, 
					ConfigurationType.DAYS_REQUIRED_BEFORE_NEXT_ORDER));
			setRemainingDaysForSurveys(orderService.getRemainingDaysForNextAction(user, 
					ConfigurationType.TIME_REQUIRED_BEFORE_PRODUCT_SURVEYS));
			setLastOrderSurveyable(orderService.isLastOrderSurveyable(user));
			
			// set up the notification message
			if(getLastOrderReceivedDate() == null && getLastOrderSentDate() == null) { // #1
				message = FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						"order.isBeingPrepared");
                                cartDisplay = "preparing";
			} else if(getLastOrderReceivedDate() == null && getLastOrderSentDate() != null) { // #2
				message = FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						"order.isInDelivery");
                                cartDisplay = "indelivery";
			} else if(getLastOrderReceivedDate() != null && getRemainingDaysForSurveys() > 0) { // #3
				message = FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						"order.productSurveysWillStart", new Object[] { getRemainingDaysForSurveys() } );				
                                cartDisplay = "surveywaiting";
			} else if(getRemainingDaysForSurveys() == 0 && isLastOrderSurveyable() == true) { // #4
				message = FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						"order.surveysMustBeCompleted");
                                cartDisplay = "surveyongoing";
			} else if(isLastOrderSurveyable() == false && getRemainingDaysForNextOrder() > 0) { // #5
				message = FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						"order.shouldWait", new Object[] { getRemainingDaysForNextOrder() } );				
                                cartDisplay = "shouldwait";
			} else {  // #6
				message = null;
                                cartDisplay = null;
				//.. it's okay; user can place orders..
			}
		}	
	}
	
	// --- other helper methods --- //

	
	// -- setters & getters -- /
	public Date getLastOrderReceivedDate() {
		return lastOrderReceivedDate;
	}

	public void setLastOrderReceivedDate(Date lastOrderReceivedDate) {
		this.lastOrderReceivedDate = lastOrderReceivedDate;
	}

	public Date getLastOrderSentDate() {
		return lastOrderSentDate;
	}

	public void setLastOrderSentDate(Date lastOrderSentDate) {
		this.lastOrderSentDate = lastOrderSentDate;
	}

	public long getRemainingDaysForNextOrder() {
		return remainingDaysForNextOrder;
	}

	public void setRemainingDaysForNextOrder(long remainingDaysForNextOrder) {
		this.remainingDaysForNextOrder = remainingDaysForNextOrder;
	}

	public long getRemainingDaysForSurveys() {
		return remainingDaysForSurveys;
	}

	public void setRemainingDaysForSurveys(long remainingDaysForSurveys) {
		this.remainingDaysForSurveys = remainingDaysForSurveys;
	}

	public boolean isLastOrderSurveyable() {
		return lastOrderSurveyable;
	}

	public void setLastOrderSurveyable(boolean lastOrderSurveyable) {
		this.lastOrderSurveyable = lastOrderSurveyable;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCartDisplay() {
		return cartDisplay;
	}

	public void setCartDisplay(String cartDisplay) {
		this.cartDisplay = cartDisplay;
	}
}
