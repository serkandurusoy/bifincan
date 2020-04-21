package com.dna.bifincan.model.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.dna.bifincan.model.BaseEntity;

@Entity
@Table(name="email_campaign")
public class EmailCampaign extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 7061019318366606082L;

    @Column(name="name", nullable=false, length = 250)
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;
    
    @Column(name="active", nullable=false, columnDefinition="TINYINT(1) unsigned")
    private boolean active = false;
    
    @NotNull
    @NotEmpty
    @NotBlank
    @Column(name = "content_subject", nullable = false, length = 250)
    private String contentSubject;    
    
    @NotNull
    @NotEmpty
    @NotBlank
    @Lob
    @Column(name = "content_html_part", nullable = false, columnDefinition = "longtext")
    private String contentHtmlPart;   
    
    @NotNull
    @NotEmpty
    @NotBlank
    @Lob
    @Column(name = "content_text_part", nullable = false, columnDefinition = "longtext")
    private String contentTextPart; 
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_scheduled", nullable = false)
    private Date timeScheduled;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_start", nullable = true)
    private Date timeStart;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_finish", nullable = true)
    private Date timeFinish;
    
    @Column(name="send_to_invitations", nullable=false, columnDefinition="TINYINT(1) unsigned")
    private boolean sendToInvitations = false;
    
    @Column(name="send_to_users", nullable=false, columnDefinition="TINYINT(1) unsigned")
    private boolean sendToUsers = false;
    
    @ManyToOne
    @JoinColumn(name="send_to_group")
    private EmailGroup targetGroup;
    
	public EmailCampaign() { }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getContentSubject() {
		return contentSubject;
	}

	public void setContentSubject(String contentSubject) {
		this.contentSubject = contentSubject;
	}

	public String getContentHtmlPart() {
		return contentHtmlPart;
	}

	public void setContentHtmlPart(String contentHtmlPart) {
		this.contentHtmlPart = contentHtmlPart;
	}

	public String getContentTextPart() {
		return contentTextPart;
	}

	public void setContentTextPart(String contentTextPart) {
		this.contentTextPart = contentTextPart;
	}

	public Date getTimeScheduled() {
		return timeScheduled;
	}

	public void setTimeScheduled(Date timeScheduled) {
		this.timeScheduled = timeScheduled;
	}

	public Date getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public Date getTimeFinish() {
		return timeFinish;
	}

	public void setTimeFinish(Date timeFinish) {
		this.timeFinish = timeFinish;
	}

	public boolean isSendToInvitations() {
		return sendToInvitations;
	}

	public void setSendToInvitations(boolean sendToInvitations) {
		this.sendToInvitations = sendToInvitations;
	}

	public boolean isSendToUsers() {
		return sendToUsers;
	}

	public void setSendToUsers(boolean sendToUsers) {
		this.sendToUsers = sendToUsers;
	}

	public EmailGroup getTargetGroup() {
		return targetGroup;
	}

	public void setTargetGroup(EmailGroup targetGroup) {
		this.targetGroup = targetGroup;
	}
	
	
}
