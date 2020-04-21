package com.dna.bifincan.web.product;

import java.io.IOException;
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
import com.dna.bifincan.service.ProductService;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.spring.ScopeType;
import org.apache.commons.collections.ListUtils;

@Named("productListAction")
@Scope(ScopeType.VIEW)
public class ProductListAction implements Serializable {
	private static final long serialVersionUID = 9162612932545096999L;
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ProductListAction.class);

	private List<ProductDTO> products;
	
	@Inject
	protected ProductService productService;	
	
	public ProductListAction() { }
	
	@PostConstruct
	public void initialize() { 
		loadProducts();
	}	
	
	/**
	 * This method is used in order to set-up a list of ALL products ready for rendering.
	 */
	public void loadProducts()  {
		// 1. find the current user's principal
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		
		if(principal == null) {  // if there is no user's valid session
			// TODO: this list normally should be cached for paging somehow
			List<Product> productRecordsInStock = productService.findAllActiveProductsAllTypesInStock();
			List<Product> productRecordsOutOfStock = productService.findAllActiveProductsAllTypesOutOfStock();
                        List<Product> productRecords = ListUtils.union(productRecordsInStock, productRecordsOutOfStock);
			
			fetchProductsAndConstructData(productRecords);
		} else {
			try {
				FacesUtils.getExternalContext().redirect(
						FacesUtils.getExternalContext().getRequestContextPath() + "/" + 
						"uygun-hediyeler");
			} catch(IOException ex) {
				// do sometihng here about this error
			}
		}
	}

	public void fetchProductsAndConstructData(List<Product> productRecords) {
		if(productRecords != null) {
			setProducts(new ArrayList<ProductDTO>());

			// TODO: the list normally should be a subset of the cached one (when will be used in paging)

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
                                productDTO.setStockQuantity(product.getStockQuantity());
                                productDTO.setOrderQuantity(product.getOrderQuantity());
                                productDTO.setLoss(product.getLoss());
				
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
