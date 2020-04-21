package com.dna.bifincan.admin.miscellaneous;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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
import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.user.AdminUser;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.common.util.Constants;

@Named("configAction")
@Scope(ScopeType.VIEW)
public class ConfigurationAction implements Serializable {
	private static final long serialVersionUID = 8471619200776313226L;

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ConfigurationAction.class);

	private static final String CANONICAL_URL_KEY = "applicationCanonicalURL";
	
    private List<Configuration> configurations;
    private Configuration currentConfig;
    
    @Inject
    private ConfigurationService configurationService;

    public ConfigurationAction() { }

    @PostConstruct
    public void initialize() {
    	loadConfigurations();
    }
    
    // --- Action methods and listeners --- //
    public void loadConfigurations() {
    	try {
    		this.configurations = configurationService.getParameters();
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    }

    public void initAdd() {
        this.currentConfig = new Configuration();
    }

    public void initEdit(Configuration config) {
        this.currentConfig = config;
    }

    public void save() {
    	try {
	        if(this.configurationService.saveOrUpdate(this.currentConfig) != null) { // success
	        	String canonicalURL = configurationService.find(CANONICAL_URL_KEY).getValue();
	        	
	        //	AdminUser currentUser = (AdminUser)FacesUtils.getExternalContext().getSessionMap().get(WebConstants.CURRENT_USER_SESSION_KEY);
	        	 
	        //	String url = canonicalURL + "/fi/spring/config?reload="+ Constants.RELOAD_CACHE_KEY +"&usr=" + 
	        //			currentUser.getUsername();
	        //	InputStream response = new URL(url).openStream();

	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"config.error"));
	        }
	        
	        this.currentConfig = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    	
    	loadConfigurations();    
    }
    
    // --- Getters and Setters --- //
	public List<Configuration> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<Configuration> configurations) {
		this.configurations = configurations;
	}

	public Configuration getCurrentConfig() {
		return currentConfig;
	}

	public void setCurrentConfig(Configuration currentConfig) {
		this.currentConfig = currentConfig;
	}
    
}
