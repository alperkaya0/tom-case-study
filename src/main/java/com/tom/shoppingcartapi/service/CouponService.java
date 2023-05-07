package com.tom.shoppingcartapi.service;
import org.springframework.stereotype.Service;
import com.tom.shoppingcartapi.repository.CouponRepository;


@Service
public class CouponService {
	private final CouponRepository couponRepository;
	
	public CouponService (CouponRepository couponRepository) {
		this.couponRepository = couponRepository;
	}
}