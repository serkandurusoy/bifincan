/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.repository.blog;

import com.dna.bifincan.model.blog.BlogPost;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author hantsy
 */
public interface BlogPostRepository extends PagingAndSortingRepository<BlogPost, Long>, BlogPostRepositoryCustom {

	
    public List<BlogPost> findBySlug(String slug);
    
    public List<BlogPost> findAll(Sort sort);

	@Query("select p.id from BlogPost p where p.slug = :slug")
	public Long findBlogPostIdBySlug(@Param("slug") String slug);

}
