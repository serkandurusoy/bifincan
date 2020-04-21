package com.dna.bifincan.exception;

public class InvalidUserResponseException extends Exception {
	private static final long serialVersionUID = -5932701282762002566L;

	public InvalidUserResponseException() { 
		super();
	}
	
	public InvalidUserResponseException(String msg) {
		super(msg);
	}
}
