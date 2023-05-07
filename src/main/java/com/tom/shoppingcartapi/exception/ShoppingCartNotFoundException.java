package com.tom.shoppingcartapi.exception;

public class ShoppingCartNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ShoppingCartNotFoundException(String message) {
		super(message);
	}
}
