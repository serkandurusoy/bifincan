package com.dna.bifincan.web.order;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.dna.bifincan.exception.OrderProcessException;
import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.dto.OrderDTO;
import com.dna.bifincan.model.dto.OrderItemDTO;
import com.dna.bifincan.model.order.Order;
import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.type.OrderDetailsType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.MailService;
import com.dna.bifincan.service.OrderService;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.DateUtils;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.common.util.RandomStringGenerator;
import com.dna.bifincan.util.WebConstants;
import com.dna.bifincan.util.spring.ScopeType;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named("orderHistoryAction")
@Scope(ScopeType.VIEW)  
public class OrderHistoryAction implements Serializable {
	private static final long serialVersionUID = 5391875341162690916L;
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(OrderHistoryAction.class);	
	
	private List<OrderDTO> orders;
	private List<OrderItemDTO> orderItems;
	private OrderDTO currentOrder;
	
	@Inject
	private OrderService orderService;	
	@Inject
	RandomStringGenerator passwordGenerator;
	@Inject
	PasswordEncoder passwordEncoder;
	@Inject
	private MailService mailService;
	@Inject
	private UserService userService;
	@Inject
	private ConfigurationService configurationService;

        public String getBaseUrlWithPath() {
            String applicationCanonicalURL = "https://www.bifincan.com";
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            String ctxPath = context.getRequestContextPath();
            Configuration canonicalUrlConfig = configurationService.find((ConfigurationType.
                            APPLICATION_CANONICAL_URL.getKey()));
            if(canonicalUrlConfig != null) applicationCanonicalURL = canonicalUrlConfig.getValue();
            String baseUrlWithPath = applicationCanonicalURL + ctxPath;
            return baseUrlWithPath;
        }

        public String getRemainingDaysForSurvey() {
            String remainingDaysForSurvey = "0";
            Configuration remainingDaysForSurveyConfig = configurationService.find((ConfigurationType.
                            TIME_REQUIRED_BEFORE_PRODUCT_SURVEYS.getKey()));
            if(remainingDaysForSurveyConfig != null) remainingDaysForSurvey = remainingDaysForSurveyConfig.getValue();
            return remainingDaysForSurvey;
        }

        public String getRemainingDaysForNextOrder() {
            String remainingDaysForNextOrder = "0";
            Configuration remainingDaysForNextOrderConfig = configurationService.find((ConfigurationType.
                            DAYS_REQUIRED_BEFORE_NEXT_ORDER.getKey()));
            if(remainingDaysForNextOrderConfig != null) remainingDaysForNextOrder = remainingDaysForNextOrderConfig.getValue();
            return remainingDaysForNextOrder;
        }

        public String getRemainingDaysForNextOrderSameProduct() {
            String remainingDaysForNextOrderSameProduct = "0";
            Configuration remainingDaysForNextOrderSameProductConfig = configurationService.find((ConfigurationType.
                            DAYS_REQUIRED_BEFORE_PRODUCT_IS_ORDERABLE.getKey()));
            if(remainingDaysForNextOrderSameProductConfig != null) remainingDaysForNextOrderSameProduct = remainingDaysForNextOrderSameProductConfig.getValue();
            return remainingDaysForNextOrderSameProduct;
        }
	
	public OrderHistoryAction() { }
	
	@PostConstruct
	public void initialize() { 
		loadOrders();
	}	

	/**
	 * This method is used to fetch the current user's orders in a reverse chronological order.
	 */
	public void loadOrders() {

		// 1. find the current user entity from database
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		String username = principal.getName();

		// 2. load the records for this user
		setOrders(null);  // reset first the list of orders
		
		List<Order> orderRecords = orderService.getUserOrders(username); // fetch them
		if(orderRecords != null && !orderRecords.isEmpty()) {
			this.orders = new ArrayList<OrderDTO>();
			Long lastOrderId = null;
			for(Order order : orderRecords) { // iterate through the list to calculate the sums and set-up the DTOs				
				OrderDTO entry = new OrderDTO();
				
				entry.setId(order.getId());
				entry.setPlacedTime(order.getPlacedTime());
				entry.setReceivedTime(order.getReceivedTime());
				entry.setSentTime(order.getSentTime());
				
				int sum = 0;
				List<OrderDetails> orderItems = order.getListOfOrderDetails();
				for(OrderDetails item : orderItems) {
					sum += (item.getType() == OrderDetailsType.ORDERABLE_PRODUCT) ? 
							item.getProduct().getPricePoints() : 0;
				}

				entry.setTotalPoints(sum);
				this.orders.add(entry);
				
				if(lastOrderId == null) {
					lastOrderId = order.getId();
				}				
			}
			// load the last order's items for rendering (default behaviour)
			fetchOrderItemsFromRepository(lastOrderId);
		}

	}

