package com.dna.bifincan.security;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.model.user.User;
import com.dna.bifincan.service.UserService;

/**
 * An implementation of Spring Security's UserDetailsService.
 */
@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Inject
    private UserService userService;

    public UserDetailsServiceImpl() {
    }

    /**
     * Retrieve an User depending on its login this method is not case sensitive.<br>
     * use <code>obtainUser</code> to match the login to either email, login or whatever is your login logic
     * 
     * @param login the User login
     * @return a Spring Security userdetails object that matches the login
     * @see #obtainUser(String)
     * @throws UsernameNotFoundException when the user could not be found
     * @throws DataAccessException when an error occured while retrieving the User
     */
    @SuppressWarnings("deprecation")
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException, DataAccessException {
	if (login == null || login.trim().isEmpty()) {
	    throw new UsernameNotFoundException("Empty login");
	}

	if (logger.isDebugEnabled()) {
	    logger.debug("Security verification for user '" + login + "'");
	}

	User _loginUser = userService.findUserByUsername(login.toLowerCase());

	if (_loginUser == null) {
	    if (logger.isDebugEnabled()) {
		logger.debug("User " + login + " could not be found");
	    }
	    throw new UsernameNotFoundException("User " + login + " could not be found");
	}
        
        return _loginUser;

//	String password = _loginUser.getPassword();
//
//	GrantedAuthority[] grantedAuthorities = new GrantedAuthority[] { new GrantedAuthorityImpl("ROLE_USER") };
//
//	boolean enabled = _loginUser.isActive();
//	boolean userNonExpired = true;
//	boolean credentialsNonExpired = true;
//	boolean userNonLocked = true;
//
//	return new org.springframework.security.core.userdetails.User(login, password, enabled, userNonExpired,
//		credentialsNonExpired, userNonLocked, grantedAuthorities);
    }
}