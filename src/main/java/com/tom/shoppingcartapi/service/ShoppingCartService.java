package com.tom.shoppingcartapi.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.tom.shoppingcartapi.repository.ShoppingCartRepository;
import com.tom.shoppingcartapi.exception.ShoppingCartAlreadyPresentException;
import com.tom.shoppingcartapi.model.Coupon;
import com.tom.shoppingcartapi.model.Item;
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
			throw new ShoppingCartAlreadyPresentException("Customer already has a shopping cart. You may want to update it or delete then recreate it.");
		}
		//Total price will be calculated from items list
		double sum = 0;
		List<Item> it = shoppingCart.getItems();
		for (int i = 0; i < it.size(); ++i) {
			sum += it.get(i).getPrice();
		}
		shoppingCart.setTotalPrice(sum);
		//Discounted price will be calculated from coupons list
		List<Coupon> cp = shoppingCart.getCoupons();
		for (int i = 0; i < cp.size(); ++i) {
			if (sum > cp.get(i).getLowerLimit() && sum < cp.get(i).getUpperLimit()) {
				sum = sum - (sum * cp.get(i).getRate());
			}
		}
		shoppingCart.setDiscountedPrice(sum);
		return shoppingCartRepository.save(shoppingCart);
	}
}