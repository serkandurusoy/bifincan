package com.dna.bifincan.admin.order;

import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.dna.bifincan.model.order.Order;
import com.dna.bifincan.service.OrderService;

public class PendingOrderDataModel extends OrderDataModel {
	private static final long serialVersionUID = -2011773546211731615L;

	private static String DEFAULT_SORTED_COL = "address.user.lastName"; 
	private static String LASTNAME_COL = "address.user.lastName"; 
	private static String FIRSTNAME_COL = "address.user.firstName"; 
	private static String CITY_COL = "address.area.district.county.city.name"; 
	
	public PendingOrderDataModel(OrderService orderService) {
		super(orderService);
	}	
	
	@Override
	public List<Order> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {

		// sorting
		if(sortField == null || sortField.isEmpty()) sortField = DEFAULT_SORTED_COL;
		
		String[] sortedCols = null;
		if(DEFAULT_SORTED_COL.equals(sortField)) {
			sortedCols = new String[] { FIRSTNAME_COL, LASTNAME_COL };  // NOTE: the o. prefix (ie. the alias of the main table in the queries) is added automatically !
		} else {
			sortedCols = new String[] { sortField }; 
		}

		Sort.Order[] sortOrders = new Sort.Order[sortedCols.length]; 
		for(int i=0; i < sortOrders.length; i++) {
			sortOrders[i] = new Sort.Order(sortOrder == SortOrder.DESCENDING ? 
					Sort.Direction.DESC : Sort.Direction.ASC, sortedCols[i]);
		}
		
		Sort defaultSort = new Sort(sortOrders);
    
        Pageable cond = new PageRequest(first / pageSize, pageSize, defaultSort);

        // filtering (note: filtering uses the LIKE operator... indexing may be necessary for the columns being filtered !
        String nameCriterion = null;
        String cityCriterion = null;
        
        if(filters != null && filters.size() > 0) {
        	nameCriterion = filters.get(LASTNAME_COL);
        	cityCriterion = filters.get(CITY_COL);
        }
        
        nameCriterion = "%" + (nameCriterion != null ? nameCriterion : "") + "%"; 
        cityCriterion = "%" + (cityCriterion != null ? cityCriterion : "") + "%"; 
        
		List<Order> orders = orderService.findPendingOrders(nameCriterion, cityCriterion, cond); 
		this.setRowCount(orderService.countPendingOrders().intValue());  
		
		return orders;
	}

}
