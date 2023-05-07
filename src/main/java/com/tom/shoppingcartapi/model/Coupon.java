package com.tom.shoppingcartapi.model;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
	/* What does a coupon have?
	   - Id
	   - Type
	   - Rate or Amount(so that you can calculate discount)
	   - Lower and upper limits for further improvements on this API
	    */
	@Id
	private String id;
	private String type;
	@JsonInclude(JsonInclude.Include.NON_NULL)//This means it will be ignored if client doesn't provide a value
	private double rate;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private double amount;
	//These below are not going to be used for this case study, but it will serve a purpose later if someone wants to add a coupon that comes with conditions
	@JsonIgnore //For now ignore them
	private double lowerLimit = 0;
	@JsonIgnore //For now ignore them
	private double upperLimit = Double.MAX_VALUE;
}
