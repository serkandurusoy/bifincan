package com.dna.bifincan.admin.order;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;

import com.dna.bifincan.model.order.Order;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.service.OrderService;

public class DeliveryOrderDataModel extends OrderDataModel {
	private static final long serialVersionUID = 4842552865397571035L;

	public DeliveryOrderDataModel(OrderService orderService) {
		super(orderService);
	}	
	
	@Override
	public List<Order> load(int first, int pageSize, String string, SortOrder so,
			Map<String, String> map) {
		List<Order> orders = orderService.findInDeliveyOrders(first, pageSize);
		this.setRowCount(orderService.countInDeliveryOrders().intValue());
		
		return orders;
	}

}
