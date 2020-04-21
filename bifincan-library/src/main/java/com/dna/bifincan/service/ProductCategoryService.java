package com.dna.bifincan.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.dna.bifincan.model.products.ProductCategoryLevelOne;
import com.dna.bifincan.model.products.ProductCategoryLevelThree;
import com.dna.bifincan.model.products.ProductCategoryLevelTwo;
import com.dna.bifincan.repository.products.ProductCategoryLevelOneRepository;
import com.dna.bifincan.repository.products.ProductCategoryLevelThreeRepository;
import com.dna.bifincan.repository.products.ProductCategoryLevelTwoRepository;
import com.dna.bifincan.repository.products.ProductRepository;
import com.dna.bifincan.repository.survey.SurveyBrandProductCategoryRepository;

@Service
@Named("productCategoryService")
public class ProductCategoryService {
	@Inject
	private ProductCategoryLevelOneRepository productCategoryLevelOneRepository;
	@Inject
	private ProductCategoryLevelTwoRepository productCategoryLevelTwoRepository;
	@Inject
	private ProductCategoryLevelThreeRepository productCategoryLevelThreeRepository;	
	@Inject
	private ProductRepository productRepository;
	@Inject
	private SurveyBrandProductCategoryRepository surveyBrandProductCategoryRepository;	
	
	public ProductCategoryService() { }
	
    public List<ProductCategoryLevelOne> findProductCategoriesLevelOne() {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "name");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        return this.productCategoryLevelOneRepository.findAll(sort);
    }
    
    public List<ProductCategoryLevelTwo> findProductCategoriesLevelTwo() {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "name");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        return this.productCategoryLevelTwoRepository.findAll(sort);
    }
    
    public List<ProductCategoryLevelThree> findProductCategoriesLevelThree() {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "name");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        return this.productCategoryLevelThreeRepository.findAll(sort);
    }
    
    /**
     * Saves a product category level one entity, but first checks for duplicate name.
     * @param category
     * @return  true for success or false for failure 
     */    
    public boolean saveProductCategoryLevelOne(ProductCategoryLevelOne category) {
    	Long total = productCategoryLevelOneRepository.countByName(category.getName(), 
    			category.getId() != null ? category.getId() : 0);
    	if(total == 0) {
    		this.productCategoryLevelOneRepository.save(category);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * Saves a product category level two entity, but first checks for duplicate name.
     * @param category
     * @return  true for success or false for failure 
     */    
    public boolean saveProductCategoryLevelTwo(ProductCategoryLevelTwo category) {
    	Long total = productCategoryLevelTwoRepository.countByName(category.getName(), 
    			category.getId() != null ? category.getId() : 0, category.getParent().getId());
    	if(total == 0) {
    		this.productCategoryLevelTwoRepository.save(category);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * Saves a product category level three entity, but first checks for duplicate name.
     * @param category
     * @return  true for success or false for failure 
     */    
    public boolean saveProductCategoryLevelThree(ProductCategoryLevelThree category) {
    	Long total = productCategoryLevelThreeRepository.countByName(category.getName(), 
    			category.getId() != null ? category.getId() : 0, category.getParent().getId(), category.getParent().getParent().getId());
    	if(total == 0) {
    		this.productCategoryLevelThreeRepository.save(category);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * Deletes a product category level one entity, but first checks for existing dependencies.
     * @param category
     * @return  true for success or false for failure (ONLY due to dependencies)
     */
    public boolean deleteProductCategoryLevelOne(ProductCategoryLevelOne category) {
    	Long total = productCategoryLevelTwoRepository.countByProductCategoryLevelOne(category.getId());
    	if(total == 0) {
    		// get a fresh copy of the entity
    		ProductCategoryLevelOne tempCategory = this.productCategoryLevelOneRepository.findOne(category.getId());
    		this.productCategoryLevelOneRepository.delete(tempCategory);
    		return true;
    	} else {
    		return false;
    	}
    }   
    
    /**
     * Deletes a product category level one entity, but first checks for existing dependencies.
     * @param category
     * @return  true for success or false for failure (ONLY due to dependencies)
     */
    public boolean deleteProductCategoryLevelTwo(ProductCategoryLevelTwo category) {
    	Long total = productCategoryLevelThreeRepository.countByProductCategoryLevelTwo(category.getId());
    	if(total == 0) {
    		// get a fresh copy of the entity
    		ProductCategoryLevelTwo tempCategory = this.productCategoryLevelTwoRepository.findOne(category.getId());
    		this.productCategoryLevelTwoRepository.delete(tempCategory);
    		return true;
    	} else {
    		return false;
    	}
    }    

    /**
     * Deletes a product category level three entity, but first checks for existing dependencies.
     * @param category
     * @return  true for success or false for failure (ONLY due to dependencies)
     */
    public boolean deleteProductCategoryLevelThree(ProductCategoryLevelThree category) {
    	Long total1 = productRepository.countByProductCategoryLevelThree(category.getId());
    	Long total2 = surveyBrandProductCategoryRepository.countByProductCategoryLevelThree(category.getId());
    	if(total1 == 0 && total2 == 0) {
    		// get a fresh copy of the entity
    		ProductCategoryLevelThree tempCategory = this.productCategoryLevelThreeRepository.findOne(category.getId());
    		this.productCategoryLevelThreeRepository.delete(tempCategory);
    		return true;
    	} else {
    		return false;
    	}
    }      
}
