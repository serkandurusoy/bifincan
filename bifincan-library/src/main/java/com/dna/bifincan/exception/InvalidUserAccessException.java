package com.dna.bifincan.exception;

public class InvalidUserAccessException extends Exception {
	private static final long serialVersionUID = -7441327546162289274L;

	public InvalidUserAccessException() { 
		super();
	}
	
	public InvalidUserAccessException(String msg) {
		super(msg);
	}
}
