/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.repository.blog;

import com.dna.bifincan.model.blog.BlogComment;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hantsy
 */
public class BlogCommentRepositoryImpl implements BlogCommentRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<BlogComment> findRecentComments() {
        return em.createQuery("select p from BlogComment p where p.numberOfDisLikes-p.numberOfLikes < 3 order by p.commentedAt desc").setMaxResults(5).getResultList();
    }
}
