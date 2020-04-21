package com.dna.bifincan.admin.product;

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
import com.dna.bifincan.model.address.Country;
import com.dna.bifincan.model.products.ProductPriceSource;
import com.dna.bifincan.service.ProductService;

@Named("priceSourceAction")
@Scope(ScopeType.VIEW)
public class ProductPriceSourceAction implements Serializable {
	private static final long serialVersionUID = -1471264384815178645L;

	private static final Logger log = LoggerFactory.getLogger(ProductPriceSourceAction.class);

    private List<ProductPriceSource> sources;
    private ProductPriceSource currentPriceSource;
    
    @Inject
    private ProductService productService;

    public ProductPriceSourceAction() { }
    
    @PostConstruct
    public void initialize() {
    	loadProductPriceSources();
    }
    
    // --- Action methods and listeners --- //
    public void loadProductPriceSources() {
    	try {
    		this.sources = productService.findProductPriceSources();
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    }    
    
    public void initAdd() {
        this.currentPriceSource = new ProductPriceSource();
    }

    public void initEdit(ProductPriceSource priceSource) {
        this.currentPriceSource = priceSource;
    }
    
    public void save() {
    	try {
    		Long id = this.currentPriceSource.getId();
	        if(this.productService.saveProductPriceSource(this.currentPriceSource)) { // success
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"priceSource.error.duplicateName"));
	        }
	        
	        this.currentPriceSource = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    	
    	loadProductPriceSources();    
    }
    
    public void delete() {
    	try {
	        if(this.productService.deleteProductPriceSource(this.currentPriceSource)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"priceSource.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    
		
		loadProductPriceSources();
    }
    
    // --- Getters and Setters --- //
    
    public ProductPriceSource getCurrentPriceSource() {
        return currentPriceSource;
    }

	public void setCurrentPriceSource(ProductPriceSource currentPriceSource) {
        this.currentPriceSource = currentPriceSource;
    } 
	
    public List<ProductPriceSource> getSources() {
		return sources;
	}

	public void setSources(List<ProductPriceSource> sources) {
		this.sources = sources;
	}
}
