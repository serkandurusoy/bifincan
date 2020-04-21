package com.dna.bifincan.security;

import org.springframework.security.core.AuthenticationException;

public class KaptchaMismatchException extends AuthenticationException {

	public KaptchaMismatchException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3306810486248371322L;

}
