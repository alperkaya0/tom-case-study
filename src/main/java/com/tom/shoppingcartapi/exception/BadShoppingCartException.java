package com.tom.shoppingcartapi.exception;

public class BadShoppingCartException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BadShoppingCartException(String message) {
		super(message);
	}
}
