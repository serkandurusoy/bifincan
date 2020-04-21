package com.dna.bifincan.model.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * The persistent class for the user_login database table.
 * 
 */
@Entity
@Table(name = "user_login")
public class UserLogin extends com.dna.bifincan.model.BaseEntity implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -477850049711761505L;

    @Column(name = "ip_address", nullable = false, length = 15)
    @NotNull
    @NotEmpty
    private String ipAddress;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "login_time", nullable = false)
    private Date loginTime;

    // bi-directional many-to-one association to User
    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    private User user;

    public UserLogin() {
    }

    public UserLogin(String ipAddress, Date loginTime, User user) {
	super();
	this.ipAddress = ipAddress;
	this.loginTime = loginTime;
	this.user = user;
    }

    public String getIpAddress() {
	return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
	this.ipAddress = ipAddress;
    }

    public Date getLoginTime() {
	return this.loginTime;
    }

    public void setLoginTime(Date loginTime) {
	this.loginTime = loginTime;
    }

    public User getUser() {
	return this.user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final UserLogin other = (UserLogin) obj;
	if ((this.ipAddress == null) ? (other.ipAddress != null) : !this.ipAddress.equals(other.ipAddress)) {
	    return false;
	}
	if (this.loginTime != other.loginTime && (this.loginTime == null || !this.loginTime.equals(other.loginTime))) {
	    return false;
	}
	if (this.user != other.user && (this.user == null || !this.user.equals(other.user))) {
	    return false;
	}
	return true;
    }

    @Override
    public int hashCode() {
	int hash = 5;
	hash = 17 * hash + (this.ipAddress != null ? this.ipAddress.hashCode() : 0);
	hash = 17 * hash + (this.loginTime != null ? this.loginTime.hashCode() : 0);
	hash = 17 * hash + (this.user != null ? this.user.hashCode() : 0);
	return hash;
    }

}