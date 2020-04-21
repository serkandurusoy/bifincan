package com.dna.bifincan.admin.order;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;
import com.dna.bifincan.admin.util.spring.ScopeType;

@Named("pendingOrderAction")
@Scope(ScopeType.VIEW)
public class PendingOrderAction extends OrderAction {
	private static final long serialVersionUID = -4141407570071376631L;
	private static final Logger log = LoggerFactory.getLogger(PendingOrderAction.class);
    		
    @PostConstruct
    public void initialize() {
    	this.dataModel = new PendingOrderDataModel(orderService); 
    }
    
    public void setSentTime() {
    	try {
	        if(this.orderService.setOrderSentTime(this.currentOrder)) { // success 
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
