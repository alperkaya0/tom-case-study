package com.tom.shoppingcartapi.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tom.shoppingcartapi.service.CouponService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/coupons")
@AllArgsConstructor
public class CouponController {
	private final CouponService couponService;
	
}