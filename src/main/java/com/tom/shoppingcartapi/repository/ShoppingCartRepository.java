package com.tom.shoppingcartapi.repository;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.tom.shoppingcartapi.model.ShoppingCart;

public interface ShoppingCartRepository extends MongoRepository<ShoppingCart, String> {
	Optional<ShoppingCart> findByCustomerId(String customerId);
}