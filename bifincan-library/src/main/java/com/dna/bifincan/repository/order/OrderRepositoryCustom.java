/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.repository.order;

import com.dna.bifincan.model.order.Order;
import com.dna.bifincan.model.user.User;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author hantsy
 */
interface OrderRepositoryCustom {

    public BigDecimal sumOfOrdersByUser(User _user);

    public List findLatestMoneyValuesStatistics();

   // public List<Order> findPendingOrders(int first, int pageSize);

    public Long countPendingOrders();

    public Long countActivePendingOrders();
    
    public List<Order> findInDeliveryOrders(int first, int pageSize);

    public Long countInDeliveryOrders();

    public List<Order> findReceivedOrders(int first, int pageSize);

    public Long countReceivedOrders();
}
