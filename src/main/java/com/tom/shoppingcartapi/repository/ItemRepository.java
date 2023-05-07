package com.tom.shoppingcartapi.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.tom.shoppingcartapi.model.Item;

public interface ItemRepository extends MongoRepository<Item, String> {
	
}