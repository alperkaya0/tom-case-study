package com.tom.shoppingcartapi.service;
import org.springframework.stereotype.Service;
import com.tom.shoppingcartapi.repository.ItemRepository;


@Service
public class ItemService {
	private final ItemRepository itemRepository;
	
	public ItemService (ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}
}