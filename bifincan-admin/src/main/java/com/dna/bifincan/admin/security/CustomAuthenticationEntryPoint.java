/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.admin.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

public class CustomAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    private static final Logger LOG = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
	    throws IOException, ServletException {

	LOG.debug("request.getHeader(\"Faces-Request\")@" + request.getHeader("Faces-Request"));
	if ("partial/ajax".equals(request.getHeader("Faces-Request"))) {
	    LOG.debug("catch jsf ajax request...");
	    LOG.debug("clean up saved request for faces ajax request...");
	    requestCache.removeRequest(request, response);

	    PrintWriter pw = response.getWriter();

	    pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	    pw.println("<partial-response><redirect url=\""
		    + this.buildRedirectUrlToLoginPage(request, response, authException)
		    + "\"></redirect></partial-response>");
	    pw.flush();
	    return;
	}
	super.commence(request, response, authException);
    }

    public void setRequestCache(RequestCache requestCache) {
	this.requestCache = requestCache;
    }

}
