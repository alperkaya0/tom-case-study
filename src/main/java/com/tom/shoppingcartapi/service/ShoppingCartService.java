package com.tom.shoppingcartapi.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import com.tom.shoppingcartapi.repository.ShoppingCartRepository;
import com.tom.shoppingcartapi.exception.BadCouponException;
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

	public ShoppingCart calculatePrices(ShoppingCart shoppingCart) {
		//Total price will be calculated from items list
		double sum = 0;
		List<Item> it = shoppingCart.getItems();
		for (int i = 0; i < it.size(); ++i) {
			sum += it.get(i).getPrice();
		}
		shoppingCart.setTotalPrice(sum);
		//Discounted price will be calculated with the help of coupons list
		List<Coupon> cp = shoppingCart.getCoupons();
		for (int i = 0; i < (cp == null ? 0 : cp.size()); ++i) {//notice the ternary operation inside for loop. It is there because client can totally ignore coupons list, can send an empty array, can send a non-empty array. If we don't use ternary operator here, then first scenario will cause an error because cp will be null, null.size() doesn't mean anything.
			if (sum > cp.get(i).getLowerLimit() && sum < cp.get(i).getUpperLimit()) {
				if (cp.get(i).getType().toLowerCase().equals("rate")) {
					sum = sum - (sum * cp.get(i).getRate());
				} else if (cp.get(i).getType().toLowerCase().equals("amount")) {
					sum = sum - (cp.get(i).getAmount());
				} else {
					throw new BadCouponException("Coupon type is wrong, therefore provided coupon is incorrect.");
				}
			}
		}
		shoppingCart.setDiscountedPrice(sum);
		return shoppingCart;
	}
	
	public ShoppingCart createNewShoppingCart(ShoppingCart shoppingCart) {
		Optional<ShoppingCart> sCById = shoppingCartRepository.findByCustomerId(shoppingCart.getCustomerId());
		if (sCById.isPresent()) {
			throw new ShoppingCartAlreadyPresentException("Customer already has a shopping cart. You may want to update it or delete then recreate it.");
		}
		shoppingCart = calculatePrices(shoppingCart);
		// If client didn't send even an empty array as "coupons", then we will initiate a new List, ArrayList is a type of List. We cannot use List because it is not a class, it is an interface
		if (shoppingCart.getCoupons() == null) shoppingCart.setCoupons(new ArrayList<Coupon>());
		return shoppingCartRepository.save(shoppingCart);
	}

	public List<Item> getItems(String id) {
		Optional<ShoppingCart> sC = shoppingCartRepository.findById(id);
		if (!sC.isPresent()) {
			throw new ShoppingCartNotFoundException("There is no ShoppingCart with that id.");
		}
		
		return sC.get().getItems();
	}

	public ShoppingCart getShoppingCartById(String id) {
        return shoppingCartRepository.findById(id)
            .orElseThrow(() -> new ShoppingCartNotFoundException("There is no ShoppingCart with that id: " + id));
    }
	
	public void deleteItem(String id, String itemId) {
		ShoppingCart temp = getShoppingCartById(id);
		
		boolean contains = false;
		for (Item i : temp.getItems()) {
			if (i.getId().equals(itemId)) {
				contains = true;
				
				//re-calculate prices
				temp = calculatePrices(temp);
				
				//delete the item
				temp.getItems().remove(i);
				
				//overwrite the old object
				shoppingCartRepository.save(temp);
				break;
			}
		}
		
		if (!contains) {
			throw new ItemNotFoundException("There is no Item with that id.");
		}
	}

	public List<Coupon> applyCoupon(String id, Coupon coupon) {
		ShoppingCart sC = getShoppingCartById(id);
		
		//append coupon to the list of coupons of the specific shopping cart
		sC.getCoupons().add(coupon);
		
		//re-calculate prices because coupons will affect them
		sC = calculatePrices(sC);
		
		//overwrite the old shopping cart
		shoppingCartRepository.save(sC);
		
		return sC.getCoupons();
	}

	public List<Item> addNewItemToShoppingCart(String id, Item item) {
		ShoppingCart sC = getShoppingCartById(id);
		
		//Validate that item has correct structure!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		//Add item to the List<Item>
		sC.getItems().add(item);
		
		//re-calculate prices because new items will affect them
		sC = calculatePrices(sC);
		
		//Overwrite the old object
		shoppingCartRepository.save(sC);
		
		return sC.getItems();
	}
}