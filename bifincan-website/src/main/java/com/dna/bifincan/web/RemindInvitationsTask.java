/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.web;

import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.model.user.UserInvitation;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.MailService;
import com.dna.bifincan.service.UserService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author hantsy
 */
@Named
public class RemindInvitationsTask {

    private static final Logger log = LoggerFactory.getLogger(RemindInvitationsTask.class);
    @Inject
    private UserService userService;
    @Inject
    private ConfigurationService configurationService;
    @Inject
    private MailService emailService;
    @Inject
    PasswordEncoder encoder;
    @Value("${mail.from}")
    private String mailFrom;

    @Scheduled(cron = "0 0 11 * * MON-FRI")
    //@Scheduled(cron = "*/60 * * * * *")// every 60 seconds for test */60 * * * * *
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void emailRemindDaily() {
        if (log.isDebugEnabled()) {
            log.debug("executing scheduled task @ dailyEmailReminder...begin");
        }

        Date lastTime = new Date();
        String baseUrl = getBaseUrlWithPath();

        boolean sendingFailed = false;
        
        List<UserInvitation> _invitations = userService.findUnsignedInvitationsInSevenDays();
        for (UserInvitation _invitation : _invitations) {
            if (_invitation.getCreateTime() == _invitation.getLastInvitationTime()) {
                _invitation.setLastInvitationTime(lastTime);
                userService.saveUserInvitation(_invitation);
                try {
                    sendReminderEmail(_invitation, baseUrl);
                } catch (Exception e) {
                    sendingFailed = true;
                    if (log.isErrorEnabled()) {
                        log.error("Daily invitation reminder error: " + e.getMessage());
                    }
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("executing scheduled task @ dailyEmailReminder...done");
        }

        if (sendingFailed) {
            emailService.sendEmail(mailFrom, "Daily invitation reminder job is NOT fully successful", "Daily invitation reminder job is NOT fully successful");
        } else {
            emailService.sendEmail(mailFrom, "Daily invitation reminder job successful", "Daily invitation reminder job successful");
        }
    }

    //get the base url of this website
    private String getBaseUrlWithPath() {
        String applicationCanonicalURL = "https://www.bifincan.com";
        String ctxPath = ServletContextInfo.getContextPath();
        Configuration canonicalUrlConfig = configurationService.find((ConfigurationType.APPLICATION_CANONICAL_URL.getKey()));
        if (canonicalUrlConfig != null) {
            applicationCanonicalURL = canonicalUrlConfig.getValue();
        }
        String baseUrlWithPath = applicationCanonicalURL + ctxPath;

        if (log.isDebugEnabled()) {
            log.debug("baseurl @" + baseUrlWithPath);
        }

        return baseUrlWithPath;
    }

    private String generateConfirmationKey(String email2, String _currentUsername) {
        String confirmKey = encoder.encodePassword(email2, _currentUsername);
        return confirmKey;
    }

    private void sendReminderEmail(UserInvitation _invitation, String baseUrl) throws MailException {
        if (log.isDebugEnabled()) {
            log.debug("send renminder email to @" + _invitation);
        }

        final User inviter = _invitation.getInviter();
        final String email = _invitation.getEmail();

        String encodedVal = generateConfirmationKey(email, inviter.getUsername());


        String link = getBaseUrlWithPath() + "/yeni-uye/" + email + "/" + _invitation.getId() + "/" + encodedVal;

        String link2 = getBaseUrlWithPath() + "/davetiyenin-hatirlatilmasini-istemiyorum/sadece-bu-davetiye-icin/" + email + "/" + _invitation.getId() + "/" + encodedVal;

        Map _model = new HashMap();
        _model.put("firstName", inviter.getFirstName());
        _model.put("lastName", inviter.getLastName());
        _model.put("linkAccept", link);
        _model.put("linkOptOutThis", link2);
        _model.put("baseurl", baseUrl);

        if (log.isDebugEnabled()) {
            log.debug("sending email reminder to @" + email);
        }

        emailService.sendEmail(email, inviter.getFirstName() + " " + inviter.getLastName() + " seni bifincan'da bekliyor", "remind-invitations", _model);
    }
}
