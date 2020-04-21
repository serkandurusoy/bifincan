/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.model.user.verification;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import com.dna.bifincan.model.gsm.GsmOperator;
import com.dna.bifincan.model.gsm.GsmPrefix;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;


@Embeddable
public class GsmVerification implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "gsm_number", nullable = false, length = 7)
    private String gsmNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "gsm_verification_sent_time", nullable = false)
    private Date gsmVerificationSentTime;
    
    @Column(name = "gsm_verified", nullable = false, columnDefinition = "TINYINT(1) unsigned")
    private boolean gsmVerified = false;

    @ManyToOne(cascade = { CascadeType.MERGE })
    @JoinColumn(name = "gsm_operator", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private GsmOperator gsmOperator;

    @ManyToOne
    @JoinColumn(name = "gsm_prefix", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private GsmPrefix gsmPrefix;

    public String getGsmNumber() {
	return gsmNumber;
    }

    public void setGsmNumber(String gsmNumber) {
	this.gsmNumber = gsmNumber;
    }

    public GsmOperator getGsmOperator() {
	return gsmOperator;
    }

    public void setGsmOperator(GsmOperator gsmOperator) {
	this.gsmOperator = gsmOperator;
    }

    public GsmPrefix getGsmPrefix() {
	return gsmPrefix;
    }

    public void setGsmPrefix(GsmPrefix gsmPrefix) {
	this.gsmPrefix = gsmPrefix;
    }

    public Date getGsmVerificationSentTime() {
	return gsmVerificationSentTime;
    }

    public void setGsmVerificationSentTime(Date gsmVerificationSentTime) {
	this.gsmVerificationSentTime = gsmVerificationSentTime;
    }

    public boolean isGsmVerified() {
	return gsmVerified;
    }

    public void setGsmVerified(boolean gsmVerified) {
	this.gsmVerified = gsmVerified;
    }

    @Override
    public String toString() {
	return "GsmVerification [gsmNumber=" + gsmNumber + ", gsmVerificationSentTime=" + gsmVerificationSentTime
		+ ", gsmVerified=" + gsmVerified + ", gsmOperator=" + gsmOperator + ", gsmPrefix=" + gsmPrefix + "]";
    }

}
