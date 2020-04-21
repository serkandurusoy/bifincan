/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.order.Order;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.user.EmailCampaign;
import com.dna.bifincan.model.user.EmailGroup;
import com.dna.bifincan.model.user.EmailListEntry;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.model.user.UserInvitation;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.MailService;
import com.dna.bifincan.service.OrderService;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.DateUtils;

@Named
public class UserNotificationTask {
    private static final Logger log = LoggerFactory.getLogger(UserNotificationTask.class);
    
    private static final String JOB_SUBJECT_MSG = "Executing a job - results";
    
    private static final String SUCCESFUL_UNCONFIRMED_JOB_MSG = "'NotifyUsersForUnconfirmedOrder' job was successful";
    private static final String UNSUCCESFUL_UNCONFIRMED_JOB_MSG = "'NotifyUsersForUnconfirmedOrder' job is NOT successful";

    private static final String SUCCESFUL_LONG_ABSENCE_JOB_MSG = "'notifyUsersForLongAbsence' job was successful";
    private static final String UNSUCCESFUL_LONG_ABSENCE_JOB_MSG = "'notifyUsersForLongAbsence' job is NOT successful";
    
    private static final String SUCCESFUL_NO_INV_ACTIVITY_JOB_MSG = "'notifyUsersForNoInvitationActivity' job was successful";
    private static final String UNSUCCESFUL_NO_INV_ACTIVITY_JOB_MSG = "'notifyUsersForNoInvitationActivity' job is NOT successful";
    
    private static final String SUCCESFUL_NON_COMPLETED_ORDER_JOB_MSG = "'notifyUsersForNonCompletedOrder' job was successful";
    private static final String UNSUCCESFUL_NON_COMPLETED_ORDER_JOB_MSG = "'notifyUsersForNonCompletedOrder' job is NOT successful";    
    
    private static final String SUCCESFUL_CAMPAIGN_NOTIIFICATIONS_JOB_MSG = "'notifyUsersFromCampaigns' job was successful";
    private static final String UNSUCCESFUL_CAMPAIGN_NOTIIFICATIONS_JOB_MSG = "'notifyUsersFromCampaigns' job is NOT successful"; 
    
    
    private static final int LONG_ABSENCE_DAYS = 30, NO_INV_ACTIVITY_DAYS = 30, ACC_ORDER_DAYS = 30;
    
    @Inject
    private OrderService orderService;
    @Inject 
    private UserService userService;
    @Inject
    private MailService emailService;
    @Inject
    private ConfigurationService configurationService;
    
    @Value("${mail.from}")
    private String mailFrom;

    //@Scheduled(cron = "0 0 15 * * 2")  // every Tuesday at 15:00
    //@Scheduled(cron = "*/5 * * * * *")// every five seconds for test */5 * * * * *
    @Scheduled(cron = "0 0 16 1 * *")  // every first day of month at 16:00
    public void notifyUsersForUnconfirmedOrder() {
        log.debug("executing scheduled task @ notifyUsersForUnconfirmeOrder...begin");

        try {
        	String baseUrl = getBaseUrlWithPath();
        	
        	List<Order> orders = orderService.findUnconfirmedOrdersTwoWeeks(); 
        	
        	if(orders != null && !orders.isEmpty()) {
	           for(Order order : orders) {
	            	if (order.getAddress().getUser().getEmailVerification().isEmailVerified()) {
                            Map<String,String> _model = new HashMap<String,String>();

                            _model.put("firstName", order.getAddress().getUser().getFirstName());
                            _model.put("lastName", order.getAddress().getUser().getLastName());
                            _model.put("baseurl", baseUrl);
                            _model.put("sent_time", DateUtils.formatDateToDDMMYYYY(order.getSentTime())); 

                            if(log.isDebugEnabled()){
                                log.debug("sending email reminder to @" + order.getAddress().getUser().getEmail());
                            }

                            emailService.sendEmail(order.getAddress().getUser().getEmail(), "Hediyenin onay kodunu henüz girmedin", 
	                		"remind-unconfirmed-order", _model);
                        }
                   }
        	}
        	
            log.debug("executing scheduled task @ notifyUsersForUnconfirmeOrder...done");

            emailService.sendEmail(mailFrom, JOB_SUBJECT_MSG, SUCCESFUL_UNCONFIRMED_JOB_MSG);
        } catch (Exception e) {
            log.error(e.getMessage());
            emailService.sendEmail(mailFrom, JOB_SUBJECT_MSG, UNSUCCESFUL_UNCONFIRMED_JOB_MSG);
        }
    }
    
