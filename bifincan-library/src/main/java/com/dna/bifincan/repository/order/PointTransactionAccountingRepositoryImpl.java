/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.repository.order;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hantsy
 */
public class PointTransactionAccountingRepositoryImpl implements PointTransactionAccountingRepositoryCustom{

    @PersistenceContext 
    EntityManager em;
    
    @Override
    public List findLatestPointsStatistics() {
        //return  em.createQuery("select new map(sum(o.invitationPoints+o.invitationSuccessPoints+o.signupPoints+o.loginPoints+o.ratingPoints+ o.productSurveyPoints+o.quizPoints+o.genericSurveyPoints+o.blogCommentPoints+o.productCommentPoints+o.socialPoints+o.votingPoints) as score, o.user as user) from PointTransactionAccounting o  where o.user.active=true  group by o.user order by score desc, o.user.createDate desc" )
        return  em.createQuery("select new map(sum(o.invitationPoints+o.invitationSuccessPoints+o.signupPoints+o.loginPoints+o.ratingPoints+ o.productSurveyPoints+o.quizPoints+o.genericSurveyPoints+o.blogCommentPoints+o.productCommentPoints+o.socialPoints+o.votingPoints) as score, o.user as user) from PointTransactionAccounting o  where o.user.active=true  group by o.user,o.user.createDate order by score desc, o.user.createDate desc" )
                .setMaxResults(10)
                .getResultList();
    }
    
}
