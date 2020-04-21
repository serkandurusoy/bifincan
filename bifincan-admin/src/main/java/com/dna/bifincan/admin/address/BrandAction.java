package com.dna.bifincan.admin.address;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.DualListModel;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;
import com.dna.bifincan.admin.util.spring.ScopeType;
import com.dna.bifincan.model.products.Brand;
import com.dna.bifincan.model.products.Image;
import com.dna.bifincan.model.products.ProductCategoryLevelThree;
import com.dna.bifincan.service.AddressService;
import com.dna.bifincan.service.BrandService;
import com.dna.bifincan.service.ProductCategoryService;

@Named("brandAction")
@Scope(ScopeType.VIEW)
public class BrandAction implements Serializable {
	private static final long serialVersionUID = 4826947531396955834L;
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(BrandAction.class);

    private List<Brand> brands;
    private Brand currentBrand;
    private FileItem file;
    private DualListModel<ProductCategoryLevelThree> productCategories = null;  
    
    @Inject
    private BrandService brandService;    
    @Inject
    private AddressService addressService;
    @Inject
    private ProductCategoryService productCategoryService;
    
    public BrandAction() { }

    @PostConstruct
    public void initialize() {
    	initProductCatetegories();
    	
    	loadBrands();
    }
    
    // --- Action methods and listeners --- //
    public void loadBrands() {
    	try {
    		this.brands = brandService.getBrands();
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    }

    public void initAdd() {
        this.currentBrand = new Brand();
        
        initProductCatetegories();
        loadProductCategories();
    }

    public void initEdit(Brand brand) {
        this.currentBrand = this.brandService.findBrandById(brand.getId());
        
        initProductCatetegories();
        loadProductCategories();
    }
    
    public void save() {
    	try {
    		Long id = this.currentBrand.getId();

    	    //Image image = new Image(logo.getContents());    		
    	    Image image = new Image(IOUtils.toByteArray(file.getInputStream()));
    	    
    	    // assign the selected product categories (level three)
    	    this.currentBrand.setCategories(this.productCategories.getTarget());
    	    
	        if(this.brandService.saveBrand(this.currentBrand, image)) { // success  
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"brand.error.duplicateName"));
	        }
	        
	        this.currentBrand = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    	
	    loadBrands();    
    }
    
    public void delete() {
    	
    	try {
	        if(this.brandService.deleteBrand(this.currentBrand)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"brand.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    
		
        loadBrands();
    }
      
    // --- Helper methods --- //
    private void initProductCatetegories() {
    	if(this.productCategories == null) {
    		this.productCategories = new DualListModel<ProductCategoryLevelThree>(null, null);
    	} 
    	List<ProductCategoryLevelThree> source = this.productCategories.getSource();
    	List<ProductCategoryLevelThree> target = this.productCategories.getTarget();
    	if(source == null) {
    		this.productCategories.setSource(new ArrayList<ProductCategoryLevelThree>());
    	} else {
    		this.productCategories.getSource().clear();
    	}
    	if(target == null) {
    		this.productCategories.setTarget(new ArrayList<ProductCategoryLevelThree>());
    	} else {
    		this.productCategories.getTarget().clear();
    	}
    }
    
    private void loadProductCategories() {
    	List<ProductCategoryLevelThree> source = productCategoryService.findProductCategoriesLevelThree();   
 	
    	List<ProductCategoryLevelThree> target = null;
    
    	if(this.currentBrand != null && this.currentBrand.getCategories() != null) { 
    		target = this.currentBrand.getCategories();
    		source.removeAll(target);
    	} else {
    		target = new ArrayList<ProductCategoryLevelThree>();
    	}
    	 
    	this.productCategories.setSource(source);
    	this.productCategories.setTarget(target);
    }
    
    // --- Getters and Setters --- //

	public List<Brand> getBrands() {
		return brands;
	}

	public void setBrands(List<Brand> brands) {
		this.brands = brands;
	}

	public Brand getCurrentBrand() {
		return currentBrand;
	}

	public void setCurrentBrand(Brand currentBrand) {
		this.currentBrand = currentBrand;
	}

	public FileItem getFile() {
		return file;
	}

	public void setFile(FileItem file) {
		this.file = file;
	}

	public DualListModel<ProductCategoryLevelThree> getProductCategories() {
		return productCategories;
	}

	public void setProductCategories(
			DualListModel<ProductCategoryLevelThree> productCategories) {
		this.productCategories = productCategories;
	}
	
}
