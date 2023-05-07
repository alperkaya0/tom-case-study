package com.tom.shoppingcartapi.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.tom.shoppingcartapi.repository.ShoppingCartRepository;
import com.tom.shoppingcartapi.exception.ShoppingCartAlreadyPresentException;
import com.tom.shoppingcartapi.model.ShoppingCart;

@Service
public class ShoppingCartService {
	private final ShoppingCartRepository shoppingCartRepository;
	
	public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
		this.shoppingCartRepository = shoppingCartRepository;
	}

	public List<ShoppingCart> getShoppingCarts() {
		return shoppingCartRepository.findAll();
	}

	public ShoppingCart createNewShoppingCart(ShoppingCart shoppingCart) {
		Optional<ShoppingCart> sCById = shoppingCartRepository.findByCustomerId(shoppingCart.getCustomerId());
		if (sCById.isPresent()) {
			throw new ShoppingCartAlreadyPresentException("Customer has already a shopping cart. You may want to update it or delete then recreate it.");
		}
		
		return shoppingCartRepository.save(shoppingCart);
	}
}