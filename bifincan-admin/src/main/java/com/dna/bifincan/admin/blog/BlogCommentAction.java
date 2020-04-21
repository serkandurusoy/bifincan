package com.dna.bifincan.admin.blog;

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
import com.dna.bifincan.model.blog.BlogComment;
import com.dna.bifincan.service.BlogService;

@Named("blogCommentAction")
@Scope(ScopeType.VIEW)
public class BlogCommentAction implements Serializable {
    private static final long serialVersionUID = -734237480156554676L;
    private static final Logger log = LoggerFactory.getLogger(BlogCommentAction.class);

    private LazyDataModel<BlogComment> dataModel;
    private BlogComment currentComment;
    private DataTable dataTable;

    @Inject
    private BlogService blogService;
    
    @PostConstruct
    public void initialize() {
        this.dataModel = new BlogCommentDataModel(blogService);
    }

    public void initAdd() {
        this.currentComment = new BlogComment();
    }

    public void initEdit(BlogComment comment) {
        this.currentComment = comment;
    }

    public void save() {
    	try {
    		Long id = this.currentComment.getId();
	        if(this.blogService.saveBlogComment(this.currentComment)) { // success 
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
    public BlogComment getCurrentComment() {
        return currentComment;
    }

    public void setCurrentComment(BlogComment currentComment) {
        this.currentComment = currentComment;
    }
    
    public LazyDataModel<BlogComment> getDataModel() {
        return this.dataModel;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }    
}
