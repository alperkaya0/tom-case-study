package com.tom.shoppingcartapi.model;

import java.util.List;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
	/* What does a shopping cart have?
	   - Id(definitely)
	   - Items list
	   - Total price
	   - Discounted price
	   - Coupons(so that we can calculate discounted price) 
	   -*/
	@Id
	private String id;
	private String customerId;
	private List<Item> items;
	private List<Coupon> coupons;
	@JsonIgnore
	private double totalPrice;
	@JsonIgnore
	private double discountedPrice;
}
