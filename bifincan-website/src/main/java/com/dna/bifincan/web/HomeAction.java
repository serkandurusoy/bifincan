/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.web;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.model.dto.HomeProductDTO;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.service.ProductService;
import com.dna.bifincan.util.WebConstants;
import com.dna.bifincan.util.spring.ScopeType;


@Named("homeAction")
@Scope(ScopeType.VIEW)
public class HomeAction implements Serializable {
	private static final long serialVersionUID = -5102677191004399249L;

	private static final Logger log = LoggerFactory.getLogger(HomeAction.class);

    private String redirectTo = "/index.xhtml";
    private HomeProductDTO product;
    
    @Inject
    private ProductService productService;
    
	@PostConstruct
	public void initialize() {
		//log.debug(">>> initializing the HomeAction.....");
		loadRandomProduct();
	}	
    
	public void loadRandomProduct() {
		this.product = productService.fetchRandomHomeProduct();
		
		/*
		if(product != null) {
			log.debug(">>> Random home product found:\n");
			
			log.debug(">>> id = " + product.getId());
			log.debug(">>> brandName = " + product.getBrandName());
			log.debug(">>> optionText = " + product.getOptionText());
			log.debug(">>> productName = " + product.getProductName());
			log.debug(">>> shortDescription = " + product.getProductShortDescription());
			log.debug(">>> productSlug = " + product.getProductSlug());
			log.debug(">>> questionText = " + product.getQuestionText());	
			log.debug(">>> percentage = " + product.getPercentage());			
		} else {
			log.debug(">>> No product found:\n");
		}
		*/

	}
	
	public HomeProductDTO getProduct() {
		return product;
	}

	public void setProduct(HomeProductDTO product) {
		this.product = product;
	}
	
	
    // -------------------------------- //

	// Note: seem not to be used anywhere in the application (probably remove them)
    public String detectSession() {
        Object currentUser = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(WebConstants.CURRENT_USER_SESSION_KEY);
        log.debug("@@@@@@@@currentUser@" + (currentUser != null));
        if (currentUser != null) {
            this.redirectTo = "/user/home.xhtml";
        }
        return this.redirectTo;
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }
    
}
