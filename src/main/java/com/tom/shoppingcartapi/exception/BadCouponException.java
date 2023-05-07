package com.tom.shoppingcartapi.exception;

public class BadCouponException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BadCouponException(String message) {
		super(message);
	}
}
