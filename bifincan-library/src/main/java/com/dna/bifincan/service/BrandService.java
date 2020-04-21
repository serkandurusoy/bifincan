package com.dna.bifincan.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.model.products.Brand;
import com.dna.bifincan.model.products.Image;
import com.dna.bifincan.repository.brand.BrandRepository;
import com.dna.bifincan.repository.products.ImageRepository;
import com.dna.bifincan.repository.products.ProductRepository;
import com.dna.bifincan.repository.survey.SurveyAnswerRepository;

@Service
@Named("brandService")
public class BrandService {
    @SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(BrandService.class);

	@Inject
	private BrandRepository brandRepository;
	@Inject
	private ProductRepository productRepository;
	@Inject
	private SurveyAnswerRepository surveyAnswerRepository;
	@Inject
	private ImageRepository imageRepository;
	
	public BrandService() { }

    public List<Brand> getBrands() {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "name");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        
        List<Brand> brands = this.brandRepository.findAll(sort);
        if(brands != null) {
        	for(Brand b : brands) {
        		b.getCategories().size();
        	}
        }
        
        return brands; 
    }
    
	@Transactional(propagation=Propagation.SUPPORTS)
	public Brand findBrandById(Long brandId) {
		Brand brand = brandRepository.findOne(brandId);
		if(brand.getCategories() != null) brand.getCategories().size();
		return brand;
	}
	
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public Long findBrandIdBySlug(String slug) {
		return brandRepository.findBrandIdBySlug(slug);
	}
	
    /**
     * Saves a brand, but first checks for duplicate name.
     * @param brand
     * @return  true for success or false for failure 
     */    
    public boolean saveBrand(Brand brand, Image logo) {
    	Long total = brandRepository.countByName(brand.getName(), 
    			brand.getId() != null ? brand.getId() : 0);
    	if(total == 0) {
    		if(brand.getId() == null || brand.getId() == 0) {
    			if(logo != null && logo.getContents() != null && logo.getContents().length > 0) {
    				imageRepository.save(logo);
    				brand.setLogo(logo.getId());
    			}

    			brand.setCreateDate(new Date());   			
    		} else {
    			/* TODO: this is just a workaround in order to preserve the current logo from empty logo form submissions; 
    			 	However a flag should be added to indicate that we really want to reset the logo..
    			 	Usign the DTO pattern may be better here..
    			 */
    			if(logo != null && logo.getContents().length > 0) {
    				if(brand.getLogo() != null) {
    					Image curImage = imageRepository.findOne(brand.getLogo());
    					if(curImage != null) imageRepository.delete(curImage);
    				}
    				imageRepository.save(logo);
    				brand.setLogo(logo.getId());				
    			}    			
    		}
    		this.brandRepository.save(brand);
    		return true;
    	} else {
    		return false;
    	}
    }	
    
    /**
     * Deletes a brand, but first checks for existing dependencies.
     * @param brand
     * @return  true for success or false for failure (ONLY due to dependencies)
     */
    public boolean deleteBrand(Brand brand) {
    	Long total1 = productRepository.countByBrand(brand.getId());
    	Long total2 = surveyAnswerRepository.countByBrand(brand.getId());
    	
    	if(total1 == 0 && total2 == 0) {
    		if(brand.getLogo() != null) {
	    		Image image = imageRepository.findOne(brand.getLogo());
	    		if(image != null) {
	    			imageRepository.delete(image);
	    		}
    		}
    		
    		// get a fresh copy of the entity
    		Brand tempBrand = this.brandRepository.findOne(brand.getId());
    		this.brandRepository.delete(tempBrand);
    		
    		return true;
    	} else {
    		return false;
    	}
    }      
    
    public Image findBrandLogo(Long logoId) {
    	return imageRepository.findOne(logoId);
    }
}
