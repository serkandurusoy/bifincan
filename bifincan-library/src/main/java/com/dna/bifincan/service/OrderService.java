package com.dna.bifincan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.exception.ConfigurationException;
import com.dna.bifincan.exception.NoSurpiseProductsException;
import com.dna.bifincan.exception.NoWelcomeProductsException;
import com.dna.bifincan.exception.OrderProcessException;
import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.order.Order;
import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.type.OrderDetailsType;
import com.dna.bifincan.model.type.PointTransactionType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.repository.order.OrderDetailsRepository;
import com.dna.bifincan.repository.order.OrderRepository;
import com.dna.bifincan.repository.products.ProductRepository;

@Service
@Named("orderService")
public class OrderService {
    private final static Logger log = LoggerFactory.getLogger(OrderService.class);

    @Inject
    private OrderRepository orderRepository;
    @Inject
    private OrderDetailsRepository orderDetailsRepository;
    @Inject
    private ProductRepository productRepository;
    @Inject
    private ProductService productService;
    @Inject
    private ConfigurationService configurationService;
    @Inject
    private UserService userService;
    @Inject
    private PointTransactionAccountingService transactionService;

    public OrderService() { }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {NoWelcomeProductsException.class,
        OrderProcessException.class, ConfigurationException.class})
    public void createWelcomeOrder(User user) throws OrderProcessException,
            NoWelcomeProductsException, ConfigurationException {

        User tempUser = userService.findUserByUsername(user.getUsername()); // take a fresh copy of the user entity

        // Perform validations upon the current user entity stored in the DB (checks e-mail & gsm & active flag)
        boolean canUserOrder = userService.canUserOrder(tempUser);

        if (canUserOrder == true) { // the user is valid, so go on...
            // ensure that there is no other order stored in the system
            Order order = orderRepository.getLastUserOrder(user.getId());

            if (order == null) {
                order = new Order();

                // set up the address 
                order.setAddress(userService.getUserPrimaryAddress(user));
                // set the time of creation
                order.setPlacedTime(new Date());

                // find the maximum allowed number of 'welcome' products
                Configuration config = configurationService.find(ConfigurationType.MAX_NO_OF_WELCOME_PRODUCTS.getKey());

                if (config != null) {  // if the value is defined
                    int maxNoOfWelcomeProducts = Integer.parseInt(config.getValue());
                    /*
                     * Note: here we are going to execute a few queries which if
                     * we wanted to execute them within one single query we
                     * would have a big inefficient one with correlated
                     * sub-queries. So instead of a such big query, we have 3
                     * smaller ones which are more efficient when are executed
                     * and easier to maintain or enhance
                     */
                    List<Long> productIds = getRandomProductIds(tempUser, maxNoOfWelcomeProducts,
                            OrderDetailsType.WELCOME_PRODUCT);

                    if (productIds != null && !productIds.isEmpty()) { // if we got a list actually of product stock ids..
                        // create a list of the order items
                        List<OrderDetails> orderItems = getOrderDetailsByProductIds(order,
                                productIds, OrderDetailsType.WELCOME_PRODUCT);

                        // set the order's list of order items (order details)
                        order.setListOfOrderDetails(orderItems);
                    } else {
                        /*
                         * TODO: here possibly we could fill the list of
                         * 'welcome' products with some ones from the list of
                         * emergency products. This is a HUGE exception for the
                         * welcome order....
                         */
                        throw new NoWelcomeProductsException("No welcome products found. "
                                + "Probably this list may use the emergency list of products.");
                    }

                } else {
                    throw new ConfigurationException("The configuration value for maximum welcome products has not been set");
                }

                orderRepository.save(order);  // persist the order

            } else {
                throw new OrderProcessException("User [" + tempUser.getUsername()
                        + "] has already an order related to his account");
            }
        } else {
            throw new OrderProcessException("The given user entity either is not valid or it hasn't the rights yet "
                    + "to place orders in the system");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {NoSurpiseProductsException.class,
        OrderProcessException.class, ConfigurationException.class})
    public Object[] createOrder(String username, Set<String> productSlugs) throws OrderProcessException,
            NoSurpiseProductsException, ConfigurationException {
        Object[] orderResults = new Object[2];
        // Check upon the current user entity stored in the DB (checks e-mail & gsm & active flag)
        User user = userService.findUserByUsername(username); // take a fresh copy of the user entity

        int currentUserPoints = user.getPointsBalance();

        boolean canUserOrder = canUserPlaceNewOrder(user);
        if (canUserOrder == true) {  // the user can place a new order - go on....
            Configuration orderableConfig = configurationService.find(ConfigurationType.MAX_NO_OF_ORDERABLE_PRODUCTS.getKey());
            int numberOfOrderableProducts = Integer.parseInt(orderableConfig.getValue());

            Configuration bounusOrderableConfig = configurationService.find(ConfigurationType.NO_OF_BONUS_ORDERABLE_PRODUCTS.getKey());
            int numberOfBonusOrderableProducts = Integer.parseInt(bounusOrderableConfig.getValue());

            switch(user.getActivityLevel() ) {
            	case 1: throw new OrderProcessException(OrderProcessException.LOW_USER_ACTIVITY_LEVEL,
            			"Your activity level is too low. Unfortunately you cannot place an order right now.");
            	case 3:	numberOfOrderableProducts += numberOfBonusOrderableProducts;
            }

            List<Product> products = productService.findProductsBySlugs(productSlugs);
            if (products != null && products.size() > 0 && products.size() <= numberOfOrderableProducts) {
                // construct the main product item list

                Order order = new Order();
                List<OrderDetails> orderItems = new ArrayList<OrderDetails>();

                // find the product ids validated upon order criteria for the given user
                Set<Long> allResults = productService.fetchProductIdsValidatedUponUserCriteria(user);

                int sumOfPoints = 0;
                // construct the order item list

                List<Long> addedProductIds = new ArrayList<Long>();
                for (Product product : products) {
                    // check if the product is available
                    if (product.getRemainingQuantity() <= 0 || !product.isActive() || !product.isOrderableProduct()) {
                        throw new OrderProcessException(OrderProcessException.PRODUCT_IS_NOT_AVAILABLE,
                                "The product [" + product.getId() + "] is not available");
                    }

                    if (!allResults.contains(product.getId())) { // check if the product is valid upon order criteria
                        throw new OrderProcessException(OrderProcessException.NO_RIGHTS_TO_PLACE_ORDER,
                                "The product [" + product.getId() + "] is not validated upon order criteria");
                    }
                    // construct the order item
                    OrderDetails item = new OrderDetails();

                    item.setOrder(order);
                    item.setProduct(product);
                    item.setSurveyCompleted(false);
                    item.setType(OrderDetailsType.ORDERABLE_PRODUCT);

                    orderItems.add(item);

                    addedProductIds.add(product.getId()); // keep a rerefence to its id for use later

                    product.setOrderQuantity(product.getOrderQuantity() + 1);
                    //product.setRemainingQuantity(product.getRemainingQuantity() - 1); // TODO: remove this line

                    sumOfPoints += product.getPricePoints();
                }

                if (sumOfPoints > user.getPointsBalance()) {
                    throw new OrderProcessException(OrderProcessException.NO_AVAILABLE_USER_POINTS,
                            "The user's available points [" + user.getPointsBalance()
                            + "] are less than the total of points of the ordered products [" + sumOfPoints + "]");
                }
                // set up other info
                order.setAddress(userService.getUserPrimaryAddress(user));
                order.setPlacedTime(new Date());

                // construct now the list of surprise products and add it to the orders item list
                // find the maximum allowed number of 'surprise' products
                Configuration surpriseConfig = configurationService.find(ConfigurationType.MAX_NO_OF_SURPRISE_PRODUCTS.getKey());

                if (surpriseConfig != null) {  // if the value is defined
                    int maxNoOfSurpriseProducts = Integer.parseInt(surpriseConfig.getValue());

                    // get a random list with the oldest products stock ids (active = true & remaining quantity > 0) 
                    // for the list of random 'surprise' product ids 
                    List<Long> surpriseProductIds = getRandomProductIds(user, maxNoOfSurpriseProducts,
                            OrderDetailsType.SURPRISE_PRODUCT);

                    if (surpriseProductIds != null) {
                        surpriseProductIds.removeAll(addedProductIds); // remove any already used products
                    }
                    if (surpriseProductIds != null && !surpriseProductIds.isEmpty()) { // if we still have a list of product ids
                        // create a list of the surprise order items

                        List<OrderDetails> surpriseOrderItems = getOrderDetailsByProductIds(order,
                                surpriseProductIds, OrderDetailsType.SURPRISE_PRODUCT);
                        orderItems.addAll(surpriseOrderItems);

                    } else {
                        /*
                         * TODO: here possibly we could fill the list of
                         * 'surprise' products with some ones from the list of
                         * emergency products
                         */
                        //throw new NoSurpiseProductsException("No surprise products found. " +
                        //		"Probably this list may use the emergency list of products.");
                        // do nothing currently. just let the order to complete.
                    }
                } else {
                    throw new ConfigurationException("The configuration value for maximum surprise products has not been set");
                }

                // store the whole product item list
                order.setListOfOrderDetails(orderItems);

                // persist the order
                orderRepository.save(order);

                // update user's points balance
                currentUserPoints -= sumOfPoints;

                transactionService.addPoints(user, PointTransactionType.PRODUCT_ORDER, sumOfPoints); // add a transaction record

                orderResults[0] = orderItems;
                orderResults[1] = currentUserPoints;
            } else {
                throw new OrderProcessException(OrderProcessException.INVALID_NUMBER_OF_PRODUCTS_IN_CART,
                        "Invalid number of products in the shopping cart !");
            }
        } else {
            throw new OrderProcessException(OrderProcessException.NO_RIGHTS_TO_PLACE_ORDER,
                    "The given user [" + user.getUsername()
                    + "] entity either is not valid or it hasn't the rights yet "
                    + "to place orders in the system");
        }

        return orderResults;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<OrderDetails> getOrderItemsForSurveyableOrder(User user) {
        List<OrderDetails> results = null;

        // 1. Find the last order for the given user and current date >= order receivedDate + daysNeededToPass
        Order lastOrder = orderRepository.getLastUserOrder(user.getId());

        int daysNeededToPass = 0;
        Configuration config = configurationService.find((ConfigurationType.DAYS_REQUIRED_BEFORE_NEXT_ORDER.getKey()));

        if (config != null) {
            daysNeededToPass = Integer.parseInt(config.getValue());
        }

        boolean isValidInTimeRange = isLastOrderValidInTimeRange(lastOrder, daysNeededToPass);

        if (isValidInTimeRange) { // it's ok, so go on...
            List<OrderDetails> details = lastOrder.getListOfOrderDetails();
            // iterate the order details and sse if there are valid stocks for surveying
            if (details != null && !details.isEmpty()) {
                results = new ArrayList<OrderDetails>();
                for (OrderDetails d : details) {
                    if (!d.isSurveyCompleted()) { // if it hasn't been surveyed yet
                        results.add(d); // add it to the result list
                    }
                }
                if (results.isEmpty()) {
                    results = null; // just to indicate no results
                }
            }
        }

        return results;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Long> getOrderItemIdsForSurveyableOrder(User user) {
        List<Long> results = null;

        // 1. Find the last order for the given user and current date >= order receivedDate + daysNeededToPass
        Order lastOrder = orderRepository.getLastUserOrder(user.getId());

        int daysNeededToPass = 0;
        Configuration config = configurationService.find((ConfigurationType.DAYS_REQUIRED_BEFORE_NEXT_ORDER.getKey()));

        if (config != null) {
            daysNeededToPass = Integer.parseInt(config.getValue());
        }

        boolean isValidInTimeRange = isLastOrderValidInTimeRange(lastOrder, daysNeededToPass);

        if (isValidInTimeRange) { // it's ok, so go on...
            List<OrderDetails> details = lastOrder.getListOfOrderDetails();
            // iterate the order details and sse if there are valid stocks for surveying
            if (details != null && !details.isEmpty()) {
                results = new ArrayList<Long>();
                for (OrderDetails d : details) {
                    if (!d.isSurveyCompleted()) { // if it hasn't been surveyed yet
                        results.add(d.getId()); // add it's id to the result list
                    }
                }
                if (results.isEmpty()) {
                    results = null; // just to indicate no results
                }
            }
        }

        return results;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void disableOrderDetailSurveyableFlag(OrderDetails orderDetails) {
        // TODO: this probably will need refinement
        // note: in order to prevent persisting any other accidental modifications, it modifies the original item
        OrderDetails curOrderDetails = orderDetailsRepository.findOne(orderDetails.getId());
        curOrderDetails.setSurveyCompleted(true);

        orderDetailsRepository.save(curOrderDetails);
    }

    // --- HELPER PRIVATE METHODS ---/
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private List<Long> getRandomProductIds(User user, int maxNoOfProducts, OrderDetailsType type) {
        List<Long> productIds = null;
        List<Long> excludedList = new ArrayList<Long>();
        excludedList.add(0L); // initialize it with a dummy excluded product stock id
        int totalStockFound = 0;

        int daysNeededToPass = 0;
        Configuration orderConfig = configurationService.find((ConfigurationType.DAYS_REQUIRED_BEFORE_PRODUCT_IS_ORDERABLE.getKey()));

        if (orderConfig != null) {
            daysNeededToPass = Integer.parseInt(orderConfig.getValue());
        }

        int count = 0;
        do {
            productIds = productService.getRandomLatestActiveProductIds(user,
                    maxNoOfProducts, type, excludedList);
            if (productIds != null) {
                for (Long productId : productIds) {
                    if (!isProductValidInTimeRange(user, productRepository.findOne(productId), daysNeededToPass)) {
                        if(!excludedList.contains(productId)) excludedList.add(productId);
                    } else {
                        totalStockFound++;
                    }
                }
            }
        } while ((totalStockFound < maxNoOfProducts) && (productIds != null && productIds.size() > 0) && (count++ < 20));
        
        if(productIds != null) productIds.removeAll(excludedList);
        
        return productIds;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private boolean isProductValidInTimeRange(User user, Product product, int daysNeededToPass) {
        boolean result = true;

        if (product != null) {
        	//log.debug(">>> 44444444");
            Long orderId = orderRepository.getLastUserOrderByProduct(product.getId(), user.getId());
            if (orderId != null) {
                Order order = orderRepository.findOne(orderId);
                result = isLastOrderValidInTimeRange(order, daysNeededToPass);
            }
        }

        return result;
    }

    /**
     * This method finds the receivedDate of the specified order and then adds a
     * number of days as specified in the 'daysNeededToPass' argument. Then it
     * checks whether the current date is within this time range [receivedDate,
     * receivedDate + 7].
     *
     * @param order the specified order
     * @param daysNeededToPass days needed to pass
     * @return TRUE if the current date is NOT within the time range otherwise
     * FALSE
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private boolean isLastOrderValidInTimeRange(Order order, int daysNeededToPass) {
        boolean result = true;

        if (order != null && order.getReceivedTime() != null) {
            // 1. find the value for the days required to pass before next order			
            Calendar from = Calendar.getInstance();
            from.setTime(order.getReceivedTime());
            from.set(Calendar.HOUR_OF_DAY, 0);
            from.set(Calendar.MINUTE, 0);
            from.set(Calendar.SECOND, 0);

            Calendar to = Calendar.getInstance();
            to.setTime(order.getReceivedTime());
            to.add(Calendar.DATE, daysNeededToPass);
            to.set(Calendar.HOUR_OF_DAY, 23);
            to.set(Calendar.MINUTE, 59);
            to.set(Calendar.SECOND, 59);

            Calendar today = Calendar.getInstance();

            // if current date is within the acceptable time range
            if ((today.compareTo(from) >= 0 && today.compareTo(to) <= 0)) {
                result = false;
            }
        } else {
            result = false;
        }

        return result;
    }

    public long getRemainingDaysForNextAction(User user, ConfigurationType type) {
        long remainingDays = 0;

        Order order = orderRepository.getLastUserOrder(user.getId());
        remainingDays = calculateRemainingDaysForAction(order, type);

        return remainingDays;
    }

    public long calculateRemainingDaysForAction(Order order, ConfigurationType type) {
        long remainingDays = 0;

        if (order != null) {
            if (order.getReceivedTime() != null) {
                int daysNeededToPass = 0;
                Configuration orderConfig = configurationService.find((type.getKey()));
                if (orderConfig != null) {
                    daysNeededToPass = Integer.parseInt(orderConfig.getValue());
                }

                Calendar from = Calendar.getInstance();

                Calendar to = Calendar.getInstance();
                to.setTime(order.getReceivedTime());
                to.add(Calendar.DATE, daysNeededToPass);

                long diff = to.getTimeInMillis() - from.getTimeInMillis();

                if (diff > 0) {
                    remainingDays = (diff / (24 * 60 * 60 * 1000)) + 1;
                }
            } else {
                remainingDays = -1;
            }
        }
        return remainingDays;
    }

    public boolean isLastOrderSurveyable(User user) {
        boolean surveyable = false;
        Order lastOrder = orderRepository.getLastUserOrder(user.getId());
        surveyable = isLastOrderDetailSetSurveyable(lastOrder);

        return surveyable;
    }

    public List<OrderDetails> findNotSurveyableOrderDetailIds(User user) {
        List<OrderDetails> items = null;
        Order lastOrder = orderRepository.getLastUserOrder(user.getId());
        if (lastOrder != null) {
            // this returns the order details for which there are not any initiated product surveys  
            items = orderDetailsRepository.findNotSurveyableOrderDetailIdsByOrder(lastOrder);
        }
        return items;
    }

    private boolean isLastOrderDetailSetSurveyable(Order lastOrder) {
        boolean surveyable = false;

        if (lastOrder != null && lastOrder.getReceivedTime() != null) { // it's ok, so go on...
            List<OrderDetails> details = lastOrder.getListOfOrderDetails();
            // iterate the order details and sse if there are valid stocks for surveying
            if (details != null && !details.isEmpty()) {
                for (OrderDetails d : details) {
                    if (d.isSurveyCompleted() == false) { // if it hasn't been surveyed yet
                        surveyable = true;
                        break;
                    }
                }
            }
        }
        return surveyable;
    }

    public Order getLastOrder(User user) {
    	//log.debug(">>> 88888");
        return orderRepository.getLastUserOrder(user.getId());
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = false)
    private List<OrderDetails> getOrderDetailsByProductIds(Order order,
            List<Long> productIds, OrderDetailsType type) {

        List<OrderDetails> orderItems = null;

        //TODO: for each product id find the corresponding product  
        List<Product> products = productRepository.getProductsByIds(productIds);

        if (products != null && !products.isEmpty()) { // if have a list of product entries
            orderItems = new ArrayList<OrderDetails>();

            // construct the list of order details for this 'welcome' order
            for (Product product : products) {
                OrderDetails item = new OrderDetails();

                item.setOrder(order);
                item.setProduct(product);
                item.setSurveyCompleted(false);
                item.setType(type);

                orderItems.add(item);

                //product.setRemainingQuantity(product.getRemainingQuantity() - 1);  // TODO: remove this line
                product.setOrderQuantity(product.getOrderQuantity() + 1);
            }
        }
        return orderItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean canUserPlaceNewOrder(String username) {

        User user = userService.findUserByUsername(username);
        return canUserPlaceNewOrder(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean canUserPlaceNewOrder(User user) {
        boolean result = true;

        Order lastOrder = this.getLastOrder(user);
        if (lastOrder != null) {
          /*  log.debug(">>> userService.canUserOrder(user) = " + userService.canUserOrder(user));
            log.debug(">>> lastOrder.getReceivedTime() = " + lastOrder.getReceivedTime());
            log.debug(">>> (lastOrder.getSentTime() != null) = " + (lastOrder.getSentTime() != null));
            log.debug(">>> (calculateRemainingDaysForAction(lastOrder, ConfigurationType.TIME_REQUIRED_BEFORE_PRODUCT_SURVEYS) == 0) = " + (calculateRemainingDaysForAction(lastOrder, ConfigurationType.TIME_REQUIRED_BEFORE_PRODUCT_SURVEYS) == 0));
            log.debug(">>> (calculateRemainingDaysForAction(lastOrder, ConfigurationType.DAYS_REQUIRED_BEFORE_NEXT_ORDER) == 0) = " + (calculateRemainingDaysForAction(lastOrder, ConfigurationType.DAYS_REQUIRED_BEFORE_NEXT_ORDER) == 0));
            log.debug(">>> isLastOrderDetailSetSurveyable(lastOrder) = " + isLastOrderDetailSetSurveyable(lastOrder));*/

            result = userService.canUserOrder(user)
                    && (lastOrder.getReceivedTime() != null)
                    && (lastOrder.getSentTime() != null)
                    && (calculateRemainingDaysForAction(lastOrder, ConfigurationType.TIME_REQUIRED_BEFORE_PRODUCT_SURVEYS) == 0)
                    && (calculateRemainingDaysForAction(lastOrder, ConfigurationType.DAYS_REQUIRED_BEFORE_NEXT_ORDER) == 0)
                    && isLastOrderDetailSetSurveyable(lastOrder) == false;
        }

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Order> getUserOrders(String username) {
        return orderRepository.getOrdersByUsername(username);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Order retrieveOrder(Long id) {
        return orderRepository.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Date updateOrderReceivedDate(Long orderId) throws OrderProcessException {
        Order order = orderRepository.findOne(orderId);
        if (order != null) {
            Date curDate = new Date();

            order.setReceivedTime(curDate);
            return curDate;
        } else {
            throw new OrderProcessException("Error: the order is not found");
        }
    }

    public BigDecimal sumOfOdersByUser(User _user) {
        BigDecimal _sum = this.orderRepository.sumOfOrdersByUser(_user);
        log.debug("sum @" + _sum);
        return _sum == null ? BigDecimal.ZERO : _sum;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Order> findPendingOrders(String nameCriterion, String cityCriterion, Pageable cond) {        
        return orderRepository.findPendingOrders(nameCriterion, cityCriterion, cond);  
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Long countPendingOrders() {
        return orderRepository.countPendingOrders();
    }
    
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Order> findActivePendingOrders(Pageable cond) {        
        return orderRepository.findActivePendingOrders(cond);  
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Long countActivePendingOrders() {
        return orderRepository.countActivePendingOrders(); 
    }
    
     @Transactional(propagation = Propagation.SUPPORTS)
    public List<Order> findInDeliveyOrders(int first, int pageSize) { 
        return orderRepository.findInDeliveryOrders(first, pageSize);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Long countInDeliveryOrders() {
        return orderRepository.countInDeliveryOrders();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Order> findReceivedOrders(int first, int pageSize) {
        return orderRepository.findReceivedOrders(first, pageSize);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Long countReceivedOrders() {
        return orderRepository.countReceivedOrders();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Long countSurveyCompletedOrderDetailsByUser(User user) {
        return orderDetailsRepository.countSurveyCompletedOrderDetailsByUser(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean saveOrder(Order order) {
        orderRepository.save(order);
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean setOrderSentTime(Order order) {
        Order tempOrder = orderRepository.findOne(order.getId());

        if (tempOrder != null && tempOrder.getSentTime() == null) {
            order.setSentTime(new Date());
            orderRepository.save(order);
            return true;
        }

        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean setOrderReceivedTime(Order order) {
        Order tempOrder = orderRepository.findOne(order.getId());

        if (tempOrder != null && tempOrder.getReceivedTime() == null) {
            order.setReceivedTime(new Date());
            orderRepository.save(order);
            return true;
        }

        return false;
    }

    @SuppressWarnings("rawtypes")
	public List findLatestMoneyValuesStatistics() {
        return orderRepository.findLatestMoneyValuesStatistics();
    }
 
    
    public List<Order> findUnconfirmedOrders() {
        Calendar from = Calendar.getInstance();
        from.add(Calendar.DATE, -7);
        from.set(Calendar.HOUR_OF_DAY, 15);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);

    	return orderRepository.findUnconfirmedOrders(from.getTime());
    }
    
    public List<Order> findUnconfirmedOrdersTwoWeeks() {
        Calendar from = Calendar.getInstance();
        from.add(Calendar.DATE, -15);
        from.set(Calendar.HOUR_OF_DAY, 15);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);

    	return orderRepository.findUnconfirmedOrders(from.getTime());
    }
    
    public List<Order> findNoSurveyCompletedOrders(int days) {
        Configuration orderableConfig = configurationService.find(ConfigurationType.TIME_REQUIRED_BEFORE_PRODUCT_SURVEYS.getKey());
        int daysReqBeforeProdSurveys = Integer.parseInt(orderableConfig.getValue());
        
        Calendar from = Calendar.getInstance();
        from.add(Calendar.DATE, - (days + daysReqBeforeProdSurveys) );
        from.set(Calendar.HOUR_OF_DAY, 15);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);

        return orderRepository.findNoSurveyCompletedOrderIds(from.getTime());
    }    
    
    @Transactional(propagation = Propagation.SUPPORTS)
	public Long countOrderedProductsByUser(Long userId) {
		return orderDetailsRepository.countOrderedProductsByUser(userId);
	}
	
    @Transactional(propagation = Propagation.SUPPORTS)
	public Long countPointsOfOrderedProductsByUser(Long userId) {
		return orderDetailsRepository.countPointsOfOrderedProductsByUser(userId);
	}
    
    @Transactional(propagation = Propagation.SUPPORTS)
	public Long countCompletedProductOrders(Long productId) {
		return orderDetailsRepository.countCompletedProductOrders(productId);
	}    
    
}
