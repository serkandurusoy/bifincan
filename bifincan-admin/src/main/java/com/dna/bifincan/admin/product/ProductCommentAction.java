package com.dna.bifincan.admin.product;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.admin.util.spring.ScopeType;
import com.dna.bifincan.model.products.ProductComment;
import com.dna.bifincan.service.ProductService;

@Named("productCommentAction")
@Scope(ScopeType.VIEW)
public class ProductCommentAction implements Serializable {
    private static final long serialVersionUID = -734237480156554676L;
    private static final Logger log = LoggerFactory.getLogger(ProductCommentAction.class);

    private LazyDataModel<ProductComment> dataModel;
    private ProductComment currentComment;
    private DataTable dataTable;

    @Inject
    private ProductService productService;
    
    @PostConstruct
    public void initialize() {
        this.dataModel = new ProductCommentDataModel(productService);
    }

    public void initAdd() {
        this.currentComment = new ProductComment();
    }

    public void initEdit(ProductComment comment) {
        this.currentComment = comment;
    }

    public void save() {
    	try {
    		Long id = this.currentComment.getId();
	        if(this.productService.saveProductComment(this.currentComment)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } /* else { // failure   // not used at the moment 
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"????"));
	        }*/
	        
	        this.currentComment = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}  
    	
    	initialize();
    	this.dataTable.setFirst(0);          
    }

    // --- Getters and Setters --- //
    public ProductComment getCurrentComment() {
        return currentComment;
    }

    public void setCurrentComment(ProductComment currentComment) {
        this.currentComment = currentComment;
    }
    
    public LazyDataModel<ProductComment> getDataModel() {
        return this.dataModel;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }    
}
