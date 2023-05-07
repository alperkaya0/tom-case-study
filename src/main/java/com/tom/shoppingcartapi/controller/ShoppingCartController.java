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
import com.tom.shoppingcartapi.exception.ShoppingCartAlreadyPresentException;
import com.tom.shoppingcartapi.exception.ShoppingCartNotFoundException;
import com.tom.shoppingcartapi.model.Item;
import com.tom.shoppingcartapi.model.ShoppingCart;
import com.tom.shoppingcartapi.service.ShoppingCartService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/shopping-carts")
@AllArgsConstructor
public class ShoppingCartController {
	private final ShoppingCartService shoppingCartService;
	
	//End-points
	@GetMapping
	public ResponseEntity<List<ShoppingCart>> getShoppingCarts() {
		return new ResponseEntity<>(shoppingCartService.getShoppingCarts(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}/items")
	public ResponseEntity<List<Item>> getAllItemsInShoppingCart(@PathVariable String id) {
		return new ResponseEntity<List<Item>>(shoppingCartService.getItems(id), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<ShoppingCart> addNewShoppingCart(@RequestBody ShoppingCart shoppingCart) {
		return new ResponseEntity<>(shoppingCartService.createNewShoppingCart(shoppingCart), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}/items/{itemId}")
	public ResponseEntity<Void> deleteItemInShoppingCart(@PathVariable String id, @PathVariable String itemId) {
		shoppingCartService.deleteItem(id, itemId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	//Exception Handlers
	@ExceptionHandler({ShoppingCartAlreadyPresentException.class})
	public ResponseEntity<String> handleShoppingCartAlreadyPresentException(ShoppingCartAlreadyPresentException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler({ShoppingCartNotFoundException.class})
	public ResponseEntity<String> handleShoppingCartNotFoundException(ShoppingCartNotFoundException e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
}