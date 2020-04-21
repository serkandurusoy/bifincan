/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.repository.products;

import com.dna.bifincan.model.products.ProductComment;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hantsy
 */
public class ProductCommentRepositoryImpl implements ProductCommentRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<ProductComment> findRecentComments() {
        return em.createQuery("select p from ProductComment p where p.numberOfDisLikes-p.numberOfLikes < 3 order by p.commentedAt desc").setMaxResults(5).getResultList();
    }
}
