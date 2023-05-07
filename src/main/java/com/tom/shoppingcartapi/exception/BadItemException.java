package com.tom.shoppingcartapi.exception;

public class BadItemException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BadItemException(String message) {
		super(message);
	}
}