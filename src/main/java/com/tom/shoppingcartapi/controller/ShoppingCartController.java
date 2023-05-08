package com.tom.shoppingcartapi.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tom.shoppingcartapi.exception.BadCouponException;
import com.tom.shoppingcartapi.exception.BadItemException;
import com.tom.shoppingcartapi.exception.BadShoppingCartException;
import com.tom.shoppingcartapi.exception.CouponAlreadyPresentException;
import com.tom.shoppingcartapi.exception.ItemAlreadyPresentException;
import com.tom.shoppingcartapi.exception.ItemNotFoundException;
import com.tom.shoppingcartapi.exception.ShoppingCartAlreadyPresentException;
import com.tom.shoppingcartapi.exception.ShoppingCartNotFoundException;
import com.tom.shoppingcartapi.model.Coupon;
import com.tom.shoppingcartapi.model.Item;
import com.tom.shoppingcartapi.model.ShoppingCart;
import com.tom.shoppingcartapi.service.ShoppingCartService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/shopping-carts")
@AllArgsConstructor
public class ShoppingCartController {
	private final ShoppingCartService shoppingCartService;
	
	//End-points
	//User can see the original price and discount price(by looking at totalPrice and discountedPrice attributes)
	@GetMapping
	public ResponseEntity<List<ShoppingCart>> getShoppingCarts() {
		return new ResponseEntity<>(shoppingCartService.getShoppingCarts(), HttpStatus.OK);
	}
	
	//User can list all items in the shopping cart
	@GetMapping("/{id}/items")
	public ResponseEntity<List<Item>> getAllItemsInShoppingCart(@PathVariable String id) {
		return new ResponseEntity<List<Item>>(shoppingCartService.getItems(id), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<ShoppingCart> addNewShoppingCart(@RequestBody ShoppingCart shoppingCart) {
		return new ResponseEntity<>(shoppingCartService.createNewShoppingCart(shoppingCart), HttpStatus.CREATED);
	}
	
	//User can add a new item to shopping cart
	@PostMapping("/{id}/items")
	public ResponseEntity<List<Item>> addNewItemToShoppingCart(@PathVariable String id, @RequestBody Item item) {
		return new ResponseEntity<>(shoppingCartService.addNewItemToShoppingCart(id, item), HttpStatus.CREATED);
	}
	
	//User can apply a coupon to shopping cart
	@PostMapping("/{id}/coupons")
	public ResponseEntity<List<Coupon>> applyCouponToShoppingCart(@PathVariable String id, @RequestBody Coupon coupon) {
		return new ResponseEntity<>(shoppingCartService.applyCoupon(id, coupon), HttpStatus.CREATED);
	}
	
	//User can remove an existing item from shopping cart
	@DeleteMapping("/{id}/items/{itemId}")
	public ResponseEntity<Void> deleteItemInShoppingCart(@PathVariable String id, @PathVariable String itemId) {
		shoppingCartService.deleteItem(id, itemId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	//Exception Handlers
	@ExceptionHandler({ShoppingCartNotFoundException.class})
	public ResponseEntity<String> handleShoppingCartNotFoundException(ShoppingCartNotFoundException e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({ShoppingCartAlreadyPresentException.class})
	public ResponseEntity<String> handleAlreadyPresentException(ShoppingCartAlreadyPresentException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler({ItemNotFoundException.class})
	public ResponseEntity<String> handleItemNotFoundException(ItemNotFoundException e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({ItemAlreadyPresentException.class})
	public ResponseEntity<String> handleItemAlreadyPresentException(ItemAlreadyPresentException e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler({CouponAlreadyPresentException.class})
	public ResponseEntity<String> handleCouponAlreadyPresentException(CouponAlreadyPresentException e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler({BadShoppingCartException.class})
	public ResponseEntity<String> handleBadShoppingCartException(BadShoppingCartException e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({BadItemException.class})
	public ResponseEntity<String> handleBadItemException(BadItemException e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({BadCouponException.class})
	public ResponseEntity<String> handleBadCouponException(BadCouponException e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
}