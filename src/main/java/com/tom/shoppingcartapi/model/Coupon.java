package com.tom.shoppingcartapi.model;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
	/* What does a coupon have?
	   - Id
	   - Rate(so that you can calculate discount)
	   - Lower and upper limits for further improvements on this API
	    */
	@Id
	private String id;
	private double rate;
	//These below are not going to be used for this case study, but it will serve a purpose later if someone wants to add a coupon that comes with conditions
	private double lowerLimit = 0;
	private double upperLimit = Double.MAX_VALUE;
}
