package com.dna.bifincan.admin.address;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.dna.bifincan.model.address.County;
import com.dna.bifincan.model.address.District;
import com.dna.bifincan.service.AddressService;

@Named("districtAction")
@Scope(ScopeType.VIEW)
public class DistrictAction implements Serializable {
	private static final long serialVersionUID = -3174875277982643011L;
	private static final Logger log = LoggerFactory.getLogger(DistrictAction.class);

	private LazyDataModel<District> dataModel;
    private District currentDistrict;
    private DataTable dataTable;
    private List<County> allCounties = null;
    
    @Inject
    AddressService addressService;

    @PostConstruct
    public void initialize() {
    	this.dataModel = new DistrictDataModel(addressService);
    	if(this.allCounties == null) this.allCounties = addressService.getCounties();
    }

    public void initAdd() {
        this.currentDistrict = new District();
    }

    public void initEdit(District district) {
        this.currentDistrict = district;
    }

    public void save() {
    	try {
    		Long id = this.currentDistrict.getId();
	        if(this.addressService.saveDistrict(this.currentDistrict)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"district.error.duplicateName"));
	        }
	        
	        this.currentDistrict = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}  

    	initialize();
    	this.dataTable.setFirst(0);
    }
   
    /**
     * This method is used automatically by the drop-down box for selecting counties
     * @param query
     * @return
     */
    public List<County> completeCounty(String query) {  
        List<County> results = new ArrayList<County>();  
        
        if(query.length() > 0) {
	        for(County tempCounty : this.allCounties) {  
	            if(tempCounty.getName().contains(query)) {
	            	results.add(tempCounty);  
	            }  	
	        }  
        }  
        
        return results;  
    } 
    
    public void delete() {
    	try {
	        if(this.addressService.deleteDistrict(this.currentDistrict)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"district.error.inUse"));
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
    public LazyDataModel<District> getDataModel() {
    	return this.dataModel; 
    }    
    
    public District getCurrentDistrict() {
        return currentDistrict;
    }

    public void setCurrentDistrict(District currentDistrict) {
        this.currentDistrict = currentDistrict;
    }
    
	public DataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}    
}
