package com.tom.shoppingcartapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tom.shoppingcartapi.service.ItemService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/items")
@AllArgsConstructor
public class ItemController {
	private final ItemService itemService;
	
}