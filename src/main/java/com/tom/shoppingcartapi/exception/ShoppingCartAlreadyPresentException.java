package com.tom.shoppingcartapi.exception;

public class ShoppingCartAlreadyPresentException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShoppingCartAlreadyPresentException(String message) {
		super(message);
	}
}
