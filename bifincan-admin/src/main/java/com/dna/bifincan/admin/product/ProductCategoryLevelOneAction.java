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
import com.dna.bifincan.service.ProductCategoryService;

@Named("prodCatLevel1Action")
@Scope(ScopeType.VIEW)
public class ProductCategoryLevelOneAction implements Serializable {
	private static final long serialVersionUID = 5624890051620577172L;

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ProductCategoryLevelOneAction.class);

    private List<ProductCategoryLevelOne> categories;
    private ProductCategoryLevelOne currentCategory;
    
    @Inject
    private ProductCategoryService productCategoryService;

    public ProductCategoryLevelOneAction() { }

    @PostConstruct
    public void initialize() {
    	loadCategories();
    }
    
    // --- Action methods and listeners --- //
    public void loadCategories() {
    	try {
    		this.categories = productCategoryService.findProductCategoriesLevelOne();
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    }

    public void initAdd() {
        this.currentCategory = new ProductCategoryLevelOne();
    }

    public void initEdit(ProductCategoryLevelOne category) {
        this.currentCategory = category;
    }

    public void save() {
    	try {
    		Long id = this.currentCategory.getId();
	        if(this.productCategoryService.saveProductCategoryLevelOne(this.currentCategory)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"prodCatLevel1.error.duplicateName"));
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
	        if(this.productCategoryService.deleteProductCategoryLevelOne(this.currentCategory)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"prodCatLevel1.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    
		
		loadCategories();
    }
    
    // --- Getters and Setters --- //
	public List<ProductCategoryLevelOne> getCategories() {
		return categories;
	}

	public void setCategories(List<ProductCategoryLevelOne> categories) {
		this.categories = categories;
	}

	public ProductCategoryLevelOne getCurrentCategory() {
		return currentCategory;
	}

	public void setCurrentCategory(ProductCategoryLevelOne currentCategory) {
		this.currentCategory = currentCategory;
	}
}
