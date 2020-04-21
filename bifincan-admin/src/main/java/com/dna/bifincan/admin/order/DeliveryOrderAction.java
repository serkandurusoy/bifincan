package com.dna.bifincan.admin.order;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;
import com.dna.bifincan.admin.util.spring.ScopeType;

@Named("deliveryOrderAction")
@Scope(ScopeType.VIEW)
public class DeliveryOrderAction extends OrderAction {
	private static final long serialVersionUID = -443472051322101240L;
	private static final Logger log = LoggerFactory.getLogger(DeliveryOrderAction.class);
    		
    @PostConstruct
    public void initialize() {
    	this.dataModel = new DeliveryOrderDataModel(orderService);  
    }
    
    public void setReceivedTime() {
    	try {
	        if(this.orderService.setOrderReceivedTime(this.currentOrder)) { // success  
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (either already being set or does not exist)  
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"order.error.sentTime"));  
	        }
	        
	        this.currentOrder = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}  
    	
    	initialize();
    	this.dataTable.setFirst(0);    	
    }    
    
    // --- Helper methods --- //
    
    // --- Getters and Setters --- //
}
