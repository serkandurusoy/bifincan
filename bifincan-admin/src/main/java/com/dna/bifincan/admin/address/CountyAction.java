package com.dna.bifincan.admin.address;

import java.io.Serializable;

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
import com.dna.bifincan.model.address.County;
import com.dna.bifincan.model.address.LocalRegion;
import com.dna.bifincan.service.AddressService;

@Named("countyAction")
@Scope(ScopeType.VIEW)
public class CountyAction implements Serializable {
	private static final long serialVersionUID = -3094045932591777186L;
	private static final Logger log = LoggerFactory.getLogger(CountyAction.class);

	private LazyDataModel<County> dataModel;
    private County currentCounty;
    private DataTable dataTable;
    
    @Inject
    AddressService addressService;

    @PostConstruct
    public void initialize() {
    	this.dataModel = new CountyDataModel(addressService);
    }

    public void initAdd() {
        this.currentCounty = new County();
    }

    public void initEdit(County county) {
        this.currentCounty = county;
    }

    
    public void save() {
    	try {
    		Long id = this.currentCounty.getId();
	        if(this.addressService.saveCounty(this.currentCounty)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"county.error.duplicateName"));
	        }
	        
	        this.currentCounty = null;
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
	        if(this.addressService.deleteCounty(this.currentCounty)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"county.error.inUse"));
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
    public LazyDataModel<County> getDataModel() {
    	return this.dataModel; 
    }
    
    public County getCurrentCounty() {
        return currentCounty;
    }

    public void setCurrentCounty(County currentCounty) {
        this.currentCounty = currentCounty;
    }   
    
	public DataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}
}