    //@Scheduled(cron = "0 0 15 * * 4")  // every Thursday at 15:00
    @Scheduled(cron = "0 0 16 15 * *")  // every 15th day of month at 15:00
    public void notifyUsersForNonCompletedOrder() {
        log.debug("executing scheduled task @ notifyUsersForNonCompletedOrder...begin");

        try {
        	String baseUrl = getBaseUrlWithPath();
        	
        	List<Order> orders = orderService.findNoSurveyCompletedOrders(ACC_ORDER_DAYS);  
        	
        	if(orders != null && !orders.isEmpty()) {
	            for(Order order : orders) {
                        if (order.getAddress().getUser().getEmailVerification().isEmailVerified()) {
                            Map<String,String> _model = new HashMap<String,String>();

                            _model.put("firstName", order.getAddress().getUser().getFirstName());
                            _model.put("lastName", order.getAddress().getUser().getLastName());
                            _model.put("baseurl", baseUrl);
                            _model.put("received_time", DateUtils.formatDateToDDMMYYYY(order.getReceivedTime())); 

                            if(log.isDebugEnabled()){
                                log.debug("sending email reminder to @" + order.getAddress().getUser().getEmail());
                            }

                            emailService.sendEmail(order.getAddress().getUser().getEmail(), "Son hediyene özel soruları henüz yanıtlamadın", 
                                            "remind-non-completed-order", _model);
                        }
	            }
        	}
        	
            log.debug("executing scheduled task @ notifyUsersForNonCompletedOrder...done");

            emailService.sendEmail(mailFrom, JOB_SUBJECT_MSG, SUCCESFUL_NON_COMPLETED_ORDER_JOB_MSG);
        } catch (Exception e) {
            log.error(e.getMessage());
            emailService.sendEmail(mailFrom, JOB_SUBJECT_MSG, UNSUCCESFUL_NON_COMPLETED_ORDER_JOB_MSG);
        }
    }

