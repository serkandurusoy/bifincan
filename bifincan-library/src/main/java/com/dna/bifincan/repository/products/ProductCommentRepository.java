/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.repository.products;

import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.products.ProductComment;
import com.dna.bifincan.model.type.VoteType;
import com.dna.bifincan.model.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author hantsy
 */
public interface ProductCommentRepository extends PagingAndSortingRepository<ProductComment, Long>, ProductCommentRepositoryCustom {

    @Query("select count(o) from ProductComment o where o.user =:_user")
    public long countByUser(@Param("_user") User _user);
    
	
	@Query("select count(o) from ProductComment o where o.product =:product and o.user =:user")
	public long countCommentsbyProductAndUser(@Param("product") Product product, @Param("user") User user);

	@Query("select count(o) from ProductComment o where o.product =:product and o.numberOfLikes - o.numberOfDisLikes > -3")
	public long countVotableCommentsByProduct(@Param("product") Product product);


	@Query("select count(o) from ProductComment o left join o.ratings r where o.product =:product and r.user =:user and r.type=:type")
	public long countCommentsbyProductAndUserAndVoteType(@Param("product") Product product,
			 @Param("user")User user,   @Param("type")VoteType like);

	@Query("select sum(o.numberOfLikes) from ProductComment o where o.product =:product and o.user =:user")
	public long countLikesReceivedForUserAtProduct(@Param("product") Product product, @Param("user")User user);

	@Query("select sum(o.numberOfDisLikes) from ProductComment o where o.product =:product and o.user =:user")
	public long countDisLikesReceivedForUserAtProduct(@Param("product") Product product, @Param("user")User user);

}
