package com.dna.bifincan.web.user;

import java.io.Serializable;

import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import com.dna.bifincan.util.spring.ScopeType;

//@SuppressWarnings("serial")
//@Named("currentUser")
//@Scope(ScopeType.SESSION)
public class CurrentUserHolder implements Serializable {
	private static final long serialVersionUID = 8167845919789093779L;
	
	private String username;
    private boolean authenticated = false;
    private Long userId;
    private String firstName;
    private String lastName;

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public boolean isAuthenticated() {
	return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
	this.authenticated = authenticated;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFullNameFirstNameThenLastName()
	{
		return this.firstName + " " + this.lastName;
	}

	public String getFullNameLastNameThenFirstName()
	{
		return this.lastName + " " + this.firstName;
	}
}
