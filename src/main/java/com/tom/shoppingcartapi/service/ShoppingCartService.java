package com.tom.shoppingcartapi.service;

import java.util.List;
import java.util.HashMap;
import java.util.Optional;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import com.tom.shoppingcartapi.repository.ShoppingCartRepository;

import lombok.AllArgsConstructor;

import com.tom.shoppingcartapi.exception.BadCouponException;
import com.tom.shoppingcartapi.exception.BadItemException;
import com.tom.shoppingcartapi.exception.BadShoppingCartException;
import com.tom.shoppingcartapi.exception.ItemNotFoundException;
import com.tom.shoppingcartapi.exception.ShoppingCartAlreadyPresentException;
import com.tom.shoppingcartapi.exception.ShoppingCartNotFoundException;
import com.tom.shoppingcartapi.model.Coupon;
import com.tom.shoppingcartapi.model.Item;
import com.tom.shoppingcartapi.model.ShoppingCart;

@Service
@AllArgsConstructor
public class ShoppingCartService {
	private final ShoppingCartRepository shoppingCartRepository;

	public List<ShoppingCart> getShoppingCarts() {
		return shoppingCartRepository.findAll(); 
	}
	
	//Because coupons are out of assignment, I will use this function to use my hard-coded coupons
	//This method should be used before validateCoupon
	public void changeCouponToHardCoded(Coupon coupon) {
		if (coupon.getId().equals("coupon1")) {
			coupon.setRate(0);
			coupon.setType("amount");
			coupon.setAmount(50);
		}
		if (coupon.getId().equals("coupon2")) {
			coupon.setRate(0.10);
			coupon.setType("rate");
			coupon.setAmount(0);
		}
		if (coupon.getId().equals("coupon3")) {
			coupon.setRate(0.05);
			coupon.setType("rate");
			coupon.setAmount(0);
		}
	}
	
	public void validateCoupon(Coupon coupon) {
		if (coupon.getType() == null) {
			throw new BadCouponException("Type of a coupon cannot be neither empty nor null.");
		}
		//type cannot be null because of the function above, because of that there is no need to check if it's null or not
		if (!(coupon.getType().toLowerCase().equals("rate") || coupon.getType().toLowerCase().equals("amount"))) {
			throw new BadCouponException("Type of a coupon must be either 'rate' or 'amount'.");
		}
		if (coupon.getRate() < 0 || (coupon.getType().toLowerCase().equals("rate") && (coupon.getRate() == 0) ||  coupon.getRate() > 1 )) {
			throw new BadCouponException("Rate of a coupon of type rate, must be a positive value between 0 and 1.");
		}
		if (coupon.getAmount() < 0 || (coupon.getType().toLowerCase().equals("amount") && coupon.getAmount() == 0)) {
			throw new BadCouponException("Amount of a coupon of type amount, must be a positive value.");
		}
		if (coupon.getType().toLowerCase().equals("rate") && coupon.getAmount() != 0) {
			throw new BadCouponException("Amount of, a coupon of type rate must be zero.");
		}
		if (coupon.getType().toLowerCase().equals("amount") && coupon.getRate() != 0) {
			throw new BadCouponException("Rate of, a coupon of type amount must be zero.");
		}
	}
	
	public void validateItem(Item item) {
		if (item.getUrl() == null) {
			throw new BadItemException("Item url cannot be empty.");
		}
		if (item.getName() == null) {
			throw new BadItemException("Item name cannot be empty.");
		}
		if (item.getPrice() <= 0) {
			throw new BadItemException("Price must be a positive number.");
		}
		if (item.getQuantity() <= 0) {
			throw new BadItemException("Quantity must be a positive number.");
		}
		if (item.getCategory() == null) {
			throw new BadItemException("Category name cannot be empty.");
		}
	}
	
	public void validateWholeShoppingCart(ShoppingCart sc) {
		//Validate prices
		if (sc.getTotalPrice() < 0 || sc.getDiscountedPrice() < 0) {
			throw new BadShoppingCartException("Price must be a positive number. Incorrect shopping cart.");
		}
		//Validate that items have different id
		HashMap<String, Integer> ids = new HashMap<>();
		//I know that using for-each loop would be better for code readibility but using classic for loop protects us from NullPointerException
		for (int i = 0; i < (sc.getItems() == null ? 0 : sc.getItems().size()); ++i) {
			Item item = sc.getItems().get(i);
			//At the same time validate the items in shopping cart so that we won't miss anything and it is faster to like this in terms of Big O Notation
			validateItem(item);
			ids.putIfAbsent(item.getId(), 0);
			ids.put(item.getId(), ids.get(item.getId()) + 1);
		}
		//Check if any id occurred more than once
		for (String key : ids.keySet()) {
			if (ids.get(key) > 1) {
				throw new BadShoppingCartException("There are multiple items with the same id. Incorrect shopping cart.");
			}
		}
		//Validate that coupons have different id
		//No need to create a new HashMap, we can just clear it
		ids.clear();
		//I know that using for-each loop would be better for code readibility but using classic for loop protects us from NullPointerException
		for (int i = 0; i < (sc.getCoupons() == null ? 0 : sc.getCoupons().size()); ++i) {
			Coupon coupon = sc.getCoupons().get(i);
			//if coupon is a hard-coded coupon then user doesn't have to write other than id, we need to fill other parts
			//this won't change the coupon if its id is different
			changeCouponToHardCoded(coupon);
			//At the same time validate the coupons in shopping cart, this is faster to do it alltogether
			validateCoupon(coupon);
			ids.putIfAbsent(coupon.getId(), 0);
			ids.put(coupon.getId(), ids.get(coupon.getId()) + 1);
		}
		for (String key : ids.keySet()) {
			if (ids.get(key) > 1) {
				throw new BadShoppingCartException("There are multiple coupons with the same id. Incorrect shopping cart.");
			}
		}
	}
	
