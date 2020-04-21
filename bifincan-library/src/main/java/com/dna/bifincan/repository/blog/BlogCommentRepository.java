/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.repository.blog;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.blog.BlogComment;
import com.dna.bifincan.model.blog.BlogPost;
import com.dna.bifincan.model.type.VoteType;
import com.dna.bifincan.model.user.User;

/**
 *
 * @author hantsy
 */
public interface BlogCommentRepository extends PagingAndSortingRepository<BlogComment, Long>, BlogCommentRepositoryCustom {

    @Query("select count(o) from BlogComment o where o.user =:_user")
    public long countByUser(@Param("_user") User _user);
    
	@Query("select count(o) from BlogComment o where o.post =:post and o.user =:user")
	public long countCommentsByPostAndUser(@Param("post") BlogPost post, @Param("user") User user);	 
    
	@Query("select count(o) from BlogComment o where o.post =:post and o.numberOfLikes - o.numberOfDisLikes > -3")
	public long countVotableCommentsByPost (@Param("post") BlogPost post);	 
    
	
	@Query("select count(o) from BlogComment o left join o.ratings r where o.post =:post and r.user =:user and r.type=:type")
	public long countCommentsByPostAndUserAndVoteType(@Param("post")BlogPost post, @Param("user") User user,
			@Param("type") VoteType dislike);

        @Query("select sum(o.numberOfLikes) from BlogComment o where o.post =:post and o.user =:user")
	public long countLikesReceivedForUserAtPost(@Param("post")BlogPost post, @Param("user") User user);

        @Query("select sum(o.numberOfDisLikes) from BlogComment o where o.post =:post and o.user =:user")
	public long countDisLikesReceivedForUserAtPost(@Param("post")BlogPost post, @Param("user") User user);

}
