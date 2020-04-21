package com.dna.bifincan.admin.rating;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;
import com.dna.bifincan.admin.util.spring.ScopeType;
import com.dna.bifincan.model.blog.BlogComment;
import com.dna.bifincan.model.rating.Rating;
import com.dna.bifincan.service.RatingService;

@Named("ratingAction")
@Scope(ScopeType.VIEW)
public class RatingAction implements Serializable {
	private static final long serialVersionUID = -3302075439388788166L;

	private static final Logger log = LoggerFactory.getLogger(RatingAction.class);

    private LazyDataModel<Rating> dataModel;
    private Rating currentRating;
    private DataTable dataTable;

    @Inject
    private RatingService ratingService;
    
    @PostConstruct
    public void initialize() {
        this.dataModel = new RatingDataModel(ratingService);
    }

    public void initAdd() {
        this.currentRating = new Rating();  
    }

    public void initEdit(Rating rating) {
        this.currentRating = rating;
    }

    public void save() {
    	try {
    		Long id = this.currentRating.getId();
	        if(this.ratingService.saveRating(this.currentRating)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } /* else { // failure   // not used at the moment 
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"????"));
	        }*/
	        
	        this.currentRating = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}  
    	
    	initialize();
    	this.dataTable.setFirst(0);          
    }

    // --- Getters and Setters --- //
    public Rating getCurrentRating() {
        return currentRating;
    }

    public void setCurrentRating(Rating rating) {
        this.currentRating = rating;
    }
    
    public LazyDataModel<Rating> getDataModel() {
        return this.dataModel;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }    
}
