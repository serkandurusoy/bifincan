package com.dna.bifincan.exception;

@SuppressWarnings("serial")
public class NoWelcomeProductsException extends Exception {
	public NoWelcomeProductsException() { 
		super();
	}
	
	public NoWelcomeProductsException(String msg) {
		super(msg);
	}
}
