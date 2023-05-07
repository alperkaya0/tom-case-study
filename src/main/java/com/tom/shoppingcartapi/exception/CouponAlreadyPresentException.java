package com.tom.shoppingcartapi.exception;

public class CouponAlreadyPresentException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CouponAlreadyPresentException(String message) {
		super(message);
	}
}