package com.tom.shoppingcartapi.exception;

public class ShoppingCartAlreadyPresentException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShoppingCartAlreadyPresentException(String message) {
		super(message);
	}
}
