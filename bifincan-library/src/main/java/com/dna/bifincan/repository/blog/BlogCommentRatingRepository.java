/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.repository.blog;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.blog.BlogComment;
import com.dna.bifincan.model.blog.BlogCommentRating;
import com.dna.bifincan.model.blog.BlogPost;
import com.dna.bifincan.model.type.VoteType;
import com.dna.bifincan.model.user.User;

public interface BlogCommentRatingRepository extends CrudRepository<BlogCommentRating, Long> {
	@Query("select count(o) from BlogCommentRating o where o.blogComment.id =:comment and o.user =:user")
	public Long countByCommentAndVoter(@Param("comment") Long blogComment, @Param("user") User user);
	
}
