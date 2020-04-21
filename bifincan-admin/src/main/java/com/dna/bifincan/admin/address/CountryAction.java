package com.dna.bifincan.admin.address;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.model.address.Country;
import com.dna.bifincan.model.address.GlobalRegion;
import com.dna.bifincan.service.AddressService;
import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;
import com.dna.bifincan.admin.util.spring.ScopeType;
import com.dna.bifincan.model.address.Country;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;

@Named("countryAction")
@Scope(ScopeType.VIEW)
public class CountryAction implements Serializable {
	private static final long serialVersionUID = 2213143053101309957L;
	private static final Logger log = LoggerFactory.getLogger(CountryAction.class);

    private List<Country> countries;
    private Country currentCountry;
    
    @Inject
    AddressService addressService;

    public CountryAction() { }
    
    @PostConstruct
    public void initialize() {
    	loadCountries();
    }
    
    // --- Action methods and listeners --- //
    public void loadCountries() {
    	try {
    		this.countries = addressService.findCountries();
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    }    
    
    public void initAdd() {
        this.currentCountry = new Country();
    }

    public void initEdit(Country country) {
        this.currentCountry = country;
    }
    
    public void save() {
    	try {
    		Long id = this.currentCountry.getId();
	        if(this.addressService.saveCountry(this.currentCountry)) { // success
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"country.error.duplicateName"));
	        }
	        
	        this.currentCountry = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    	
	    loadCountries();    
    }
    
    public void delete() {
    	try {
	        if(this.addressService.deleteCountry(this.currentCountry)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"country.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    
		
		loadCountries();
    }
    
    // --- Getters and Setters --- //
    
    public Country getCurrentCountry() {
        return currentCountry;
    }

    public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public void setCurrentCountry(Country currentCountry) {
        this.currentCountry = currentCountry;
    }    
}
