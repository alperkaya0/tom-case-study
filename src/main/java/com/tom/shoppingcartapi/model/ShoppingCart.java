package com.tom.shoppingcartapi.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
	/* What does a shopping cart have?
	   - Id(definitely)
	   - CustomerId to link it to customers (1 to 1 relationship)
	   - Items list
	   - Total price
	   - Discounted price
	   - Coupons(so that we can calculate discounted price) 
	*/
	@Id
	private String id;
	private String customerId;
	private List<Item> items;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Coupon> coupons;
	@JsonInclude(JsonInclude.Include.NON_NULL) //with this, you don't have to give totalPrice at POST request, but it will show up at GET request
	private double totalPrice;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private double discountedPrice;
}
