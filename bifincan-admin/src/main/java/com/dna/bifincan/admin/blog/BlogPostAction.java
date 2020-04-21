package com.dna.bifincan.admin.blog;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;

import java.io.Serializable;

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

import com.dna.bifincan.admin.util.spring.ScopeType;
import com.dna.bifincan.model.blog.BlogPost;
import com.dna.bifincan.model.products.Image;
import com.dna.bifincan.service.BlogService;

@Named("blogPostAction")
@Scope(ScopeType.VIEW)
public class BlogPostAction implements Serializable {
    private static final long serialVersionUID = -734237480156554676L;
    @SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(BlogPostAction.class);

    private LazyDataModel<BlogPost> dataModel;
    private BlogPost currentPost;
    private DataTable dataTable;
    private FileItem imageLarge;
    private FileItem imageSmall;
    
    @Inject
    private BlogService blogService;

    @PostConstruct
    public void initialize() {
        this.dataModel = new BlogPostDataModel(blogService);
    }

    public void initAdd() {
        this.currentPost = new BlogPost();
    }

    public void initEdit(BlogPost post) {
        this.currentPost = post;
    }

    public void save() {
    	try {
    		Long id = this.currentPost.getId();
    		
    	    Image imgLarge = (imageLarge != null) ? new Image(IOUtils.toByteArray(imageLarge.getInputStream())) : null;   		
    	    Image imgSmall = (imageSmall != null) ? new Image(IOUtils.toByteArray(imageSmall.getInputStream())) : null;
    	    
	        if(this.blogService.saveBlogPost(this.currentPost, imgLarge, imgSmall)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } /* else { // failure   // not used at the moment 
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"????"));
	        }*/
	        
	        this.currentPost = null;
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
	        if(this.blogService.deleteBlogPost(this.currentPost)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } /*else { // failure (due to dependencies)  // not used at the moment
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"city.error.inUse"));
	        }*/
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    

		initialize();
    	this.dataTable.setFirst(0);        
    }
    
    
    // --- Getters and Setters --- //
    public LazyDataModel<BlogPost> getDataModel() {
        return this.dataModel;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }
    
    public BlogPost getCurrentPost() {
        return currentPost;
    }

    public void setCurrentPost(BlogPost currentPost) {
        this.currentPost = currentPost;
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
