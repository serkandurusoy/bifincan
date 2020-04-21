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
import com.dna.bifincan.model.products.ProductCategoryLevelOne;
import com.dna.bifincan.model.products.ProductCategoryLevelThree;
import com.dna.bifincan.service.ProductCategoryService;

@Named("prodCatLevel3Action")
@Scope(ScopeType.VIEW)
public class ProductCategoryLevelThreeAction implements Serializable {
	private static final long serialVersionUID = -3079042952095667041L;

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ProductCategoryLevelThreeAction.class);

    private List<ProductCategoryLevelThree> categories;
    private ProductCategoryLevelThree currentCategory;
    
    @Inject
    private ProductCategoryService productCategoryService;

    public ProductCategoryLevelThreeAction() { }

    @PostConstruct
    public void initialize() {
    	loadCategories();
    }
    
    // --- Action methods and listeners --- //
    public void loadCategories() {
    	try {
    		this.categories = productCategoryService.findProductCategoriesLevelThree();   
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    }

    public void initAdd() {
        this.currentCategory = new ProductCategoryLevelThree();
    }

    public void initEdit(ProductCategoryLevelThree category) {
        this.currentCategory = category;
    }

    public void save() {
    	try {
    		Long id = this.currentCategory.getId();
	        if(this.productCategoryService.saveProductCategoryLevelThree(this.currentCategory)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"prodCatLevel3.error.duplicateName"));
	        }
	        
	        this.currentCategory = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    	
    	loadCategories();    
    }
    
    public void delete() {
    	try {
	        if(this.productCategoryService.deleteProductCategoryLevelThree(this.currentCategory)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"prodCatLevel3.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    
		
		loadCategories();
    }
    
    // --- Getters and Setters --- //
	public List<ProductCategoryLevelThree> getCategories() {
		return categories;
	}

	public void setCategories(List<ProductCategoryLevelThree> categories) {
		this.categories = categories;
	}

	public ProductCategoryLevelThree getCurrentCategory() {
		return currentCategory;
	}

	public void setCurrentCategory(ProductCategoryLevelThree currentCategory) {
		this.currentCategory = currentCategory;
	}
}
