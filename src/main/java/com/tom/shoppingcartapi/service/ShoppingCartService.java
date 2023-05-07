package com.tom.shoppingcartapi.service;
import java.util.List;
import org.springframework.stereotype.Service;
import com.tom.shoppingcartapi.repository.ShoppingCartRepository;
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
}