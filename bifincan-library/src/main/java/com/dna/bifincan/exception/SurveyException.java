package com.dna.bifincan.exception;

@SuppressWarnings("serial")
public class SurveyException extends Exception {
	public static final int NO_PRODUCT_SURVEY = 2000;
	public static final int NO_QUIZ_SURVEY = 2001;
	
	private int errorCode;
	
	public SurveyException() { 
		super();
	}
	
	public SurveyException(String msg) {
		super(msg);
	}

	public SurveyException(int errorCode, String msg) {
		super(msg);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
}
