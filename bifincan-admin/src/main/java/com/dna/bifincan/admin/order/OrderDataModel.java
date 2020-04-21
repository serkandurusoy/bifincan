package com.dna.bifincan.admin.order;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;

import com.dna.bifincan.model.order.Order;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.service.OrderService;

@SuppressWarnings("serial")
public abstract class OrderDataModel extends LazyDataModel<Order> {
	protected OrderService orderService;

	public OrderDataModel(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override 
	public Order getRowData(String rowKey) {
		return orderService.retrieveOrder(Long.valueOf(rowKey));
	}

	@Override
	public Object getRowKey(Order object) {
		return object.getId();
	}
	
}
