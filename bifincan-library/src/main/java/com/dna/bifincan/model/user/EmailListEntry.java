package com.dna.bifincan.model.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the email_list database table.
 * 
 */
@Entity
@Table(name="email_list")
public class EmailListEntry implements Serializable {
	private static final long serialVersionUID = -2509615611348577032L;

    @Id
    @Column(name="email", nullable=false, length=250)
	private String email;	
    
    @Column(name="opted_out", nullable=false, columnDefinition="TINYINT(1) unsigned")
    private boolean optedOut = false;
    
    @ManyToOne
    @JoinColumn(name = "email_group")
    private EmailGroup group;
    
    public EmailListEntry() { }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isOptedOut() {
		return optedOut;
	}

	public void setOptedOut(boolean optedOut) {
		this.optedOut = optedOut;
	}

	public EmailGroup getGroup() {
		return group;
	}

	public void setGroup(EmailGroup group) {
		this.group = group;
	}
}
