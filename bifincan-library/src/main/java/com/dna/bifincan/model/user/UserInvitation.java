package com.dna.bifincan.model.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * The persistent class for the user_invitation database table.
 * 
 */
@Entity
@Table(name = "user_invitation", uniqueConstraints = @UniqueConstraint(name = "UQ_user_email", columnNames = {
	"invited_by", "email" }))
@NamedQueries({
	@NamedQuery(name = "UserInvitation.findByInviterAndEmail", query = "select i from UserInvitation i where i.inviter=:inviter and i.email=:email"),
	@NamedQuery(name = "UserInvitation.findByEmailAndOptedOut", query = "select i from UserInvitation i where  i.email=:email and i.optedOut=true") })
public class UserInvitation extends com.dna.bifincan.model.BaseEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -948291957126288566L;
    
//    @Column(name="position", columnDefinition="bigint", updatable=false, insertable=false)
//    private Long position;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    @NotNull
    private Date createTime = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_invitation_time", nullable = false)
    @NotNull
    private Date lastInvitationTime = new Date();

    @Column(name = "email", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    @Email
    private String email;

    @Column(name = "opted_out", nullable = false, columnDefinition = "TINYINT(1) unsigned")
    private boolean optedOut = false;

    // bi-directional many-to-one association to User
    @ManyToOne
    @JoinColumn(name = "invited_by", nullable = false)
    private User inviter;

    public UserInvitation() {
    }
//
//    public Long getPosition() {
//        return position;
//    }
//
//    public void setPosition(Long position) {
//        this.position = position;
//    }

    public Date getCreateTime() {
	return this.createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

    public String getEmail() {
	return this.email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public Date getLastInvitationTime() {
	return this.lastInvitationTime;
    }

    public void setLastInvitationTime(Date lastInvitationTime) {
	this.lastInvitationTime = lastInvitationTime;
    }

    public boolean getOptedOut() {
	return this.optedOut;
    }

    public void setOptedOut(boolean optedOut) {
	this.optedOut = optedOut;
    }

    public User getInviter() {
	return inviter;
    }

    public void setInviter(User inviter) {
	this.inviter = inviter;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((email == null) ? 0 : email.hashCode());
	result = prime * result + ((inviter == null) ? 0 : inviter.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	UserInvitation other = (UserInvitation) obj;
	if (email == null) {
	    if (other.email != null)
		return false;
	} else if (!email.equals(other.email))
	    return false;
	if (inviter == null) {
	    if (other.inviter != null)
		return false;
	} else if (!inviter.equals(other.inviter))
	    return false;
	return true;
    }

}