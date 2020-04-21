package com.dna.bifincan.admin.miscellaneous;

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
import com.dna.bifincan.model.gsm.GsmOperator;
import com.dna.bifincan.model.gsm.GsmPrefix;
import com.dna.bifincan.service.GSMService;

@Named("gsmPrefixAction")
@Scope(ScopeType.VIEW)
public class GsmPrefixAction implements Serializable {
	private static final long serialVersionUID = 7291769485486248630L;

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(GsmPrefixAction.class);

    private List<GsmPrefix> prefixes;
    private GsmPrefix currentPrefix;
    
    @Inject
    GSMService gsmService;

    public GsmPrefixAction() { }

    @PostConstruct
    public void initialize() {
    	loadPrefixes();
    }
    
    // --- Action methods and listeners --- //
    public void loadPrefixes() {
    	try {
    		this.prefixes = gsmService.getGsmPrefixs();
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    }

    public void initAdd() {
        this.currentPrefix = new GsmPrefix();
    }

    public void initEdit(GsmPrefix prefix) {
        this.currentPrefix = prefix;
    }

    public void save() {
    	try {
    		Long id = this.currentPrefix.getId();
	        if(this.gsmService.saveGsmPrefix(this.currentPrefix)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"gsmPrefix.error.duplicateName"));
	        }
	        
	        this.currentPrefix = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    	
    	loadPrefixes();    
    }
    
    public void delete() {
    	try {
	        if(this.gsmService.deleteGsmPrefix(this.currentPrefix)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"gsmPrefix.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    
		
		loadPrefixes();
    }
    
    // --- Getters and Setters --- //
    public List<GsmPrefix> getPrefixes() {
        return this.prefixes;
    }
    
    public void setPrefixes(List<GsmPrefix> prefixes) {
		this.prefixes = prefixes;
	}

	public GsmPrefix getCurrentPrefix() {
		return currentPrefix;
	}

	public void setCurrentPrefix(GsmPrefix currentPrefix) {
		this.currentPrefix = currentPrefix;
	}
}
