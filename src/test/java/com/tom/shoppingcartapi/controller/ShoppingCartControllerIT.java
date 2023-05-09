package com.tom.shoppingcartapi.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.shoppingcartapi.service.ShoppingCartService;

import com.tom.shoppingcartapi.model.*;
import java.util.List;

import java.util.ArrayList;

//Integration Tests

@WebMvcTest(ShoppingCartController.class)
public class ShoppingCartControllerIT {
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private ShoppingCartService service;

    @Test
    @DisplayName("IT - Should return OK and list of jsons of shopping carts")
    public void getAllShoppingCarts() throws Exception {
    	List<ShoppingCart> list = new ArrayList<>();
    	
    	ShoppingCart sc1 = new ShoppingCart();
    	ShoppingCart sc2 = new ShoppingCart();
    	
    	sc1.setId("1");
    	sc2.setId("2");
    	
    	list.add(sc1);
    	list.add(sc2);
    	
    	when(service.getShoppingCarts()).thenReturn(list);

    	mockMvc.perform(get("/v1/shopping-carts"))
    					.andExpect(status().isOk())
    					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    					.andExpect(jsonPath("$[0].id").value("1"));

    	mockMvc.perform(get("/v1/shopping-carts"))
    					.andExpect(status().isOk())
    					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    					.andExpect(jsonPath("$[1].id").value("2"));
    }
    
    @Test
    @DisplayName("IT - Should return OK and json of a shopping cart")
    public void getShoppingCartById() throws Exception {
    	ShoppingCart sc1 = new ShoppingCart();
    	
    	Item item1 = new Item();
    	item1.setId("1");
    	item1.setCategory("sports");
    	item1.setName("name");
    	item1.setPrice(10);
    	item1.setQuantity(1);
    	item1.setUrl("");
    	
    	Coupon coupon1 = new Coupon();
    	coupon1.setId("coupon1");
    	
    	sc1.setId("1");
    	sc1.setCoupons(List.of(coupon1));
    	sc1.setCustomerId("customer1");
    	sc1.setDiscountedPrice(100);
    	sc1.setTotalPrice(100);
    	sc1.setItems(List.of(item1));
    	
    	when(service.getShoppingCartById(sc1.getId())).thenReturn(sc1);

    	mockMvc.perform(get("/v1/shopping-carts/" + sc1.getId()))
    					.andExpect(status().isOk())
    					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    					.andExpect(jsonPath("$.id").value("1"))
    					.andExpect(jsonPath("$.customerId").value("customer1"))
    					.andExpect(jsonPath("$.discountedPrice").value(100))
    					.andExpect(jsonPath("$.totalPrice").value(100))
    					.andExpect(jsonPath("$.items[0].id").value("1"))
    					.andExpect(jsonPath("$.coupons[0].id").value("coupon1"));
		
    }
    
    @Test
    @DisplayName("IT - Should return OK and list of jsons of items")
    public void getAllItemsInShoppingCarts() throws Exception {
    	String id = "someId";
    	
    	ShoppingCart sc1 = new ShoppingCart();
    	sc1.setId(id);
    	
    	Item item1 = new Item();
    	Item item2 = new Item();
    	
    	item1.setId("1");
    	item2.setId("2");
    	
    	sc1.setItems(List.of(item1, item2));
    	
    	when(service.getItems(id)).thenReturn(sc1.getItems());

    	mockMvc.perform(get("/v1/shopping-carts/" + id + "/items"))
    					.andExpect(status().isOk())
    					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    					.andExpect(jsonPath("$[0].id").value("1"));

    	mockMvc.perform(get("/v1/shopping-carts/" + id + "/items"))
    					.andExpect(status().isOk())
    					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    					.andExpect(jsonPath("$[1].id").value("2"));
    }
    
    @Test
    @DisplayName("IT - Should create a new ShoppingCart")
    public void addNewShoppingCart() throws JsonProcessingException, Exception {
    	String id = "someId";
    	
    	ShoppingCart sc = new ShoppingCart();
    	sc.setCustomerId("1234");
    	
    	ShoppingCart res = new ShoppingCart();
    	res.setId(id);
    	res.setCustomerId("1234");
    	
    	when(service.createNewShoppingCart(sc)).thenReturn(res);
    	
    	mockMvc.perform(post("/v1/shopping-carts")
    					.contentType(MediaType.APPLICATION_JSON)
    					.content(objectMapper.writeValueAsString(sc))
    					.accept(MediaType.APPLICATION_JSON))
    				.andExpect(status().isCreated())
    				.andExpect(jsonPath("$.id").value(id))
    				.andExpect(jsonPath("$.customerId").value("1234"));
    	
    	verify(service).createNewShoppingCart(sc);
    }
    
    @Test
    @DisplayName("IT - Should create a new Item")
    public void addNewItemToShoppingCart() throws JsonProcessingException, Exception {
    	String id = "someId";
    	
    	Item item = new Item();
    	item.setId("1");
    	
    	ShoppingCart res = new ShoppingCart();
    	res.setId("aa1");
    	res.setItems(List.of(item));
    	
    	when(service.addNewItemToShoppingCart(id, item)).thenReturn(res.getItems());
    	
    	mockMvc.perform(post("/v1/shopping-carts/" + id + "/items")
    					.contentType(MediaType.APPLICATION_JSON)
    					.content(objectMapper.writeValueAsString(item))
    					.accept(MediaType.APPLICATION_JSON))
    				.andExpect(status().isCreated())
    				.andExpect(jsonPath("$[0].id").value("1"));
    	
    	verify(service).addNewItemToShoppingCart(id, item);
    }
    

    @Test
    @DisplayName("IT - Should create a new Coupon")
    public void applyCouponToShoppingCart() throws JsonProcessingException, Exception {
    	String id = "someId";
    	
    	Coupon coupon = new Coupon();
    	coupon.setId("coupon1");
    	
    	ShoppingCart res = new ShoppingCart();
    	res.setId("aa1");
    	res.setCoupons(List.of(coupon));
    	
    	Coupon resCoupon = new Coupon();
    	resCoupon.setAmount(50);
    	resCoupon.setRate(0);
    	resCoupon.setType("amount");
    	resCoupon.setId("coupon1");
    	
    	when(service.applyCoupon(id, coupon)).thenReturn(resCoupon);
    	
    	mockMvc.perform(post("/v1/shopping-carts/" + id + "/coupons")
    					.contentType(MediaType.APPLICATION_JSON)
    					.content(objectMapper.writeValueAsString(coupon))
    					.accept(MediaType.APPLICATION_JSON))
    				.andExpect(status().isCreated())
    				.andExpect(jsonPath("$.id").value("coupon1"))
    				.andExpect(jsonPath("$.type").value("amount"))
    				.andExpect(jsonPath("$.amount").value(50))
    				.andExpect(jsonPath("$.rate").value(0));
		
    	verify(service).applyCoupon(id, coupon);
    }
    
    @Test
    @DisplayName("IT - Should delete an item")
    public void deleteItemInShoppingCart() throws Exception {
    	String id = "someId";
    	String itemId = "someId";
    	
    	mockMvc.perform(delete("/v1/shopping-carts/" + id + "/items/" + itemId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
    }
}