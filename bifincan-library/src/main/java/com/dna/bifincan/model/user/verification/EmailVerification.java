/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.model.user.verification;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Embeddable
public class EmailVerification implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "email_verification_sent_time", nullable = false)
    private Date emailVerificationSentTime;

    @Column(name = "email_verified", nullable = false, columnDefinition = "TINYINT(1) unsigned")
    private boolean emailVerified = false;

    public Date getEmailVerificationSentTime() {
	return emailVerificationSentTime;
    }

    public void setEmailVerificationSentTime(Date emailVerificationSentTime) {
	this.emailVerificationSentTime = emailVerificationSentTime;
    }

    public boolean isEmailVerified() {
	return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
	this.emailVerified = emailVerified;
    }

    @Override
    public String toString() {
	return "EmailVerification [emailVerificationSentTime=" + emailVerificationSentTime + ", emailVerified="
		+ emailVerified + "]";
    }

}
