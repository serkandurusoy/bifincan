package com.dna.bifincan.web.product;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.model.dto.ProductDTO;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.service.ProductService;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.spring.ScopeType;

@Named("productOtherListAction")
@Scope(ScopeType.VIEW)
public class ProductOtherListAction implements Serializable {
	private static final long serialVersionUID = -3025693139338156832L;
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ProductOtherListAction.class);
	
	private List<ProductDTO> products;
	
	@Inject
	private UserService userService;
	@Inject
	protected ProductService productService;	
	
	public ProductOtherListAction() { }
	
	@PostConstruct
	public void initialize() { 
		loadProducts();
	}	
	
	/**
	 * This method is used in order to set-up a list of OTHER (non-available) products ready for rendering.
	 */
	public void loadProducts() {
		// 1. find the current user entity from database
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		
		if(principal != null) {
			String username = principal.getName();
			
			User user = userService.findUserByUsername(username);
			
			// 2. fetch the list of products
			// TODO: this list normally should be cached for paging somehow
			List<Product> productRecords = productService.findOtherProducts(user);
			
			fetchProductsAndConstructData(productRecords);
		}
	}

	public void fetchProductsAndConstructData(List<Product> productRecords) {
		setProducts(new ArrayList<ProductDTO>());
		
		if(productRecords != null && !productRecords.isEmpty()) {
			for(Product product : productRecords) {;
				ProductDTO productDTO = new ProductDTO();
				
				productDTO.setId(product.getId());
				productDTO.setBrandName(product.getBrand().getName());
				productDTO.setProductShortDescription(product.getShortDescription());
				productDTO.setProductMoneyValue(product.getPriceMoney());
				productDTO.setProductName(product.getName());
				productDTO.setProductPointValue(product.getPricePoints());
				productDTO.setProductSlug(product.getSlug());
				productDTO.setClassifier(product.getClassifier().toString());
				
				getProducts().add(productDTO);
			}
		}		
	}	
	// -- add other methods here -- /

	// -- setters & getters -- /	
	public List<ProductDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDTO> products) {
		this.products = products;
	}
}
