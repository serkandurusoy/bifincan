package com.dna.bifincan.admin.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dna.bifincan.model.user.AdminUser;
import com.dna.bifincan.admin.util.WebConstants;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(CustomUsernamePasswordAuthenticationFilter.class);

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            Authentication authResult) throws IOException, ServletException {

        saveUserDetailsInSession(request, authResult);
        super.successfulAuthentication(request, response, authResult);

    }

    private void saveUserDetailsInSession(HttpServletRequest request,
            Authentication authResult) {
        final AdminUser _userDetail = (AdminUser) authResult.getPrincipal();
        
        
        request.getSession(true).setAttribute(WebConstants.CURRENT_USER_SESSION_KEY, _userDetail);
    }


}
