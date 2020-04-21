package com.dna.bifincan.exception;

@SuppressWarnings("serial")
public class OrderProcessException extends Exception {
	public static final int NO_PRODUCTS_IN_CART = 1000;
	public static final int NO_RIGHTS_TO_PLACE_ORDER = 1001;
	public static final int NO_AVAILABLE_USER_POINTS = 1002;
	public static final int PRODUCT_IS_NOT_AVAILABLE = 1003;
	public static final int INVALID_NUMBER_OF_PRODUCTS_IN_CART = 1004;
	public static final int LOW_USER_ACTIVITY_LEVEL = 1005;
	
	private int errorCode;
	
	public OrderProcessException() { 
		super();
	}
	
	public OrderProcessException(String msg) {
		super(msg);
	}

	public OrderProcessException(int errorCode, String msg) {
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
