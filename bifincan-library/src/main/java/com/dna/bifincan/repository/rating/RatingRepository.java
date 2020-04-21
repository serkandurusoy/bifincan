package com.dna.bifincan.repository.rating;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.address.City;
import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.model.rating.Rating;
import com.dna.bifincan.model.user.User;


public interface RatingRepository extends PagingAndSortingRepository<Rating, Long> {
	
	public Rating findByOrderDetailAndUser(OrderDetails orderDetail, User user);
	
	@Query("select count(r) from Rating r where r.user.id = :userId and r.orderDetail.id in (:orderItemIds)")
	public Long countRatingsByOrderDetailsAndUserId(@Param("orderItemIds") List<Long> orderItemIds, @Param("userId") Long userId);
	
	@Query("select avg(r.star) from Rating r where r.orderDetail.product.id = :productId")
	public Double findAverageRatingByProduct(@Param("productId")Long productId);	
	
}
