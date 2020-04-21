package com.dna.bifincan.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.rating.Rating;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.repository.products.ProductRepository;
import com.dna.bifincan.repository.rating.RatingRepository;


@Service
@Named("ratingService")
public class RatingService {

	@Inject
	private RatingRepository ratingRepository;
	@Inject	
	private ProductRepository productRepository;	
	
	@Transactional
	public boolean saveRating(Rating rating) {
		this.ratingRepository.save(rating);
		return true;
	}

	public Rating findByOrderDetailAndUser(OrderDetails orderDetail, User user) {
		return this.ratingRepository.findByOrderDetailAndUser(orderDetail, user);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public Long countRatingsByOrderDetailsAndUserId(List<Long> orderItemIds, Long userId) {
		return ratingRepository.countRatingsByOrderDetailsAndUserId(orderItemIds, userId);
	}
	
    public Page<Rating> findRatings(int first, int pageSize) {
        Sort defaultSort = new Sort(new Sort.Order(Sort.Direction.DESC, "createTime"));
        Pageable cond = new PageRequest(first, pageSize, defaultSort);
        return ratingRepository.findAll(cond);
    }	
    
    public Rating findRating(Long id) {
        return ratingRepository.findOne(id);
    }   
    
    public float findAverageRatingByProduct(Long id) {
    	Product product = productRepository.findOne(id);
    	if(product != null) {
    		Double avgValue = ratingRepository.findAverageRatingByProduct(id);
    		if(avgValue != null) {
    			return avgValue.floatValue();  
    		}
    	}
    	return 0F;
    }
    
    public long noOfRatings(){
        return this.ratingRepository.count();
    }
  
}
