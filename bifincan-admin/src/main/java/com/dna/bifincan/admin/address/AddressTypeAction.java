package com.dna.bifincan.admin.address;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.model.address.AddressType;
import com.dna.bifincan.model.address.GlobalRegion;
import com.dna.bifincan.service.AddressService;
import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;
import com.dna.bifincan.admin.util.spring.ScopeType;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Named("addressTypeAction")
@Scope(ScopeType.VIEW)
public class AddressTypeAction implements Serializable {
	private static final long serialVersionUID = -109866748149956348L;
	private static final Logger log = LoggerFactory.getLogger(AddressTypeAction.class);

    private List<AddressType> types;
    private AddressType currentAddressType;

    @Inject
    private AddressService addressService;

    public AddressTypeAction() { }

    @PostConstruct
    public void initialize() {
    	loadTypes();
    }
    
    // --- Action methods and listeners --- //
    public void loadTypes() {
    	try {
    		this.types = addressService.getAddressTypes();
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    }

    public void initAdd() {
        this.currentAddressType = new AddressType();
    }

    public void initEdit(AddressType type) {
        this.currentAddressType = type;
    }   
    
    public void save() {
    	try {
    		Long id = this.currentAddressType.getId();
	        if(this.addressService.saveAddressType(this.currentAddressType)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"addressType.error.duplicateName"));
	        }
	        
	        this.currentAddressType = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    	
    	loadTypes();    
    }
    
    public void delete() {
    	try {
	        if(this.addressService.deleteAddressType(this.currentAddressType)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"addressType.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    
		
		loadTypes();
    }

    // --- Getters and Setters --- //   
    public AddressType getCurrentAddressType() {
        return currentAddressType;
    }

    public void setCurrentAddressType(AddressType currentAddressType) {
        this.currentAddressType = currentAddressType;
    }

	public List<AddressType> getTypes() {
		return types;
	}

	public void setTypes(List<AddressType> types) {
		this.types = types;
	} 
    
}
