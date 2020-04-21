/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.repository.blog;

import com.dna.bifincan.model.blog.BlogPost;
import java.util.Date;
import java.util.List;

/**
 *
 * @author hantsy
 */
public interface BlogPostRepositoryCustom {

    BlogPost findNextPost(Date _postedAt);

    BlogPost findPrePost(Date _postedAt);

    public List<BlogPost> findMostPopPosts();

    public List<BlogPost> findRecentPosts(int maxResult);
}
