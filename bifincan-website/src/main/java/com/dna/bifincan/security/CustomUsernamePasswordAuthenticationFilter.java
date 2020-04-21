package com.dna.bifincan.security;

import java.io.IOException;
import java.util.Date;

import javax.persistence.Transient;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.dna.bifincan.model.order.ShoppingCart;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.model.user.UserLogin;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.WebConstants;

public class CustomUsernamePasswordAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {

	private static final Logger log = LoggerFactory
			.getLogger(CustomUsernamePasswordAuthenticationFilter.class);
	private static final String KAPTCHA_FIELD_KEY = "j_kaptcha";

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult)
			throws IOException, ServletException {

		saveUserDetailsInSession(request, authResult);
		storeUserLoginStatus(request, authResult);
		// remove counter
		removeAttemptCounter(request);

		super.successfulAuthentication(request, response, authResult);

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		if (log.isDebugEnabled()) {
			log.debug("overriding attemptAuthentication...");
		}

		String kaptcha = obtainKaptchaResponse(request);
		if (log.isDebugEnabled()) {
			log.debug("response value@" + kaptcha);
		}

		// set attempt counter.
		saveAttemptCounter(request);

		if (requiresInputKaptcha(request)
				&& (kaptcha == null || kaptcha.trim().length() == 0)) {
			throw new KaptchaMismatchException("Kaptcha value mismatch") ;
		}

		if (kaptcha != null && kaptcha.trim().length() > 0) {
			String expected = obtainKaptchaExpected(request);
			if (log.isDebugEnabled()) {
				log.debug("expected value@" + expected);
			}
			if (!kaptcha.equals(expected)) {
				throw new KaptchaMismatchException("Kaptcha value mismatch") ;
			}
		}

		return super.attemptAuthentication(request, response);
	}

	private boolean requiresInputKaptcha(HttpServletRequest request) {
		Integer counter = (Integer) request.getSession().getAttribute(
				WebConstants.ATTEMPT_COUNTER);
		return counter != null && counter.intValue() > 4;
	}

	private void removeAttemptCounter(HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("removeing attemp counter");
		}
		request.getSession().removeAttribute(WebConstants.ATTEMPT_COUNTER);
	}

	private void saveAttemptCounter(HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("add attemp counter");
		}
		HttpSession session = request.getSession();
		Integer counter = (Integer) session
				.getAttribute(WebConstants.ATTEMPT_COUNTER);
		if (log.isDebugEnabled()) {
			log.debug("old counter value@" + counter);
		}
		if (counter == null) {
			counter = new Integer(0);
		}

		counter = Integer.valueOf(counter.intValue() + 1);

		if (log.isDebugEnabled()) {
			log.debug("new counter value@" + counter);
		}

		session.setAttribute(WebConstants.ATTEMPT_COUNTER, counter);
	}

	private String obtainKaptchaResponse(HttpServletRequest request) {
		return request.getParameter(KAPTCHA_FIELD_KEY);
	}

	private String obtainKaptchaExpected(HttpServletRequest request) {
		return (String) request.getSession().getAttribute(
				com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
	}

	private void saveUserDetailsInSession(HttpServletRequest request,
			Authentication authResult) {
		final User _userDetail = (User) authResult.getPrincipal();

		// store a few information about the current user accessible from all
		// pages (and code)
		_userDetail.setNoOfOrderableProducts(userService
				.getNoOfOrderableProducts(_userDetail.getActivityLevel()));
		_userDetail.setShoppingCart(new ShoppingCart());

		request.getSession(true).setAttribute(
				WebConstants.CURRENT_USER_SESSION_KEY, _userDetail);
	}

	private void storeUserLoginStatus(HttpServletRequest request,
			Authentication authResult) {
		final UserDetails _userDetail = (UserDetails) authResult.getPrincipal();

		if (log.isDebugEnabled()) {
			log.debug("@@@call storeUserLoginStatus in CustomAuthenticationProcessingFilter@@@");
			log.debug("authResult @" + authResult);
			log.debug("username @" + (_userDetail).getUsername());
		}

		User user = userService.findUserByUsername((_userDetail).getUsername());
		int pointsBalance = userService
				.saveUserLoginStatus(new UserLogin(
						((WebAuthenticationDetails) ((UsernamePasswordAuthenticationToken) authResult)
								.getDetails()).getRemoteAddress(), new Date(),
						user));
		((User) request.getSession(true).getAttribute(
				WebConstants.CURRENT_USER_SESSION_KEY))
				.setPointsBalance(pointsBalance);

		request.getSession(true).setAttribute(
				WebConstants.NEED_FOR_NEW_SURVEY_CYCLE,
				userService.checkForNewSurveyCycleByUser(user) == 1 ? true
						: false);
	}

	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
