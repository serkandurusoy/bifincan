package com.dna.bifincan.admin.address;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;
import com.dna.bifincan.admin.util.spring.ScopeType;
import com.dna.bifincan.model.address.GlobalRegion;
import com.dna.bifincan.service.AddressService;

@Named("globalRegionAction")
@Scope(ScopeType.VIEW)
public class GlobalRegionAction implements Serializable {
	private static final long serialVersionUID = 4203440499554812297L;
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(GlobalRegionAction.class);

    private List<GlobalRegion> regions;
    private GlobalRegion currentGlobalRegion;
    
    @Inject
    AddressService addressService;

    public GlobalRegionAction() { }

    @PostConstruct
    public void initialize() {
    	loadRegions();
    }
    
    // --- Action methods and listeners --- //
    public void loadRegions() {
    	try {
    		this.regions = addressService.getGlobalRegions();
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    }

    public void initAdd() {
        this.currentGlobalRegion = new GlobalRegion();
    }

    public void initEdit(GlobalRegion region) {
        this.currentGlobalRegion = region;
    }

    public void save() {
    	try {
    		Long id = this.currentGlobalRegion.getId();
	        if(this.addressService.saveGlobalRegion(this.currentGlobalRegion)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"globalRegion.error.duplicateName"));
	        }
	        
	        this.currentGlobalRegion = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    	
	    loadRegions();    
    }
    
    public void delete() {
    	try {
	        if(this.addressService.deleteGlobalRegion(this.currentGlobalRegion)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"globalRegion.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    
		
        loadRegions();
    }
    
    // --- Getters and Setters --- //
    public List<GlobalRegion> getRegions() {
        return this.regions;
    }
    
    public void setRegions(List<GlobalRegion> regions) {
		this.regions = regions;
	}

	public GlobalRegion getCurrentGlobalRegion() {
        return currentGlobalRegion;
    }

    public void setCurrentGlobalRegion(GlobalRegion currentGlobalRegion) {
        this.currentGlobalRegion = currentGlobalRegion;
    }
}
