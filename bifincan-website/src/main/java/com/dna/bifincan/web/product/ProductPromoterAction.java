package com.dna.bifincan.web.product;

import java.io.Serializable;
import java.security.Principal;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.exception.NoProductPromoterException;
import com.dna.bifincan.model.dto.ProductDTO;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.service.ProductService;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.WebConstants;
import com.dna.bifincan.util.spring.ScopeType;

@Named("productPromoterAction")
@Scope(ScopeType.REQUEST)  // this is our intention, ie. request scope
public class ProductPromoterAction implements Serializable {
	private static final long serialVersionUID = 2843164724885558123L;
    private static final Logger log = LoggerFactory.getLogger(ProductPromoterAction.class);
   
	private ProductDTO product; 
	
    @Inject
    private ProductService productService;
	
    public ProductPromoterAction() { }

    @PostConstruct
    public void initialize() { 
    	fetchRandomProductPromoter();
    }

    private void fetchRandomProductPromoter() {
    	Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
    	String username = principal.getName();
    			
    	this.product = null; // reset the current product (normally it should be already null)
    	//try {
    		Product foundProduct = productService.fetchRandomProductPromoter(username);
    		if(foundProduct !=  null) {
    			product = new ProductDTO();
    			
    			product.setBrandName(foundProduct.getBrand().getName());
    			product.setProductSlug(foundProduct.getSlug());
    			product.setProductName(foundProduct.getName());
    			product.setProductPointValue(foundProduct.getPricePoints());
    			product.setClassifier(foundProduct.getClassifier().toString());
    			
    		} 
    	//} catch(NoProductPromoterException ex) { 
    		//log.debug(ex.getMessage());
			//FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
			//		WebConstants.ERROR_NO_PRODUCT_PROMOTER_KEY));	
    	//} 
    }
    
    //--- helper methods ---//
    

    //--- setters & getters ---//
	public ProductDTO getProduct() {
		return product;
	}

	public void setProduct(ProductDTO product) {
		this.product = product;
	}
  
}
