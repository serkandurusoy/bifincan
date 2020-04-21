package com.dna.bifincan.exception;

@SuppressWarnings("serial")
public class DuplicateUserResponseException extends Exception {
	public static final int DEFAULT_ERROR = 0;
	public static final int DUPL_BRAND_ERROR = 500;
	public static final int DUPL_BRAND_AND_PROD_CATEGORY_ERROR = 501;
	public static final int DUPL_PROD_CATEGORY_ERROR = 502;
	public static final int DUPL_PROFILE_ERROR = 503;
	
	private int errorCode;
	
	public DuplicateUserResponseException() { 
		super();
	}
	
	public DuplicateUserResponseException(String msg) {
		super(msg);
	}

	public DuplicateUserResponseException(int errorCode, String msg) {
		super(msg);
	}
	
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
}
