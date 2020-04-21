package com.dna.bifincan.admin.address;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;
import com.dna.bifincan.admin.util.spring.ScopeType;
import com.dna.bifincan.model.address.LocalRegion;
import com.dna.bifincan.service.AddressService;

@Named("localRegionAction")
@Scope(ScopeType.VIEW)
public class LocalRegionAction implements Serializable {
	private static final long serialVersionUID = -5019370176144786374L;
	private static final Logger log = LoggerFactory.getLogger(LocalRegionAction.class);

    private LazyDataModel<LocalRegion> dataModel;
    private LocalRegion currentLocalRegion;
    private DataTable dataTable;
    
    @Inject
    AddressService addressService;

    @PostConstruct
    public void initialize() {
    	this.dataModel = new LocalRegionDataModel(addressService);
    }

    public void initAdd() {
        this.currentLocalRegion = new LocalRegion();
    }

    public void initEdit(LocalRegion region) {
        this.currentLocalRegion = region;
    }
    
    public void save() {
    	try {
    		Long id = this.currentLocalRegion.getId();
	        if(this.addressService.saveLocalRegion(this.currentLocalRegion)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"localRegion.error.duplicateName"));
	        }
	        
	        this.currentLocalRegion = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}  
    	
    	initialize();
    	this.dataTable.setFirst(0);
    }
   
    public void delete() {
    	try {
	        if(this.addressService.deleteLocalRegion(this.currentLocalRegion)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"localRegion.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    

		initialize();
    	this.dataTable.setFirst(0);
    }    
    
    // --- Getters and Setters --- //
    public LazyDataModel<LocalRegion> getDataModel() {
    	return this.dataModel; 
    }
    
    public LocalRegion getCurrentLocalRegion() {
        return currentLocalRegion;
    }
    
	public void setCurrentLocalRegion(LocalRegion currentLocalRegion) {
        this.currentLocalRegion = currentLocalRegion;
    }
	
	public DataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}

}
