package com.dna.bifincan.web.order;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.exception.ConfigurationException;
import com.dna.bifincan.exception.NoSurpiseProductsException;
import com.dna.bifincan.exception.OrderProcessException;
import com.dna.bifincan.model.address.Address;
import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.dto.ProductDTO;
import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.model.order.ShoppingCart;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.type.OrderDetailsType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.MailService;
import com.dna.bifincan.service.OrderService;
import com.dna.bifincan.service.ProductService;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.WebConstants;
import com.dna.bifincan.util.spring.ScopeType;

@Named("shoppingCartAction")
@Scope(ScopeType.VIEW)  
public class ShoppingCartAction implements Serializable {
	private static final long serialVersionUID = -3400926595973713467L;

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ShoppingCartAction.class);	
	
	private List<ProductDTO> products;
	private Address primaryAddress;
	
	@Inject
	private ProductService productService;	
	@Inject
	private OrderService orderService;
	@Inject
	private UserService userService;
	@Inject
	private MailService mailService;
	@Inject
	private ConfigurationService configurationService;

        public String getBaseUrlWithPath() {
            String applicationCanonicalURL = "https://www.bifincan.com";
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            String ctxPath = context.getRequestContextPath();
            Configuration canonicalUrlConfig = configurationService.find((ConfigurationType.
                            APPLICATION_CANONICAL_URL.getKey()));
            if(canonicalUrlConfig != null) applicationCanonicalURL = canonicalUrlConfig.getValue();
            String baseUrlWithPath = applicationCanonicalURL + ctxPath;
            return baseUrlWithPath;
        }
	
	public ShoppingCartAction() { }
	
	@PostConstruct
	public void initialize() { 
		loadProducts();
	}	

	/**
	 * This method is used to fetch the current cart's products.
	 */
	@SuppressWarnings("unchecked")
	public void loadProducts() {
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();

		if(principal != null) {
			Map<String, Object> values = FacesUtils.getExternalContext().getSessionMap();			
			User user = (User)values.get(WebConstants.CURRENT_USER_SESSION_KEY);
			
			if(user != null) {
				ShoppingCart userShoppingCart = user.getShoppingCart();
				if(this.products != null) this.products.clear(); // reset first the list of products that is to be rendered
				Set<String> productSlugs = userShoppingCart.getProducts();
				
				Integer totalProductPoints = 0;
				if(productSlugs != null && !productSlugs.isEmpty()) {
					List<Product> productRecords = productService.findProductsBySlugs(productSlugs); // fetch the product records
					if(productRecords != null && !productRecords.isEmpty()) {
						Object[] results = constructProductList(productRecords);
						this.products = (List<ProductDTO>)results[0];
						totalProductPoints = (Integer)results[1];
					}
				}
				userShoppingCart.setTotalPointsOfProducts(totalProductPoints); // REFRESH the total points of products in cart
				
				this.primaryAddress = userService.  // fetch a fresh object due to the lazy retrieval of the addresses in user's token in session
						getUserPrimaryAddress(userService.findUserByUsername(user.getUsername()));

			}	
		}
	}
	
	/**
	 * This method is used to remove a product (slug) from the cart's cache
	 * @return String always null enforcing a postback
	 */
	public String removeProductSlug() {
		Flash flash = FacesUtils.getFlash();
		String selProductSlug = (String)flash.get(WebConstants.PRODUCT_SLUG);
		
		if(selProductSlug != null) {
			Map<String, Object> values = FacesUtils.getExternalContext().getSessionMap();
			User user = (User)values.get(WebConstants.CURRENT_USER_SESSION_KEY);
			if(user != null) {
				ShoppingCart userShoppingCart = user.getShoppingCart();
				userShoppingCart.removeProductSlug(selProductSlug);	
			}
		}
		
		loadProducts(); // refresh the list
		
		return "shoppingcart";
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String placeOrder() {
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();

		if(principal != null) {		
			Map<String, Object> sessionAttrs = FacesUtils.getExternalContext().getSessionMap();			
			User user = (User)sessionAttrs.get(WebConstants.CURRENT_USER_SESSION_KEY);
			
			if(user != null) {
				ShoppingCart userShoppingCart = user.getShoppingCart();
				try {
					Object[] orderResults = orderService.createOrder(user.getUsername(), userShoppingCart.getProducts());
					user.setPointsBalance((Integer)orderResults[1]); // REFRESH user's point balance (UI component)
					
					// send an email
					Map model = new HashMap();
					model.put("baseurl", getBaseUrlWithPath());
					model.put("userfirstname", user.getFirstName());

					Object[] invoiceResults = constructInvoiceItemList((List<OrderDetails>)orderResults[0]);

					model.put("products", (List<ProductDTO>)invoiceResults[0]);
					model.put("freeproducts", (List<ProductDTO>)invoiceResults[1]);
					model.put("totalproductpoints", (Integer)invoiceResults[2]);
					// ... add here more attributes
					
					try {
						mailService.sendEmail(user.getEmail(), "Yeni siparişini aldım. Tamam, bende ;)", "new-order", model);
					} catch (Exception ex) { 
						ex.printStackTrace();
					}						
					user.getShoppingCart().setTotalPointsOfProducts(0); // RESET the total points in cart
					userShoppingCart.getProducts().clear();
				} catch(Exception ex) { 
					processException(ex);
				}
			}
			loadProducts(); // reload the cart
		}
		return null;
	}
	
	
	// --- other helper methods --- //
	private void processException(Exception ex) {
		if(ex instanceof OrderProcessException) {
			log.debug(ex.getMessage());
			switch(((OrderProcessException)ex).getErrorCode()) {
			case OrderProcessException.NO_PRODUCTS_IN_CART:
				FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						WebConstants.ERROR_NO_PRODUCTS_IN_CART_KEY));	
				break;
			case OrderProcessException.NO_RIGHTS_TO_PLACE_ORDER:
				FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						WebConstants.ERROR_NO_RIGHTS_TO_ORDER_KEY));
				break;
			case OrderProcessException.NO_AVAILABLE_USER_POINTS:
				FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						WebConstants.ERROR_NO_AVAILABLE_POINTS_KEY));
				break;
			case OrderProcessException.PRODUCT_IS_NOT_AVAILABLE:
				FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						WebConstants.ERROR_PRODUCT_NOT_AVAILABLE_KEY));
			case OrderProcessException.INVALID_NUMBER_OF_PRODUCTS_IN_CART:
				FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						WebConstants.ERROR_INVALID_NUMBER_OF_PRODUCTS_IN_CART_KEY));	
			case OrderProcessException.LOW_USER_ACTIVITY_LEVEL:
				FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
						WebConstants.ERROR_LOW_USER_ACTIVITY_LEVEL));				
				
			}
		} else if(ex instanceof NoSurpiseProductsException) {
			log.debug(ex.getMessage());
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
					WebConstants.ERROR_WHILE_PLACING_ORDER_KEY));
		} else if(ex instanceof ConfigurationException) {
			log.debug(ex.getMessage());
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
					WebConstants.ERROR_WHILE_PLACING_ORDER_KEY));
		} else {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
					WebConstants.ERROR_GENERAL_ERROR_KEY));
		}
	}
	
	
	private Object[] constructInvoiceItemList(List<OrderDetails> orderItems) {
		Object[] results = new Object[3];
		
		if(orderItems != null && !orderItems.isEmpty()) {
			int totalProductPoints = 0;
			List<ProductDTO> products = new ArrayList<ProductDTO>();
			List<ProductDTO> freeProducts = new ArrayList<ProductDTO>();
			for(OrderDetails orderItem : orderItems) { // iterate through the list
				ProductDTO productDTO = new ProductDTO();
	
				productDTO.setId(orderItem.getProduct().getId());
				productDTO.setBrandName(orderItem.getProduct().getBrand().getName());
				productDTO.setProductShortDescription(orderItem.getProduct().getShortDescription());
				productDTO.setProductMoneyValue(orderItem.getProduct().getPriceMoney());
				productDTO.setProductName(orderItem.getProduct().getName());
				productDTO.setProductPointValue(orderItem.getProduct().getPricePoints());
				productDTO.setProductSlug(orderItem.getProduct().getSlug());
				productDTO.setClassifier(orderItem.getProduct().getClassifier().toString());
				productDTO.setRemovable(true);
				
				if(orderItem.getType() == OrderDetailsType.ORDERABLE_PRODUCT) {
					productDTO.setOrderable(true);
					totalProductPoints += orderItem.getProduct().getPricePoints();
					products.add(productDTO);
				} else {
					productDTO.setOrderable(false);
					freeProducts.add(productDTO);
				}
	
				
			}
			results[0] = products;
			results[1] = freeProducts;
			results[2] = totalProductPoints;
		}	
		
		return results;
	}	
	
	private Object[] constructProductList(List<Product> productRecords) {
		Object[] results = new Object[2];
		
		if(productRecords != null && !productRecords.isEmpty()) {
			int totalProductPoints = 0;
			List<ProductDTO> products = new ArrayList<ProductDTO>();
			for(Product product : productRecords) { // iterate through the list
				ProductDTO productDTO = new ProductDTO();
	
				productDTO.setId(product.getId());
				productDTO.setBrandName(product.getBrand().getName());
				productDTO.setProductShortDescription(product.getShortDescription());
				productDTO.setProductMoneyValue(product.getPriceMoney());
				productDTO.setProductName(product.getName());
				productDTO.setProductPointValue(product.getPricePoints());
				productDTO.setProductSlug(product.getSlug());
				productDTO.setClassifier(product.getClassifier().toString());
				productDTO.setRemovable(true);
				
				totalProductPoints += product.getPricePoints();
				
				products.add(productDTO);
			}
			results[0] = products;
			results[1] = totalProductPoints;
		}	
		
		return results;
	}	
	
	// -- setters & getters -- /
	public List<ProductDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDTO> products) {
		this.products = products;
	}

	public Address getPrimaryAddress() {
		return primaryAddress;
	}

	public void setPrimaryAddress(Address primaryAddress) {
		this.primaryAddress = primaryAddress;
	}
	
}
