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
import com.dna.bifincan.model.products.ProductCategoryLevelTwo;
import com.dna.bifincan.service.ProductCategoryService;

@Named("prodCatLevel2Action")
@Scope(ScopeType.VIEW)
public class ProductCategoryLevelTwoAction implements Serializable {
	private static final long serialVersionUID = 272512975172308459L;

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ProductCategoryLevelTwoAction.class);

    private List<ProductCategoryLevelTwo> categories;
    private ProductCategoryLevelTwo currentCategory;
    
    @Inject
    private ProductCategoryService productCategoryService;

    public ProductCategoryLevelTwoAction() { }

    @PostConstruct
    public void initialize() {
    	loadCategories();
    }
    
    // --- Action methods and listeners --- //
    public void loadCategories() {
    	try {
    		this.categories = productCategoryService.findProductCategoriesLevelTwo();   
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    }

    public void initAdd() {
        this.currentCategory = new ProductCategoryLevelTwo();
    }

    public void initEdit(ProductCategoryLevelTwo category) {
        this.currentCategory = category;
    }

    public void save() {
    	try {
    		Long id = this.currentCategory.getId();
	        if(this.productCategoryService.saveProductCategoryLevelTwo(this.currentCategory)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"prodCatLevel2.error.duplicateName"));
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
	        if(this.productCategoryService.deleteProductCategoryLevelTwo(this.currentCategory)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"prodCatLevel2.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    
		
		loadCategories();
    }
    
    // --- Getters and Setters --- //
	public List<ProductCategoryLevelTwo> getCategories() {
		return categories;
	}

	public void setCategories(List<ProductCategoryLevelTwo> categories) {
		this.categories = categories;
	}

	public ProductCategoryLevelTwo getCurrentCategory() {
		return currentCategory;
	}

	public void setCurrentCategory(ProductCategoryLevelTwo currentCategory) {
		this.currentCategory = currentCategory;
	}
}
