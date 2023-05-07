package com.tom.shoppingcartapi.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.tom.shoppingcartapi.model.Coupon;

public interface CouponRepository extends MongoRepository<Coupon, String>{
	
}