	public ShoppingCart calculatePrices(ShoppingCart shoppingCart) {
		//Total price will be calculated from items list
		double sum = 0;
		List<Item> it = shoppingCart.getItems();
		for (int i = 0; i < it.size(); ++i) {
			sum += it.get(i).getPrice() * it.get(i).getQuantity();
		}
		shoppingCart.setTotalPrice(sum);
		//Discounted price will be calculated with the help of coupons list
		List<Coupon> cp = shoppingCart.getCoupons();
		for (int i = 0; i < (cp == null ? 0 : cp.size()); ++i) {//notice the ternary operation inside for loop. It is there because client can totally ignore coupons list, can send an empty array, can send a non-empty array. If we don't use ternary operator here, then first scenario will cause an error because cp will be null, null.size() doesn't mean anything.
			//Check that coupon structure is correct
			validateCoupon(cp.get(i));
			//check if lower and upper limits suits
			if (sum > cp.get(i).getLowerLimit() 
					&& sum < cp.get(i).getUpperLimit()) {
				//if rate coupon
				if (cp.get(i).getType().toLowerCase().equals("rate")) {
					//rate cannot make our sum <= 0 but amount can, so we need to handle it
					sum = sum - (sum * cp.get(i).getRate());
				//if amount coupon
				} if (cp.get(i).getType().toLowerCase().equals("amount")) {
					//check if expenses are smaller than amount coupons
					//for example you have 3 coupons of type amount, which are 10, 20, 30 but the thing you bought is 50. The logic should return 0 not -10. Imagine giving money to your customers l.o.l.
					if (sum - (cp.get(i).getAmount()) >= 0) {
						sum = sum - (cp.get(i).getAmount());
					} else {
						sum = 0;
					}
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
		// If client didn't send even an empty array as "coupons", then we will initiate a new List, ArrayList is a type of List. We cannot use List because it is not a class, it is an interface
		if (shoppingCart.getCoupons() == null) shoppingCart.setCoupons(new ArrayList<Coupon>());
		// Same applies for items
		if (shoppingCart.getItems() == null) shoppingCart.setItems(new ArrayList<Item>());
		validateWholeShoppingCart(shoppingCart);
		shoppingCart = calculatePrices(shoppingCart);
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
            .orElseThrow(() -> new ShoppingCartNotFoundException("There is no ShoppingCart with that id."));
    }
	
	public void deleteItem(String id, String itemId) {
		ShoppingCart temp = getShoppingCartById(id);
		
		boolean contains = false;
		for (Item i : temp.getItems()) {
			if (i.getId().equals(itemId)) {
				contains = true;
				
				//delete the item
				temp.getItems().remove(i);
				
				//re-calculate prices
				temp = calculatePrices(temp);
				
				//overwrite the old object
				shoppingCartRepository.save(temp);
				break;
			}
		}
		
		if (!contains) {
			throw new ItemNotFoundException("There is no Item with that id.");
		}
	}

	public Coupon applyCoupon(String id, Coupon coupon) {
		//validate that coupon is correct
		changeCouponToHardCoded(coupon);
		validateCoupon(coupon);
		
		ShoppingCart sC = getShoppingCartById(id);
		
		//if coupons list is null
		if (sC.getCoupons() == null) sC.setCoupons(new ArrayList<>());
		
		//append coupon to the list of coupons of the specific shopping cart
		sC.getCoupons().add(coupon);
		
		//validate whole shopping cart, as long as we don't save, it is safe to change sC
		validateWholeShoppingCart(sC);
		
		//re-calculate prices because coupons will affect them
		sC = calculatePrices(sC);
		
		//overwrite the old shopping cart
		shoppingCartRepository.save(sC);
		
		return coupon;
	}

	public List<Item> addNewItemToShoppingCart(String id, Item item) {
		ShoppingCart sC = getShoppingCartById(id);
		
		//Validate that item has correct structure
		validateItem(item);
		
		//Add item to the List<Item>
		sC.getItems().add(item);
		
		//Validate whole shopping cart, as long as we don't save, it is safe to change sC
		validateWholeShoppingCart(sC);
		
		//re-calculate prices because new items will affect them
		sC = calculatePrices(sC);
		
		//Overwrite the old object
		shoppingCartRepository.save(sC);
		
		return sC.getItems();
	}
}