/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.repository.products;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.products.ProductComment;
import com.dna.bifincan.model.products.ProductCommentRating;
import com.dna.bifincan.model.user.User;

public interface ProductCommentRatingRepository extends CrudRepository<ProductCommentRating, Long> {
	@Query("select count(o) from ProductCommentRating o where o.productComment.id =:comment and o.user =:user")
	public Long countByCommentAndVoter(@Param("comment") Long productCommentId, @Param("user") User user);
}