	/**
	 * This AJAX listener is called by the UI components to trigger fetching order items.
	 * It is relied on the current order selection.
	 * @param e the AjaxBehaviorEvent object being passed in by the JSF framework
	 */
	public void loadOrderItems(AjaxBehaviorEvent e) {
		Flash flash = FacesUtils.getFlash();
		String sOrderId = (String)flash.get(WebConstants.ORDER_ID_PARAM);
		
		if(sOrderId != null) {
			Long orderId = Long.valueOf(sOrderId);
			fetchOrderItemsFromRepository(orderId);
		}
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void validateConfirmationCode(AjaxBehaviorEvent e) {
		Flash flash = FacesUtils.getFlash();
		String confirmationCode = (String)flash.get(WebConstants.CONFIRMATION_CODE_PARAM);
		
		String orderConfirmationCode =  passwordEncoder.encodePassword(
				(String.valueOf(currentOrder.getPlacedTime().getTime() + currentOrder.getId())), null);
		String extractedCode = orderConfirmationCode.substring(0, 8);
		
		if(orderConfirmationCode.substring(0, 8).equals(confirmationCode.trim())) { // if the user types the correct code
			try { 
				// update the receival date
				this.currentOrder.setReceivedTime(orderService.
						updateOrderReceivedDate(currentOrder.getId()));

				// update session's info about 'last order received date'
				Map<String, Object> sessionAttrs = FacesUtils.getExternalContext().getSessionMap();	
				User user = (User)sessionAttrs.get(WebConstants.CURRENT_USER_SESSION_KEY);
			
				// send an email
				Map model = new HashMap();
				model.put("baseurl", getBaseUrlWithPath());
				model.put("userfirstname", user.getFirstName());
				model.put("remainingdaysforsurvey", getRemainingDaysForSurvey());
				model.put("remainingdaysfornextorder", getRemainingDaysForNextOrder());
				model.put("remainingdaysfornextordersameproduct", getRemainingDaysForNextOrderSameProduct());
				// ... add here more attributes
				
				try {
					// TODO: enable this line
					mailService.sendEmail(user.getEmail(), "Hediyenin eline ulaştığına sevindim", "order-received", model);
				} catch (Exception ex) { 
					ex.printStackTrace();
				}
				
				// refresh the orders list
				loadOrders(); 
				
				// display a success message
				FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						WebConstants.MSG_CORRECT_CONFIRMATION_CODE_KEY));
			} catch(OrderProcessException ex) {
				log.debug(ex.getMessage());
				FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						WebConstants.ERROR_WHILE_PROCESSING_ORDER_KEY));
			}
		} else {
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
				WebConstants.ERROR_INVALID_CONFIRMATION_CODE_KEY));
		}
	}
	
	/**
	 * This method is used for loading order items for a specific order.
	 * @param orderId the order's id
	 */
	private void fetchOrderItemsFromRepository(Long orderId) {
		setOrderItems(null);  // reset the list of order items
		if(orderId != null && orderId > 0) {
			Order order = orderService.retrieveOrder(orderId);
			
			if(order != null) {
				this.currentOrder = new OrderDTO(order.getId(), order.getPlacedTime(), 
						order.getSentTime(), order.getReceivedTime());
				List<OrderDetails> entries = order.getListOfOrderDetails();
				if(entries != null && !entries.isEmpty()) {
					this.orderItems = new ArrayList<OrderItemDTO>();
					for(OrderDetails entry: entries) {
						OrderItemDTO item = new OrderItemDTO();
						item.setProductId(entry.getProduct().getId());
						item.setPoints(entry.getProduct().getPricePoints());
						item.setName(entry.getProduct().getName());
						item.setSurpriseProduct(entry.getType() == OrderDetailsType.SURPRISE_PRODUCT ? true: false);
						item.setWelcomeProduct(entry.getType() == OrderDetailsType.WELCOME_PRODUCT ? true: false);
						item.setSlug(entry.getProduct().getSlug());
						item.setBrandName(entry.getProduct().getBrand().getName());
						
						this.orderItems.add(item);
					}
				}
			} else {
				// TODO: this is an AJAX operation, what should be done in case of an error?
				throw new RuntimeException("Error: no order found");
			}
		}		
	}
	
	// --- other helper methods --- //
	public String formatDateToMonthAndYear(String source) {
		return DateUtils.formatDateToMonthAndYear(source) ;
	}
	
	public String formatDateToYYYYMMDD(Date source) {
		return DateUtils.formatDateToYYYYMMDD(source) ;
	}	
	
	public String displayStatusIndicator(Date placedTime, Date sentTime, Date receivedTime) {
		String label = null;
		
		if(placedTime != null && sentTime == null) {
			label = FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
					WebConstants.PREPARED_STATUS_KEY);
		} else if(sentTime != null && receivedTime == null) {
			label = FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
					WebConstants.DELIVERY_STATUS_KEY);
		} else if(receivedTime != null) {
			label = FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
					WebConstants.DELIVERED_STATUS_KEY);
		}
		
		return label;
	}	
	
	// -- setters & getters -- /
	public List<OrderDTO> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDTO> orders) {
		this.orders = orders;
	}

	public List<OrderItemDTO> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemDTO> orderItems) {
		this.orderItems = orderItems;
	}

	public OrderDTO getCurrentOrder() {
		return currentOrder;
	}

	public void setCurrentOrder(OrderDTO currentOrder) {
		this.currentOrder = currentOrder;
	}

}
