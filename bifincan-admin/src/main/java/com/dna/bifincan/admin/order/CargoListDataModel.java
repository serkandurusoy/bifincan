package com.dna.bifincan.admin.order;

import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.dna.bifincan.model.order.Order;
import com.dna.bifincan.service.OrderService;

public class CargoListDataModel extends OrderDataModel {
	private static final long serialVersionUID = 4470680066948726786L;
	
	private static String DEFAULT_SORTED_COL = "address.user.firstName"; 

	public CargoListDataModel(OrderService orderService) {
		super(orderService);
	}	
	
	@Override
	public List<Order> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		// sorting
		if(sortField == null || sortField.isEmpty()) sortField = DEFAULT_SORTED_COL;
		
		String[] sortedCols = new String[] { sortField }; 
		
		Sort.Order[] sortOrders = new Sort.Order[sortedCols.length]; 
		for(int i=0; i < sortOrders.length; i++) {
			sortOrders[i] = new Sort.Order(sortOrder == SortOrder.DESCENDING ? 
					Sort.Direction.DESC : Sort.Direction.ASC, sortedCols[i]);
		}
		
		Sort defaultSort = new Sort(sortOrders);
    
        Pageable cond = new PageRequest(first / pageSize, pageSize, defaultSort);

        // filtering (note: filtering uses the LIKE operator... indexing may be necessary for the columns being filtered !
        //String criterion = null;
        //if(filters != null && filters.size() > 0) {
        //	criterion = filters.get(DEFAULT_SORTED_COL);
        //} 
        //criterion = "%" + (criterion != null ? criterion : "") + "%"; 
        //List<Order> orders = orderService.findxxxxxxx(criterion, cond); 
        
		List<Order> orders = orderService.findActivePendingOrders(cond);   
		
		this.setRowCount(orderService.countActivePendingOrders().intValue());  
		
		return orders;
	}

}
