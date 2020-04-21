package com.dna.bifincan.web.product;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.dto.ProductDTO;
import com.dna.bifincan.model.order.ShoppingCart;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.OrderService;
import com.dna.bifincan.service.ProductService;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.WebConstants;
import com.dna.bifincan.util.spring.ScopeType;

@Named("productAvailListAction")
@Scope(ScopeType.VIEW)
public class ProductAvailableListAction implements Serializable {
	private static final long serialVersionUID = 8395926251323648616L;
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ProductAvailableListAction.class);
	
	private List<ProductDTO> products;    
    
	@Inject
	private UserService userService;
	@Inject
	protected ProductService productService;	
	@Inject
	protected OrderService orderService;	
	@Inject 
	protected ConfigurationService configurationService;
	
	public ProductAvailableListAction() { }
	
	@PostConstruct
	public void initialize() { 
		loadProducts();
	}	
	
	/**
	 * This method is used in order to set-up a list of AVAILABLE products ready for rendering.
	 */
	public void loadProducts() {
		// find the current user entity from database
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		
		if(principal != null) {
			String username = principal.getName();
			
			User user = userService.findUserByUsername(username);
			
			// fetch the list of products VALIDATED also upon user's criteria
			// TODO: this list normally should be cached for paging somehow
			List<Product> productRecords = productService.findAvailableProducts(user);
			
			// fetch data
			fetchProductsAndConstructData(user, productRecords);
	
		}
	}

	private void fetchProductsAndConstructData(User user, List<Product> productRecords) {
		setProducts(new ArrayList<ProductDTO>());
		
		if(productRecords != null && !productRecords.isEmpty()) {
			Map<String, Object> values = FacesUtils.getExternalContext().getSessionMap();		

			User userToken = (User)values.get(WebConstants.CURRENT_USER_SESSION_KEY);
			ShoppingCart userShoppingCart = userToken != null ? userToken.getShoppingCart() : null;
			
			int maxNoOfOrderableProducts = 0;
			Configuration config = configurationService.find(ConfigurationType.MAX_NO_OF_ORDERABLE_PRODUCTS.getKey());
			if(config != null) maxNoOfOrderableProducts = Integer.parseInt(config.getValue());
			
			Configuration bounusOrderableConfig = configurationService.find(ConfigurationType.NO_OF_BONUS_ORDERABLE_PRODUCTS.getKey());
            int numberOfBonusOrderableProducts = Integer.parseInt(bounusOrderableConfig.getValue());
            
            if(user.getActivityLevel() == WebConstants.HIGH_USER_ACTIVITY)  maxNoOfOrderableProducts += numberOfBonusOrderableProducts;
            
			// CHECK whether the user can place a new order  (order scope attributes)
			boolean canUserOrder = orderService.canUserPlaceNewOrder(user);
			
			for(Product product : productRecords) {
				ProductDTO productDTO = new ProductDTO();
			
				productDTO.setId(product.getId());
				productDTO.setBrandName(product.getBrand().getName());
				productDTO.setProductShortDescription(product.getShortDescription());
				productDTO.setProductMoneyValue(product.getPriceMoney());
				productDTO.setProductName(product.getName());
				productDTO.setProductPointValue(product.getPricePoints());
				productDTO.setProductSlug(product.getSlug());
				productDTO.setClassifier(product.getClassifier().toString());
				
				if(user.getActivityLevel() == WebConstants.LOW_USER_ACTIVITY) { // low lever activity
					productDTO.setAvailable(false);
				} else if(userShoppingCart != null && userShoppingCart.getProducts().contains(product.getSlug())) {
					productDTO.setRemovable(true);
					productDTO.setAvailable(false);
				} else if(userShoppingCart != null && userShoppingCart.getProducts().size() >= maxNoOfOrderableProducts) {
					productDTO.setAvailable(false);	
				} else {  // this concerns generic (order scope) criteria
					productDTO.setAvailable(canUserOrder);
				} 
				
				getProducts().add(productDTO);
			}
		}		
	}	

	/**
	 * This method is used to add a product (slug) into the cart's cache
	 * @return String an outcome for the navigation handler
	 */
	public String addProductSlug() {
		Flash flash = FacesUtils.getFlash();
		String selProductSlug = (String)flash.get(WebConstants.PRODUCT_SLUG);
		
		if(selProductSlug != null) {
			Map<String, Object> values = FacesUtils.getExternalContext().getSessionMap();			
			User user = (User)values.get(WebConstants.CURRENT_USER_SESSION_KEY);
			
			if(user != null) {
				user.getShoppingCart().addProductSlug(selProductSlug);
			}
		};
		
		return "shoppingcart";
	}
	
	// --- other helper methods --- //
	
	// -- setters & getters -- /	
	public List<ProductDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDTO> products) {
		this.products = products;
	}	
}
