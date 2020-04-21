/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.web;

import com.dna.bifincan.model.user.User;
import com.dna.bifincan.model.user.UserLevelValue;
import com.dna.bifincan.service.MailService;
import com.dna.bifincan.service.PointTransactionAccountingService;
import com.dna.bifincan.service.UserService;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author hantsy
 */
@Named
public class ActivityLevelTask {

    private static final Logger log = LoggerFactory.getLogger(ActivityLevelTask.class);

    @Inject
    private PointTransactionAccountingService trancactionService;

    @Inject
    private UserService userService;

    @Inject
    private MailService emailService;

    @Value("${mail.from}")
    private String mailFrom;

    @Scheduled(cron = "0 0 5 * * 1")
    //@Scheduled(cron = "*/10 * * * * *")// every five seconds for test */5 * * * * *
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void adjustUserActivityLevels() {
        if (log.isDebugEnabled()) {
            log.debug("executing scheduled task @ adjustUserActivityLevels...begin");
        }

        final float weeklyAverage = trancactionService.calculateWeeklyAverage();
        if (log.isDebugEnabled()) {
            log.debug("weekly average@" + weeklyAverage);
        }
        try {

            for (UserLevelValue _user : userService.findAllActiveUserLevels()) {
                
                if(log.isDebugEnabled()){
                    log.debug("userlevel object  username@"+_user.getUsername()+", activityLevel@"+_user.getActivityLevel());
                }
                
                float weeklyUserTotal = trancactionService.calculateWeeklyUserTotal(_user.getUsername());
                if (log.isDebugEnabled()) {
                    log.debug("weekly average@" + weeklyUserTotal);
                }

               // if (!_user.isAmbassador()) {// the condition is added to jpql now.
                    if (weeklyUserTotal >= weeklyAverage) {
                        _user.increaseActivityLevel();
                        if (log.isDebugEnabled()) {
                            log.debug("increase acitivity level to @" + _user.getActivityLevel() + " , user@" + _user.getUsername());
                        }
                        userService.updateUserActivityLevel(_user);
                    } else if (weeklyUserTotal < weeklyAverage) {
                        _user.decreaseActivityLevel();
                        if (log.isDebugEnabled()) {
                            log.debug("descrease acitivity level to @" + _user.getActivityLevel() + " , user@" + _user.getUsername());
                        }
                        userService.updateUserActivityLevel(_user);
                    }
                //}
            }

            if (log.isDebugEnabled()) {
                log.debug("executing scheduled task @ adjustUserActivityLevels...done");
            }

            emailService.sendEmail(mailFrom, "Activity level job is successful", "Activity level job is successful");
        } catch (Exception e) {
            log.error(e.getMessage());
            emailService.sendEmail(mailFrom, "Activity level job is NOT successful", "Activity level job is NOT successful");
        }
    }
}
