/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.repository.user;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hantsy
 */
public class UserRepositoryImpl implements UserRepositoryCustom {
    private static final Logger log=LoggerFactory.getLogger(UserRepositoryImpl.class);
    @PersistenceContext
    EntityManager em;

    @Override
    public void updateUserActivityLevel(String username, int activityLevel) {
        Query q = em.createQuery("update User u set u.activityLevel=:activityLevel where u.username=:username");
        q.setParameter("activityLevel", activityLevel);
        q.setParameter("username", username);
        int num=q.executeUpdate();
        if(log.isDebugEnabled()){
            log.debug(" update result @"+ num);
        }    
    }
}
