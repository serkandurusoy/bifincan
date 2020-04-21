package com.dna.bifincan.model.dto;

import com.dna.bifincan.model.order.OrderDetails;

public class ProductRating {

	private OrderDetails orderDetail;

	public OrderDetails getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(OrderDetails orderDetail) {
		this.orderDetail = orderDetail;
	}
}
