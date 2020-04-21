package com.dna.bifincan.admin.address;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.model.address.City;
import com.dna.bifincan.model.address.LocalRegion;
import com.dna.bifincan.service.AddressService;
import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;
import com.dna.bifincan.admin.util.spring.ScopeType;
import com.dna.bifincan.model.address.City;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;

@Named("cityAction")
@Scope(ScopeType.VIEW)
public class CityAction implements Serializable {
	private static final long serialVersionUID = 6298975127540927726L;
	private static final Logger log = LoggerFactory.getLogger(CityAction.class);

    private LazyDataModel<City> dataModel;
    private City currentCity;
    private DataTable dataTable;
    
    @Inject
    AddressService addressService;

    @PostConstruct
    public void initialize() {
    	this.dataModel = new CityDataModel(addressService);
    }
    
    public void initAdd() {
        this.currentCity = new City();
    }

    public void initEdit(City city) {
        this.currentCity = city;
    }
    
    public void save() {
    	try {
    		Long id = this.currentCity.getId();
	        if(this.addressService.saveCity(this.currentCity)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"city.error.duplicateName"));
	        }
	        
	        this.currentCity = null;
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
	        if(this.addressService.deleteCity(this.currentCity)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"city.error.inUse"));
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
    public LazyDataModel<City> getDataModel() {
    	return this.dataModel; 
    }
    
    public City getCurrentCity() {
        return currentCity;
    }
    
	public void setCurrentCity(City currentCity) {
        this.currentCity = currentCity;
    }
	
	public DataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}
}
