package com.tom.shoppingcartapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.tom.shoppingcartapi.model.*;
import com.tom.shoppingcartapi.exception.BadCouponException;
import com.tom.shoppingcartapi.exception.BadItemException;
import com.tom.shoppingcartapi.exception.ShoppingCartNotFoundException;
import com.tom.shoppingcartapi.model.Item;
import com.tom.shoppingcartapi.repository.ShoppingCartRepository;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
	
	@Mock
	private ShoppingCartRepository shoppingCartRepository;
	
	@InjectMocks
	private ShoppingCartService shoppingCartService;
	
	@Captor
    private ArgumentCaptor<ShoppingCart> scCaptor;

	@Test
	@DisplayName("Positive test of getItems function of service")
	public void getItemsOfShoppingCartById() {
		String id = "someId";
		
		ShoppingCart sc = new ShoppingCart();
		
		Item item1 = new Item();
		item1.setId("1");
		item1.setCategory("Sports");
		item1.setName("Item name");
		item1.setPrice(100);
		item1.setQuantity(2);
		
		Item item2 = new Item();
		item2.setId("2");
		item2.setCategory("Technology");
		item2.setName("Item name");
		item2.setPrice(3400);
		item2.setQuantity(1);
		
		sc.setId(id);
		sc.setCustomerId("123812");
		sc.setItems(List.of(item1, item2));
		
		when(shoppingCartRepository.findById(id)).thenReturn(Optional.of(sc));
		
		shoppingCartService.getItems(id);
		
		//Verify that the findById method called with the given arguments, given number of times(1 here)
		verify(shoppingCartRepository).findById(id);
	}
	
	@Test
	@DisplayName("Negative test of getItems, throws ShoppingCartNotFoundException")
	public void getItemsOfShoppingCartByIdThrowsShoppingCartNotFoundException() {
		String id = "someId";
		
		when(shoppingCartRepository.findById(id)).thenReturn(Optional.empty());
		
		ShoppingCartNotFoundException e = assertThrows(ShoppingCartNotFoundException.class, () -> {
			shoppingCartService.getItems(id);
		});
		
		assertEquals("There is no ShoppingCart with that id.", e.getMessage());
	}
	@Test
	@DisplayName("Positive test of getShoppingCarts")
	public void getAllShoppingCarts() {
		ShoppingCart sc1 = new ShoppingCart();
		ShoppingCart sc2 = new ShoppingCart();
		
		when(shoppingCartRepository.findAll()).thenReturn(List.of(sc1, sc2));
		
		shoppingCartService.getShoppingCarts();
		
		verify(shoppingCartRepository).findAll();
	}
	
	@Test
	@DisplayName("Positive test of getShoppingCartById")
	public void getShoppingCartById() {
		String id = "someId";
		
		ShoppingCart sc = new ShoppingCart();
		sc.setId(id);
		sc.setCustomerId("23128");
		
		when(shoppingCartRepository.findById(id)).thenReturn(Optional.of(sc));
		
		shoppingCartService.getShoppingCartById(id);
		
		verify(shoppingCartRepository).findById(id);
	}
	
	@Test
	@DisplayName("Negative test of getShoppingCartById")
	public void getShoppingCartByIdThrowsException() {
		String id = "someId";
		
		when(shoppingCartRepository.findById(id)).thenReturn(Optional.empty());
		
		ShoppingCartNotFoundException e = assertThrows(ShoppingCartNotFoundException.class, () -> {
			shoppingCartService.getShoppingCartById(id);
		});
		
		assertEquals("There is no ShoppingCart with that id.", e.getMessage());
	}
	
	@Test
	@DisplayName("Positive test of validateItem")
	public void validateItem() {
		Item item = new Item();
		item.setId("1");
		item.setCategory("Sports");
		item.setName("El Yayı");
		item.setPrice(100.3);
		item.setQuantity(1);
		
		shoppingCartService.validateItem(item);
	}
	
	@Test
	@DisplayName("Negative test of validateItem, name is incorrect")
	public void validateItemNameThrowsException() {
		Item item = new Item();
		item.setId("1");
		item.setCategory("Sports");
		item.setPrice(100.3);
		item.setQuantity(1);
		
		BadItemException e = assertThrows(BadItemException.class, () -> {
			shoppingCartService.validateItem(item);
		});
		
		assertEquals("Item name cannot be empty.", e.getMessage());
	}

	@Test
	@DisplayName("Negative test of validateItem, price is zero")
	public void validateItemPriceThrowsException() {
		Item item = new Item();
		item.setId("1");
		item.setCategory("Sports");
		item.setName("El Yayı");
		item.setPrice(0);
		item.setQuantity(1);
		
		BadItemException e = assertThrows(BadItemException.class, () -> {
			shoppingCartService.validateItem(item);
		});
		
		assertEquals("Price must be a positive number.", e.getMessage());
	}
	
	@Test
	@DisplayName("Negative test of validateItem, price is negative")
	public void validateItemPriceThrowsException2() {
		Item item = new Item();
		item.setId("1");
		item.setCategory("Sports");
		item.setName("El Yayı");
		item.setPrice(-1);
		item.setQuantity(1);
		
		BadItemException e = assertThrows(BadItemException.class, () -> {
			shoppingCartService.validateItem(item);
		});
		
		assertEquals("Price must be a positive number.", e.getMessage());
	}
	

	@Test
	@DisplayName("Negative test of validateItem, quantity is zero")
	public void validateItemQuantityThrowsException() {
		Item item = new Item();
		item.setId("1");
		item.setCategory("Sports");
		item.setName("El Yayı");
		item.setPrice(100.3);
		item.setQuantity(0);
		
		BadItemException e = assertThrows(BadItemException.class, () -> {
			shoppingCartService.validateItem(item);
		});
		
		assertEquals("Quantity must be a positive number.", e.getMessage());
	}
	
	@Test
	@DisplayName("Negative test of validateItem, quantity is negative")
	public void validateItemQuantityThrowsException2() {
		Item item = new Item();
		item.setId("1");
		item.setCategory("Sports");
		item.setName("El Yayı");
		item.setPrice(100.3);
		item.setQuantity(-1);
		
		BadItemException e = assertThrows(BadItemException.class, () -> {
			shoppingCartService.validateItem(item);
		});
		
		assertEquals("Quantity must be a positive number.", e.getMessage());
	}
	
	@Test
	@DisplayName("Negative test of validateItem, category is incorrect")
	public void validateItemCategoryThrowsException() {
		Item item = new Item();
		item.setId("1");
		item.setName("El Yayı");
		item.setPrice(100.3);
		item.setQuantity(1);
		
		BadItemException e = assertThrows(BadItemException.class, () -> {
			shoppingCartService.validateItem(item);
		});
		
		assertEquals("Category name cannot be empty.", e.getMessage());
	}
	
	@Test
	@DisplayName("Test of changeCouponToHardCoded that it doesn't change what it shouldn't change")
	public void changeCouponToHardCoded() {
		Coupon c = new Coupon();
		c.setAmount(100);
		c.setId("someId");
		c.setRate(0.2);
		c.setType("someType");
		
		shoppingCartService.changeCouponToHardCoded(c);
		
		assertEquals(100, c.getAmount());
		assertEquals("someId", c.getId());
		assertEquals(0.2, c.getRate());
		assertEquals("someType", c.getType());
	}
	
	@Test
	@DisplayName("Test of changeCouponToHardCoded with id=coupon1")
	public void changeCouponToHardCodedCoupon1() {
		Coupon c = new Coupon();
		c.setAmount(100);
		c.setId("coupon1");
		c.setRate(0.2);
		c.setType("someType");
		
		shoppingCartService.changeCouponToHardCoded(c);
		
		assertEquals(50, c.getAmount());
		assertEquals("coupon1", c.getId());
		assertEquals(0, c.getRate());
		assertEquals("amount", c.getType());
	}
	
	@Test
	@DisplayName("Test of changeCouponToHardCoded with id=coupon2")
	public void changeCouponToHardCodedCoupon2() {
		Coupon c = new Coupon();
		c.setAmount(100);
		c.setId("coupon2");
		c.setRate(0.2);
		c.setType("someType");
		
		shoppingCartService.changeCouponToHardCoded(c);
		
		assertEquals(0, c.getAmount());
		assertEquals("coupon2", c.getId());
		assertEquals(0.10, c.getRate());
		assertEquals("rate", c.getType());
	}
	
	@Test
	@DisplayName("Test of changeCouponToHardCoded with id=coupon3")
	public void changeCouponToHardCodedCoupon3() {
		Coupon c = new Coupon();
		c.setAmount(100);
		c.setId("coupon3");
		c.setRate(0.2);
		c.setType("someType");
		
		shoppingCartService.changeCouponToHardCoded(c);
		
		assertEquals(0, c.getAmount());
		assertEquals("coupon3", c.getId());
		assertEquals(0.05, c.getRate());
		assertEquals("rate", c.getType());
	}
	
	@Test
	@DisplayName("Positive test of validateCoupon with amount type of coupon")
	public void validateCoupon() {
		Coupon c = new Coupon();
		c.setAmount(100);
		c.setId("1");
		c.setRate(0);
		c.setType("amount");
		
		shoppingCartService.validateCoupon(c);
	}

	@Test
	@DisplayName("Positive test of validateCoupon with rate type of coupon")
	public void validateCoupon2() {
		Coupon c = new Coupon();
		c.setAmount(0);
		c.setId("2");
		c.setRate(0.2);
		c.setType("rate");
		
		shoppingCartService.validateCoupon(c);
	}
	
	@Test
	@DisplayName("Positive test of validateCoupon with 'Rate' and 'Amount'[notice that they are uppercase]")
	public void validateCoupon3() {
		Coupon c = new Coupon();
		c.setAmount(0);
		c.setId("2");
		c.setRate(0.2);
		c.setType("Rate");
		
		shoppingCartService.validateCoupon(c);
		
		c.setAmount(100);
		c.setId("2");
		c.setRate(0);
		c.setType("Amount");
		
		shoppingCartService.validateCoupon(c);
	}
	
	@Test
	@DisplayName("Negative test of validateCoupon, type isn't given")
	public void validateCouponTypeThrowsException() {
		Coupon c = new Coupon();
		c.setAmount(0);
		c.setId("2");
		c.setRate(0.2);
		
		BadCouponException e = assertThrows(BadCouponException.class, () -> {
			shoppingCartService.validateCoupon(c);
		});
		
		assertEquals("Type of a coupon cannot be neither empty nor null.", e.getMessage());
	}
	
	@Test
	@DisplayName("Negative test of validateCoupon, type is given but it is incorrect")
	public void validateCouponTypeThrowsException2() {
		Coupon c = new Coupon();
		c.setAmount(0);
		c.setId("2");
		c.setRate(0.2);
		c.setType("asaKksakszxmwıeaüğğqwiçzşxLASPOASKLŞa1231421_-*/+-!'^+%&/()=?");
		
		BadCouponException e = assertThrows(BadCouponException.class, () -> {
			shoppingCartService.validateCoupon(c);
		});
		
		assertEquals("Type of a coupon must be either 'rate' or 'amount'.", e.getMessage());
	}
	
	//There are 2 types of coupons: rate and amount
	//There are 3 or 2 unwanted possibilities for each coupon type:
	//rate = 0, rate < 0, rate > 1; amount = 0, amount < 0
	//and both zero(rate = 0, amount = 0)
	@Test
	@DisplayName("Negative test of validateCoupon, type is 'amount' but rate is different than 0")
	public void validateCouponTypeIsAmountButRateIsNotZeroThrowsException() {
		Coupon c = new Coupon();
		c.setAmount(100);
		c.setId("2");
		c.setRate(0.2);
		c.setType("amount");
		
		BadCouponException e = assertThrows(BadCouponException.class, () -> {
			shoppingCartService.validateCoupon(c);
		});
		
		assertEquals("Rate of, a coupon of type amount must be zero.", e.getMessage());
	}
	
	@Test
	@DisplayName("Negative test of validateCoupon, type is 'rate' but amount is different than 0")
	public void validateCouponTypeIsRateButAmountIsNotZeroThrowsException() {
		Coupon c = new Coupon();
		c.setAmount(100);
		c.setId("2");
		c.setRate(0.2);
		c.setType("rate");
		
		BadCouponException e = assertThrows(BadCouponException.class, () -> {
			shoppingCartService.validateCoupon(c);
		});
		
		assertEquals("Amount of, a coupon of type rate must be zero.", e.getMessage());
	}
	
	@Test
	@DisplayName("Negative test of validateCoupon, both rate and amount are 0")
	public void validateCouponBothRateAndAmountAreZeroThrowsException() {
		Coupon c = new Coupon();
		c.setAmount(0);
		c.setId("2");
		c.setRate(0);
		c.setType("rate");
		
		BadCouponException e = assertThrows(BadCouponException.class, () -> {
			shoppingCartService.validateCoupon(c);
		});
		
		assertEquals("Rate of a coupon of type rate, must be a positive value between 0 and 1.", e.getMessage());
	}
	
	@Test
	@DisplayName("Negative test of validateCoupon, type is rate but rate is negative")
	public void validateCouponTypeIsRateButRateIsNegativeThrowsException() {
		Coupon c = new Coupon();
		c.setAmount(0);
		c.setId("2");
		c.setRate(-1);
		c.setType("rate");
		
		BadCouponException e = assertThrows(BadCouponException.class, () -> {
			shoppingCartService.validateCoupon(c);
		});
		
		assertEquals("Rate of a coupon of type rate, must be a positive value between 0 and 1.", e.getMessage());
	}
	
	@Test
	@DisplayName("Negative test of validateCoupon, type is rate but rate is bigger than one")
	public void validateCouponTypeIsRateButRateIsBiggerThanOneThrowsException() {
		Coupon c = new Coupon();
		c.setAmount(0);
		c.setId("2");
		c.setRate(10);
		c.setType("rate");
		
		BadCouponException e = assertThrows(BadCouponException.class, () -> {
			shoppingCartService.validateCoupon(c);
		});
		
		assertEquals("Rate of a coupon of type rate, must be a positive value between 0 and 1.", e.getMessage());
	}
	
	@Test
	@DisplayName("Negative test of validateCoupon, type is amount but amount is negative")
	public void validateCouponTypeIsAmountButAmountIsNegativeThrowsException() {
		Coupon c = new Coupon();
		c.setAmount(-100);
		c.setId("2");
		c.setRate(0);
		c.setType("amount");
		
		BadCouponException e = assertThrows(BadCouponException.class, () -> {
			shoppingCartService.validateCoupon(c);
		});
		
		assertEquals("Amount of a coupon of type amount, must be a positive value.", e.getMessage());
	}
	
	@Test
	@DisplayName("Negative test of validateCoupon, type is amount but amount is negative")
	public void validateCouponTypeIsAmountButAmountIsZeroThrowsException() {
		Coupon c = new Coupon();
		c.setAmount(0);
		c.setId("2");
		c.setRate(0);
		c.setType("amount");
		
		BadCouponException e = assertThrows(BadCouponException.class, () -> {
			shoppingCartService.validateCoupon(c);
		});
		
		assertEquals("Amount of a coupon of type amount, must be a positive value.", e.getMessage());
	}
	
	@Test
	@DisplayName("")
}
