package com.tom.shoppingcartapi.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import com.tom.shoppingcartapi.repository.ShoppingCartRepository;
import com.tom.shoppingcartapi.exception.ItemNotFoundException;
import com.tom.shoppingcartapi.exception.ShoppingCartAlreadyPresentException;
import com.tom.shoppingcartapi.exception.ShoppingCartNotFoundException;
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
		for (int i = 0; i < (cp == null ? 0 : cp.size()); ++i) {//notice the ternary operation inside for loop. It is there because client can totally ignore coupons list, can send an empty array, can send a non-empty array. If we don't use ternary operator here, then first scenario will cause an error because cp will be null, null.size() doesn't mean anything.
			if (sum > cp.get(i).getLowerLimit() && sum < cp.get(i).getUpperLimit()) {
				sum = sum - (sum * cp.get(i).getRate());
			}
		}
		// If client didn't send even an empty array as "coupons", then we will initiate a new List, ArrayList is a type of List. We cannot use List because it is not a class, it is an interface
		if (cp == null) shoppingCart.setCoupons(new ArrayList<Coupon>());
		shoppingCart.setDiscountedPrice(sum);
		return shoppingCartRepository.save(shoppingCart);
	}

	public List<Item> getItems(String id) {
		Optional<ShoppingCart> sC = shoppingCartRepository.findById(id);
		if (!sC.isPresent()) {
			throw new ShoppingCartNotFoundException("There is no ShoppingCart with that id.");
		}
		
		return sC.get().getItems();
	}

	public void deleteItem(String id, String itemId) {
		Optional<ShoppingCart> sC = shoppingCartRepository.findById(id);
		if (!sC.isPresent()) {
			throw new ShoppingCartNotFoundException("There is no ShoppingCart with that id.");
		}
		
		boolean contains = false;
		for (Item i : sC.get().getItems()) {
			if (i.getId().equals(itemId)) {
				contains = true;
				sC.get().getItems().remove(i);
				break;
			}
		}
		
		if (!contains) {
			throw new ItemNotFoundException("There is no ShoppingCart with that id.");
		}
	}
}