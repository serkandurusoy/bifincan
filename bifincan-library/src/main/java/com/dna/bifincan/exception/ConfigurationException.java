package com.dna.bifincan.exception;

@SuppressWarnings("serial")
public class ConfigurationException extends Exception {
	public ConfigurationException() { 
		super();
	}
	
	public ConfigurationException(String msg) {
		super(msg);
	}
}
