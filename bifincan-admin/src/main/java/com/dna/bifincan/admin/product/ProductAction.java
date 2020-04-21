package com.dna.bifincan.admin.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;
import com.dna.bifincan.admin.util.spring.ScopeType;
import com.dna.bifincan.model.products.Image;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.type.ProductClassifier;
import com.dna.bifincan.service.ProductService;

@Named("productAction")
@Scope(ScopeType.VIEW)
public class ProductAction implements Serializable {
	private static final long serialVersionUID = -1478817084700995842L;

	private static final Logger log = LoggerFactory.getLogger(ProductAction.class);

	private LazyDataModel<Product> dataModel;
    private Product currentProduct;
    private DataTable dataTable;
    private FileItem imageDetail;
    private FileItem imageLarge;
    private FileItem imageSmall;
    		
    @Inject
    private ProductService productService;

    @PostConstruct
    public void initialize() {
    	this.dataModel = new ProductDataModel(productService);
    }

    public void initAdd() {
        this.currentProduct = new Product();
    }

    public void initEdit(Product product) {
        this.currentProduct = product;
    }
    
    public void save() {
    	try {
    		Long id = this.currentProduct.getId();
   		
    	    Image imgDetail = (imageDetail != null) ? new Image(IOUtils.toByteArray(imageDetail.getInputStream())) : null;   		
    	    Image imgLarge = (imageLarge != null) ? new Image(IOUtils.toByteArray(imageLarge.getInputStream())) : null;   		
    	    Image imgSmall = (imageSmall != null) ? new Image(IOUtils.toByteArray(imageSmall.getInputStream())) : null;
    	    
	        if(this.productService.saveProduct(this.currentProduct, imgDetail, imgLarge, imgSmall)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"product.error.duplicateFields"));  
	        }
	        
	        this.currentProduct = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}  
    	
    	initialize();
    	this.dataTable.setFirst(0);
    }
   
    public void delete() {
    	try {
	        if(this.productService.deleteProduct(this.currentProduct)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"product.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    

		initialize();
    	this.dataTable.setFirst(0);
    } 
    
    // --- Getters and Setters --- //
    public LazyDataModel<Product> getDataModel() {
    	return this.dataModel; 
    }
    
    public Product getCurrentProduct() {
        return currentProduct;
    }
    
	public void setCurrentProduct(Product currentProduct) {
        this.currentProduct = currentProduct;
    }
	
	public DataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}

	public FileItem getImageDetail() {
		return imageDetail;
	}

	public void setImageDetail(FileItem imageDetail) {
		this.imageDetail = imageDetail;
	}

	public FileItem getImageLarge() {
		return imageLarge;
	}

	public void setImageLarge(FileItem imageLarge) {
		this.imageLarge = imageLarge;
	}

	public FileItem getImageSmall() {
		return imageSmall;
	}

	public void setImageSmall(FileItem imageSmall) {
		this.imageSmall = imageSmall;
	}	
}
