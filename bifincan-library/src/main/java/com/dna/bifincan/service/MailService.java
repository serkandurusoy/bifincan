/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.user.EmailCampaign;
import com.dna.bifincan.model.user.EmailGroup;
import com.dna.bifincan.model.user.EmailListEntry;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.repository.user.EmailCampaignRepository;
import com.dna.bifincan.repository.user.EmailListEntryRepository;

@Service
@Named("mailService")
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    @Inject
    private JavaMailSender mailSender;
    @Inject
    private VelocityEngine velocityEngine;
    @Inject
    private ConfigurationService configurationService;
    @Inject
    private EmailCampaignRepository emailCampaignRepository;
    @Inject
    private EmailListEntryRepository emailListEntryRepository;
    @Inject
    private PasswordEncoder encoder;

    public String getApplicationLocale() {
        String applicationLocaleString = "tr";
        Configuration applicationLocaleStringConfig = configurationService.find((ConfigurationType.APPLICATION_LOCALE.getKey()));
        if (applicationLocaleStringConfig != null) {
            applicationLocaleString = applicationLocaleStringConfig.getValue();
        }
        return applicationLocaleString;
    }
    @Value("${mail.from}")
    private String from;
    private final static String TEMPLATE_PATH_PREFIX = "/mails/";
    private final static String HTML_TEMPLATE_SUFFIX = "-html-";
    private final static String TEXT_TEMPLATE_SUFFIX = "-text-";
    private final static String TEMPLATE_EXTENSION = ".vm";

    public void sendEmail(final String to, final String subject, final String velocityTemplateName, final Map model)
            throws MailException {
        this.sendEmail(to, null, subject, velocityTemplateName, model);
    }

    @SuppressWarnings("rawtypes")
    @Async
    public void sendEmail(final String to, final String replyTo, final String subject, final String velocityTemplateName, final Map model)
            throws MailException {
        if (log.isDebugEnabled()) {
            log.debug("send email @ to ->" + to + ", subject->" + subject + ", template path->" + velocityTemplateName
                    + ", model->" + model);
        }

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                message.setTo(to);
                message.setSubject(subject);
                message.setFrom(new InternetAddress(from, "bifincan"));
                if (replyTo != null) {
                    message.setReplyTo(replyTo);
                }
                message.setSentDate(new Date());

                final String textTemplatePath = TEMPLATE_PATH_PREFIX + velocityTemplateName + TEXT_TEMPLATE_SUFFIX + getApplicationLocale() + TEMPLATE_EXTENSION;
                final String htmlTemplatePath = TEMPLATE_PATH_PREFIX + velocityTemplateName + HTML_TEMPLATE_SUFFIX + getApplicationLocale() + TEMPLATE_EXTENSION;

                String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, textTemplatePath, "UTF-8", model);
                String html = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, htmlTemplatePath, "UTF-8", model);
                if (log.isDebugEnabled()) {
                    log.debug(">>>>>>>message content @" + text);
                    log.debug(">>>>>>>message content@html @" + html);
                }
                message.setText(text, html);
            }
        };
        this.mailSender.send(preparator);
    }

    @Async
    public void sendEmail(final String to, final String subject, final String content)
            throws MailException {
        if (log.isDebugEnabled()) {
            log.debug("send email @ to ->" + to + ", subject->" + subject + ", template path->" + content);
        }

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                message.setTo(to);
                message.setSubject(subject);
                message.setFrom(new InternetAddress(from, "bifincan"));
                message.setSentDate(new Date());
                message.setText(content, false);
            }
        };
        this.mailSender.send(preparator);
    }

    @Async
    public void sendEmail(final String to, final String subject, final String htmlContent,
            final String plainContent) throws MailException {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                message.setTo(to);
                message.setSubject(subject);
                message.setFrom(new InternetAddress(from, "bifincan"));
                message.setSentDate(new Date());
                message.setText(plainContent, htmlContent);
            }
        };
        this.mailSender.send(preparator);
    }

    @Async
    public void sendEmailBatchForNotOptedOutInvitations(EmailCampaign campaign, List<String> emails, String optoutLink) throws MailException {
        if (emails != null && !emails.isEmpty()) {
            final String subject = campaign.getContentSubject();
            final String[] placeholders = new String[]{"@@optoutofallinvitations@@"};

            MimeMessagePreparator[] preps = new MimeMessagePreparator[emails.size()];
            int count = 0;

            for (String targetEmail : emails) {
            	log.debug("@@@ 1.sending to " + targetEmail);
                final String to = targetEmail;
                final String[] replacements = new String[]{replaceAllPlaceHolders(optoutLink,
                    new String[]{"@@target_email@@", "@@campaign_id@@", "@@encoded_key@@"},
                    new String[]{targetEmail,
                        String.valueOf(campaign.getId()),
                        encoder.encodePassword(targetEmail, campaign.getTimeScheduled())})};
                final String htmlContent = replaceAllPlaceHolders(campaign.getContentHtmlPart(), placeholders, replacements);
                final String plainContent = replaceAllPlaceHolders(campaign.getContentTextPart(), placeholders, replacements);
                MimeMessagePreparator preparator = new MimeMessagePreparator() {
                    @Override
                    public void prepare(MimeMessage mimeMessage) throws Exception {
                        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                        message.setTo(to);
                        message.setSubject(subject);
                        message.setFrom(new InternetAddress(from, "bifincan"));
                        message.setSentDate(new Date());
                        message.setText(plainContent, htmlContent);
                    }
                };

                preps[count++] = preparator;
                log.debug("@@@ 1.goto .. next target");
            }

            this.mailSender.send(preps);
        }
    }

    @Async
    public void sendEmailBatchForActiveSiteEmailAcceptedUsers(EmailCampaign campaign, List<User> users) throws MailException {
        if (users != null && !users.isEmpty()) {
            final String subject = campaign.getContentSubject();
            final String[] placeholders = new String[]{"@@firstname@@", "@@lastname@@"};

            MimeMessagePreparator[] preps = new MimeMessagePreparator[users.size()];
            int count = 0;

            for (User targetUser : users) {
            	log.debug("@@@ 2.sending to " + targetUser.getEmail());
                final String to = targetUser.getEmail();
                final String[] replacements = new String[]{targetUser.getFirstName(), targetUser.getLastName()};
                final String htmlContent = replaceAllPlaceHolders(campaign.getContentHtmlPart(), placeholders, replacements);
                final String plainContent = replaceAllPlaceHolders(campaign.getContentTextPart(), placeholders, replacements);
                MimeMessagePreparator preparator = new MimeMessagePreparator() {
                    @Override
                    public void prepare(MimeMessage mimeMessage) throws Exception {
                        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                        message.setTo(to);
                        message.setSubject(subject);
                        message.setFrom(new InternetAddress(from, "bifincan"));
                        message.setSentDate(new Date());
                        message.setText(plainContent, htmlContent);
                    }
                };

                preps[count++] = preparator;
                log.debug("@@@ 2.goto .. next target");
            }
            this.mailSender.send(preps);
        }
    }

    @Async
    public void sendEmailBatchForGroup(EmailCampaign campaign, EmailGroup group, String optoutLink) throws MailException {
        if (group == null) {
            return;
        }

        List<EmailListEntry> entries = findEmailsByGroup(group);

        if (entries != null && !entries.isEmpty()) {
            final String subject = campaign.getContentSubject();
            final String[] placeholders = new String[]{"@@optoutlink@@"};

            MimeMessagePreparator[] preps = new MimeMessagePreparator[entries.size()];
            int count = 0;

            for (EmailListEntry targetEntry : entries) {
            	log.debug("@@@ 3.sending to " + targetEntry.getEmail());
                final String to = targetEntry.getEmail();
                final String[] replacements = new String[]{replaceAllPlaceHolders(optoutLink,
                    new String[]{"@@target_email@@", "@@campaign_id@@", "@@encoded_key@@"},
                    new String[]{targetEntry.getEmail(),
                        String.valueOf(campaign.getId()),
                        encoder.encodePassword(targetEntry.getEmail(), campaign.getTimeScheduled())})};
                final String htmlContent = replaceAllPlaceHolders(campaign.getContentHtmlPart(), placeholders, replacements);
                final String plainContent = replaceAllPlaceHolders(campaign.getContentTextPart(), placeholders, replacements);
                MimeMessagePreparator preparator = new MimeMessagePreparator() {
                    @Override
                    public void prepare(MimeMessage mimeMessage) throws Exception {
                        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                        message.setTo(to);
                        message.setSubject(subject);
                        message.setFrom(new InternetAddress(from, "bifincan"));
                        message.setSentDate(new Date());
                        message.setText(plainContent, htmlContent);
                    }
                };

                preps[count++] = preparator;
                log.debug("@@@ 3.goto .. next target");
            }

            this.mailSender.send(preps);
        }
    }

    private String replaceAllPlaceHolders(String msg, String[] placeholders, String[] replacements) {
        if (msg == null || placeholders == null || replacements == null) {
            return msg;
        }
        if (placeholders.length != replacements.length) {
            throw new RuntimeException("Error replacement");
        }

        for (int i = 0; i < placeholders.length; i++) {
            msg = msg.replaceAll(placeholders[i], replacements[i]);
        }

        return msg;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<EmailCampaign> findPastScheduledCampaigns(Date scheduledDate) {
        return emailCampaignRepository.findPastScheduledCampaignsByDate(scheduledDate);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<EmailListEntry> findEmailsByGroup(EmailGroup group) {
        return emailListEntryRepository.findByGroup(group);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void initiateEmailCampaignTask(EmailCampaign campaign) {
        EmailCampaign originCampaign = emailCampaignRepository.findOne(campaign.getId()); // get a fresh/valid reference to entity
        if (originCampaign != null) {
            originCampaign.setTimeStart(new Date());
            emailCampaignRepository.save(originCampaign);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void finalizeEmailCampaignTask(EmailCampaign campaign) {
        EmailCampaign originCampaign = emailCampaignRepository.findOne(campaign.getId()); // get a fresh/valid reference to entity
        if (originCampaign != null) {
            originCampaign.setTimeFinish(new Date());
            emailCampaignRepository.save(originCampaign);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void optOutEmailFromList(String email) {
        List<EmailListEntry> entries = emailListEntryRepository.findByEmail(email);
        if (entries != null && !entries.isEmpty()) {
            for (EmailListEntry entry : entries) {
                entry.setOptedOut(true);
                emailListEntryRepository.save(entry);
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public EmailCampaign findEmailCampaign(long id) {
        return emailCampaignRepository.findOne(id);
    }
}
