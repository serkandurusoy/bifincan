/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.repository.blog;

import com.dna.bifincan.model.blog.BlogComment;
import java.util.List;

/**
 *
 * @author hantsy
 */
public interface BlogCommentRepositoryCustom {

    List<BlogComment> findRecentComments();
    
}
