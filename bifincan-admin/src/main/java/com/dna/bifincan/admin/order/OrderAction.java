package com.dna.bifincan.admin.order;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;
import com.dna.bifincan.model.order.Order;
import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.service.OrderService;

@SuppressWarnings("serial")
public abstract class OrderAction implements Serializable {
	private static final Logger log = LoggerFactory.getLogger(OrderAction.class);

	protected LazyDataModel<Order> dataModel;
	protected Order currentOrder;
	protected List<OrderDetails> items;
	protected DataTable dataTable;
    		
    @Inject
    protected OrderService orderService;

    /*
    public void initAdd() {
        this.currentOrder = new Order();
    }
	*/
    
    public void initEdit(Order order) {
        this.currentOrder = order;
        this.items = order.getListOfOrderDetails();
    } 
    
    public void save() {
    	try {
    		Long id = this.currentOrder.getId();

	        if(this.orderService.saveOrder(this.currentOrder)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } /*else { // failure (duplicate fields)  // not used at the moment
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"order.error.duplicateFields"));  
	        }*/
	        
	        this.currentOrder = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}  
    	
    }
   
   /* public void delete() {
    	try {
	        if(this.orderService.deleteOrder(this.currentOrder)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)  // not used at the moment
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"order.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    

		initialize();
    	this.dataTable.setFirst(0);
    }*/ 
    
    // --- Getters and Setters --- //
    public LazyDataModel<Order> getDataModel() {
    	return this.dataModel; 
    }
    
    public Order getCurrentOrder() {
        return currentOrder;
    }
    
	public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }
	
	public DataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}

	public List<OrderDetails> getItems() {
		return items;
	}

	public void setItems(List<OrderDetails> items) {
		this.items = items;
	}
	
}
