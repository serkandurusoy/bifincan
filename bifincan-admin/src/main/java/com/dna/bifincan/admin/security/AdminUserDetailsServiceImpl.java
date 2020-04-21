package com.dna.bifincan.admin.security;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.model.user.AdminUser;
import com.dna.bifincan.repository.user.AdminUserRepository;

/**
 * An implementation of Spring Security's UserDetailsService.
 */
@Service(value = "adminUserDetailsService")
public class AdminUserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserDetailsServiceImpl.class);

    @Inject
    private AdminUserRepository adminUserService;

    public AdminUserDetailsServiceImpl() {
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException, DataAccessException {
	if (login == null || login.trim().isEmpty()) {
	    throw new UsernameNotFoundException("Empty login");
	}

	if (logger.isDebugEnabled()) {
	    logger.debug("Security verification for user '" + login + "'");
	}

	AdminUser _loginUser = adminUserService.findByUsername(login.toLowerCase());

	if (_loginUser == null) {
	    if (logger.isDebugEnabled()) {
		logger.debug("User " + login + " could not be found");
	    }
	    throw new UsernameNotFoundException("User " + login + " could not be found");
	}
        
        return _loginUser;

    }
}