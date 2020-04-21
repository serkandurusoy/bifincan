/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.repository.order;

import com.dna.bifincan.model.order.Order;
import com.dna.bifincan.model.user.User;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 *
 * @author hantsy
 */
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private static final String FIND_PENDING_QUERY_KEY = "Order.findPending"; 
    
    private static final String FIND_ACTIVE_PENDING_QUERY_KEY = "Order.countActivePending";
    
    private static final String COUNT_PENDING_QUERY_KEY = "Order.countPending";

    private static final String FIND_IN_DELIVERY_QUERY_KEY = "Order.findInDelivery";

    private static final String COUNT_IN_DELIVERY_QUERY_KEY = "Order.countInDelivery";

    private static final String FIND_RECEIVED_QUERY_KEY = "Order.findReceived";

    private static final String COUNT_RECEIVED_QUERY_KEY = "Order.countReceived";

    @PersistenceContext
    EntityManager em;
    
   // public List<Order> findPendingOrders(int first, int pageSize) {
   //     return em.createNamedQuery(FIND_PENDING_QUERY_KEY, Order.class).setFirstResult(first).setMaxResults(pageSize).getResultList();
   // }

    public Long countPendingOrders() {
        return em.createNamedQuery(COUNT_PENDING_QUERY_KEY, Long.class).getSingleResult();
    }

    public Long countActivePendingOrders() {
        return em.createNamedQuery(FIND_ACTIVE_PENDING_QUERY_KEY, Long.class).getSingleResult();
    }
    
    
    public List<Order> findInDeliveryOrders(int first, int pageSize) {
        return em.createNamedQuery(FIND_IN_DELIVERY_QUERY_KEY, Order.class).setFirstResult(first).setMaxResults(pageSize).getResultList();
    }

    public Long countInDeliveryOrders() {
        return em.createNamedQuery(COUNT_IN_DELIVERY_QUERY_KEY, Long.class).getSingleResult();
    }

    public List<Order> findReceivedOrders(int first, int pageSize) {
        return em.createNamedQuery(FIND_RECEIVED_QUERY_KEY, Order.class).setFirstResult(first).setMaxResults(pageSize).getResultList();
    }

    @Override
    public BigDecimal sumOfOrdersByUser(User _user) {
        return em.createQuery("select sum(d.product.priceMoney) from Order o left join o.listOfOrderDetails d where o.address in (:address)  ", BigDecimal.class)
                .setParameter("address", _user.getAddresses()) 
                //  .setParameter("detailType", OrderDetailsType.ORDERABLE_PRODUCT)
                .getSingleResult();
    }

    public Long countReceivedOrders() {
        return em.createNamedQuery(COUNT_RECEIVED_QUERY_KEY, Long.class).getSingleResult();
    }
    
    @Override
    public List findLatestMoneyValuesStatistics() {
        //return em.createQuery("select new map(sum(d.product.priceMoney) as score, o.address.user as user) from Order o left join o.listOfOrderDetails d where o.address.user.active=true group by o.address.user  order by score desc, o.address.user.createDate desc")
        //return em.createQuery("select new map(sum(d.product.priceMoney) as score, o.address.user as user) from Order o left join o.listOfOrderDetails d group by o.address.user  order by score desc, o.address.user.createDate desc")
        return em.createQuery("select new map(sum(d.product.priceMoney) as score, o.address.user as user) from Order o left join o.listOfOrderDetails d group by o.address.user,o.address.user.createDate  order by score desc, o.address.user.createDate desc")
                 .setMaxResults(10)
                 .getResultList();
    }
}
