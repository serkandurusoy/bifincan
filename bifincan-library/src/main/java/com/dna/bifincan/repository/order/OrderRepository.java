package com.dna.bifincan.repository.order;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

	/*
    @Query("select o from Order o join o.listOfOrderDetails d where "
    + "o.id = (select max(o1.id) from Order o1 where o1.address.user.id = :userId)")
    public Order getLastUserOrder(@Param("userId") Long userId);
*/
    @Query("select o from Order o  where "
    + "o.id = (select max(o1.id) from Order o1 where o1.address.user.id = :userId)")
    public Order getLastUserOrder(@Param("userId") Long userId);
    
    @Query("select max(o.order.id) from OrderDetails o where "
    + "o.product.id = :productId and o.order.address.user.id = :userId)")
    public Long getLastUserOrderByProduct(@Param("productId") Long productId, @Param("userId") Long userId);

    @Query("select o from Order o where o.address.user.username = :username order by o.placedTime desc)")
    public List<Order> getOrdersByUsername(@Param("username") String username); 
    
    @Query("select o from Order o where o.placedTime is not null and o.sentTime is null and o.receivedTime is null and (o.address.user.lastName like ?1 and o.address.user.firstName like ?1 and o.address.area.district.county.city.name like ?2)")
    public List<Order> findPendingOrders(String nameCriterion, String cityCriterion, Pageable page);   
    
    @Query("select o from Order o where o.placedTime is not null and o.sentTime is null and o.receivedTime is null and o.address.user.active = true")
    public List<Order> findActivePendingOrders(Pageable page);   
    
    @Query("select distinct o from Order o where o.address.user.active = true and o.placedTime is not null and o.sentTime is not null and o.receivedTime is null and o.sentTime <=?1")
    public List<Order> findUnconfirmedOrders(Date refDate);     
    
    @Query("select distinct o.order from OrderDetails o where o.surveyCompleted = false and o.order.address.user.active = true and o.order.receivedTime is not null and o.order.receivedTime <=?1")
    public List<Order> findNoSurveyCompletedOrderIds(Date refDate);     
    
}
