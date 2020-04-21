package com.dna.bifincan.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.order.Order;
import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.model.user.User;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
	@Query("select o from OrderDetails o where o.order = ?1 and o.id not in (select s.orderDetail.id from SurveyProductStatusForOrderDetails s) order by o.id asc")
	public List<OrderDetails> findNotSurveyableOrderDetailIdsByOrder(Order order);
	
	@Query("select o from OrderDetails o where o.order.id = :orderId and o.product.id = :productId")
	public OrderDetails getOrderItemByOrderIdAndProductId(@Param("orderId")Long orderId, 
			@Param("productId")Long productId);
	
	@Query("select count(o) from OrderDetails o where o.product.id = :productId")
	public Long countByProduct(@Param("productId")Long productId);	

	@Query("select max(o.order.id) from OrderDetails o where o.order.address.user.id = :userId and o.product.id = :productId")
	public Long findLastUserOrderByOrderDetailsProduct(@Param("userId")Long userId, @Param("productId")Long productId);	
	
	@Query("select distinct o.product.id from OrderDetails o where o.product.id in (?1) and o.surveyCompleted = true")
	public List<Long> findAlreadyOrderedAndSurveyCompletedByList(List<Long> productIds);	
	
	//@Query("select distinct o.product.id from OrderDetails o where o.product.id in (?1) and o.surveyCompleted = true and 3 <= (select count(distinct o1.order.address.user) from OrderDetails o1 where o1.product.id = o.product.id and o1.surveyCompleted = true)")
	//public List<Long> findMinOrderedAndSurveyCompletedByList(List<Long> productIds);	
	
	@Query("select distinct o.product.id, count(distinct o.order.address.user) from OrderDetails o where o.product.id in (?1) and o.surveyCompleted = true group by o.product.id having count(distinct o.order.address.user) >= 3")
	public List<Object[]> findMinOrderedAndSurveyCompletedByList(List<Long> productIds);	
	
	@Query("select distinct o.order.address.user.id from OrderDetails o where o.product.id = ?1 and o.surveyCompleted = true")
	public List<Long> fetchOrderUserIdsBySurveyedProductId(Long productId);		
	
	@Query("select count(o) from OrderDetails o where o.surveyCompleted = true and o.order.address.user.id = :userId")
	public Long countOrderedProductsByUser(@Param("userId")Long userId);
	
	@Query("select sum(o.product.pricePoints) from OrderDetails o where o.surveyCompleted = true and o.order.address.user.id = :userId")
	public Long countPointsOfOrderedProductsByUser(@Param("userId")Long userId);	
	
	@Query("select count(distinct o.order.address.user) from OrderDetails o where o.surveyCompleted = true and o.product.id = :productId ")
	public Long countCompletedProductOrders(@Param("productId")Long productId);	
	
	@Query("select count(o) from OrderDetails o where o.surveyCompleted = true and o.order.address.user = :user ")
	public Long countSurveyCompletedOrderDetailsByUser(@Param("user")User user);	
	

}
