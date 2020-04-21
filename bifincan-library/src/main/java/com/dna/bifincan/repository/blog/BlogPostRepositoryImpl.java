/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.repository.blog;

import com.dna.bifincan.model.blog.BlogPost;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hantsy
 */
public class BlogPostRepositoryImpl implements BlogPostRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    @Override
    public BlogPost findNextPost(Date _postedAt) {
        List<BlogPost> results = em.createQuery("select p from BlogPost p where p.postedAt>:_postedAt order by p.postedAt asc").setParameter("_postedAt", _postedAt).setMaxResults(1).getResultList();

        if (!results.isEmpty()) {
            return results.get(0);
        }

        return null;
    }

    @Override
    public BlogPost findPrePost(Date _postedAt) {
        List<BlogPost> results = em.createQuery("select p from BlogPost p where p.postedAt<:_postedAt order by p.postedAt desc").setParameter("_postedAt", _postedAt).setMaxResults(1).getResultList();

        if (!results.isEmpty()) {
            return results.get(0);
        }

        return null;
    }

    @Override
    public List<BlogPost> findMostPopPosts() {
        return em.createQuery("select p from BlogPost p order by size(p.comments) desc").setMaxResults(5).getResultList();
    }

    @Override
    public List<BlogPost> findRecentPosts(int maxResult) {
        return em.createQuery("select p from BlogPost p order by p.postedAt desc").setMaxResults(maxResult).getResultList();
    }
}
