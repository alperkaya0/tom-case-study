package com.tom.shoppingcartapi.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.tom.shoppingcartapi.model.ShoppingCart;

public interface ShoppingCartRepository extends MongoRepository<ShoppingCart, String> {
	
}