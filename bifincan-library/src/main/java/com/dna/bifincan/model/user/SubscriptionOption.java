/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.model.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class SubscriptionOption implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	@Column(name = "accept_site_mail", nullable = false, columnDefinition = "TINYINT(1) unsigned")
	private boolean acceptSiteMail = true;
	@Column(name = "accept_thirdparty_mail", nullable = false, columnDefinition = "TINYINT(1) unsigned")
	private boolean acceptThirdpartyMail = true;
	@Column(name = "accept_thirdparty_sms", nullable = false, columnDefinition = "TINYINT(1) unsigned")
	private boolean acceptThirdpartySms = true;

	public boolean isAcceptSiteMail() {
		return acceptSiteMail;
	}

	public void setAcceptSiteMail(boolean acceptSiteMail) {
		this.acceptSiteMail = acceptSiteMail;
	}

	public boolean isAcceptThirdpartyMail() {
		return acceptThirdpartyMail;
	}

	public void setAcceptThirdpartyMail(boolean acceptThirdpartyMail) {
		this.acceptThirdpartyMail = acceptThirdpartyMail;
	}

	public boolean isAcceptThirdpartySms() {
		return acceptThirdpartySms;
	}

	public void setAcceptThirdpartySms(boolean acceptThirdpartySms) {
		this.acceptThirdpartySms = acceptThirdpartySms;
	}
}
