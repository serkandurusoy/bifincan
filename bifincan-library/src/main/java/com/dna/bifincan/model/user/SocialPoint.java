package com.dna.bifincan.model.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.dna.bifincan.model.BaseEntity;

@Entity
@Table(name = "social_point", uniqueConstraints = @UniqueConstraint(name = "UQ_entry", columnNames = { "url", "action", "user" }))
public class SocialPoint extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -1648646751588092094L;

	@Column(name = "url", nullable = false, length = 250)
    private String url;

	@Column(name = "action", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private SocialPointType action;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    private User user;

    public SocialPoint() { }    
    
	public SocialPoint(String url, SocialPointType action, User user) {
		super();
		this.url = url;
		this.action = action;
		this.user = user;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public SocialPointType getAction() {
		return action;
	}

	public void setAction(SocialPointType action) {
		this.action = action;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "SocialPoint [url=" + url + ", action=" + action.toString() + ", user="
				+ user.getId() + "]";
	}

}