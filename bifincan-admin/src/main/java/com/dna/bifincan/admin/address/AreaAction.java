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
import com.dna.bifincan.model.address.Area;
import com.dna.bifincan.model.address.County;
import com.dna.bifincan.model.address.District;
import com.dna.bifincan.service.AddressService;

@Named("areaAction")
@Scope(ScopeType.VIEW)
public class AreaAction implements Serializable {
	private static final long serialVersionUID = -734237480156554676L;
	private static final Logger log = LoggerFactory.getLogger(AreaAction.class);

	private LazyDataModel<Area> dataModel;
    private Area currentArea;
    private DataTable dataTable;
    private List<District> allDistricts = null;
    
    @Inject
    AddressService addressService;

    @PostConstruct
    public void initialize() {
    	this.dataModel = new AreaDataModel(addressService);
    	if(this.allDistricts == null) this.allDistricts = addressService.getDistricts();    	
    }

    public void initAdd() {
        this.currentArea = new Area();
    }

    public void initEdit(Area area) {
        this.currentArea = area;
    }

    public void save() {
    	try {
    		Long id = this.currentArea.getId();
	        if(this.addressService.saveArea(this.currentArea)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"area.error.duplicateName"));
	        }
	        
	        this.currentArea = null;
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
	        if(this.addressService.deleteArea(this.currentArea)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"area.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    

		initialize();
    	this.dataTable.setFirst(0);
    }
    
    /**
     * This method is used automatically by the drop-down box for selecting districts
     * @param query
     * @return
     */
    public List<District> completeDistrict(String query) {  
        List<District> results = new ArrayList<District>();  
        
        if(query.length() > 0) {
	        for(District tempDistrict : this.allDistricts) {  
	            if(tempDistrict.getName().contains(query)) {
	            	results.add(tempDistrict);  
	            }  	
	        }  
        }  
        
        return results;  
    } 
    
    // --- Getters and Setters --- //
    public LazyDataModel<Area> getDataModel() {
    	return this.dataModel; 
    }
    
    public Area getCurrentArea() {
        return currentArea;
    }

    public void setCurrentArea(Area currentArea) {
        this.currentArea = currentArea;
    }
    
	public DataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}    
}