  //  @Scheduled(fixedRate=10000)  // every 10 secs... for test
    @Scheduled(fixedRate=1800000)  // every 30 mins
    public void notifyUsersFromCampaigns() {
    	log.debug("executing scheduled task @ notifyUsersFromCampaigns...begin");

        Date scheduledDate = new Date();
        List<EmailCampaign> campaigns = emailService.findPastScheduledCampaigns(scheduledDate); 

        if(campaigns != null && !campaigns.isEmpty()) {
        	String optAllLnk = getBaseUrlWithPath() + 
	        		"/e-posta-istemiyorum/gelen-davetiyelerimden/@@target_email@@/@@campaign_id@@/@@encoded_key@@";
        	String optLnk = getBaseUrlWithPath() + 
	        		"/e-posta-istemiyorum/bifincandan/@@target_email@@/@@campaign_id@@/@@encoded_key@@";    
        	
        	for(EmailCampaign campaign : campaigns) {
        		try {
        			log.debug("@@@ precessing cid = " + campaign.getId());
        			emailService.initiateEmailCampaignTask(campaign);  // initiate the campaign task
        			
        			EmailGroup group = campaign.getTargetGroup();
        			if(campaign.isSendToInvitations()) {
        				log.debug("@@@ case 1: execute for invitations ");
        				emailService.sendEmailBatchForNotOptedOutInvitations(campaign, 
        						userService.findNotOptedOutInvitationEmails(),    
        						optAllLnk);   
        			} else if(campaign.isSendToUsers()) {  
        				log.debug("@@@ case 2: execute for site acc email users");
        				emailService.sendEmailBatchForActiveSiteEmailAcceptedUsers(campaign, 
        						userService.findActiveSiteEmailAcceptedAndEmailVerifiedUsers());            			
        			} else if(group != null) {    
        				log.debug("@@@ case 3: execute for group = " + group.getId());
        				emailService.sendEmailBatchForGroup(campaign, group, optLnk);         				
        			}

        			emailService.sendEmail(mailFrom, JOB_SUBJECT_MSG, SUCCESFUL_CAMPAIGN_NOTIIFICATIONS_JOB_MSG + 
        					" (Campaign ID = " + campaign.getId() + ")");
        		} catch (Exception e) {
        			log.error(e.getMessage());
        			emailService.sendEmail(mailFrom, JOB_SUBJECT_MSG, UNSUCCESFUL_CAMPAIGN_NOTIIFICATIONS_JOB_MSG + 
        					" (Campaign ID = " + campaign.getId() + ")");
        		} finally {
        			try { // finalize the campaign task
        				emailService.finalizeEmailCampaignTask(campaign); 
        			} catch(Exception e1) {
        				log.error(e1.getMessage());
        			}
        		}
        	}
        	
        }

        log.debug("executing scheduled task @ notifyUsersFromCampaigns...done");
    }
    
/*
    @Scheduled(cron = "0 0 16 1 * *")  // every first day of month at 16:00
    public void notifyUsersForLongAbsence() {
        log.debug("executing scheduled task @ notifyUsersForLongAbsence...begin");

        try {
        	String baseUrl = getBaseUrlWithPath();
        	
        	List<User> users = userService.findByLongAbsence(LONG_ABSENCE_DAYS); 
        	
        	if(users != null && !users.isEmpty()) {
	            for(User user : users) {
	            	if (user.getEmailVerification().isEmailVerified()) {
                            Map<String,String> _model = new HashMap<String,String>();

                            _model.put("firstName", user.getFirstName());
                            _model.put("lastName", user.getLastName());
                            _model.put("baseurl", baseUrl);

                            if(log.isDebugEnabled()){
                                log.debug("sending email reminder to @" + user.getEmail());
                            }

                            emailService.sendEmail(user.getEmail(), "Seni uzun süredir bifincan'da görmüyorum :(", 
                                            "remind-long-absence", _model);
                        }
	            }
        	}
        	
            log.debug("executing scheduled task @ notifyUsersForLongAbsence...done");

            emailService.sendEmail(mailFrom, JOB_SUBJECT_MSG, SUCCESFUL_LONG_ABSENCE_JOB_MSG);
        } catch (Exception e) {
            log.error(e.getMessage());
            emailService.sendEmail(mailFrom, JOB_SUBJECT_MSG, UNSUCCESFUL_LONG_ABSENCE_JOB_MSG);
        }
    }
 */    


/*
    @Scheduled(cron = "0 0 16 15 * *")  // every 15th day of month at 15:00
    public void notifyUsersForNoInvitationActivity() {
        log.debug("executing scheduled task @ notifyUsersForNoInvitationActivity...begin");

        try {
        	String baseUrl = getBaseUrlWithPath();
        	
        	List<User> users = userService.findByNoInvitationActivity(NO_INV_ACTIVITY_DAYS); 
        	
        	if(users != null && !users.isEmpty()) {
	            for(User user : users) {
	            	if (user.getEmailVerification().isEmailVerified() && user.getActivityLevel() >= 2) {
                            Map<String,String> _model = new HashMap<String,String>();

                            _model.put("firstName", user.getFirstName());
                            _model.put("lastName", user.getLastName());
                            _model.put("baseurl", baseUrl);

                            if(log.isDebugEnabled()){
                                log.debug("sending email reminder to @" + user.getEmail());
                            }

                            emailService.sendEmail(user.getEmail(), "Uzun süredir hiçbir arkadaşını bifincan'a davet etmedin :(", 
                                            "remind-no-invitation-activity", _model);
                        }
	            }
        	}
        	
            log.debug("executing scheduled task @ notifyUsersForNoInvitationActivity...done");

            emailService.sendEmail(mailFrom, JOB_SUBJECT_MSG, SUCCESFUL_NO_INV_ACTIVITY_JOB_MSG);
        } catch (Exception e) {
            log.error(e.getMessage());
            emailService.sendEmail(mailFrom, JOB_SUBJECT_MSG, UNSUCCESFUL_NO_INV_ACTIVITY_JOB_MSG);
        }
    }
 */    
    
    //get the base url of this website
    private String getBaseUrlWithPath() {
        String applicationCanonicalURL = "https://www.bifincan.com";   
        String ctxPath =ServletContextInfo.getContextPath();
        Configuration canonicalUrlConfig = configurationService.find((ConfigurationType.APPLICATION_CANONICAL_URL.getKey()));
        if (canonicalUrlConfig != null) {
            applicationCanonicalURL = canonicalUrlConfig.getValue();
        }
        String baseUrlWithPath = applicationCanonicalURL + ctxPath;
        
        if(log.isDebugEnabled()){
            log.debug("baseurl @"+ baseUrlWithPath);
        }
        
        return baseUrlWithPath;
    }
}
