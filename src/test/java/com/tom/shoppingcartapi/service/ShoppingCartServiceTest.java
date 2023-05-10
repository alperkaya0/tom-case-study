package com.tom.shoppingcartapi.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import com.tom.shoppingcartapi.exception.BadShoppingCartException;
import com.tom.shoppingcartapi.exception.CouponAlreadyPresentException;
import com.tom.shoppingcartapi.exception.ItemAlreadyPresentException;
import com.tom.shoppingcartapi.exception.ItemNotFoundException;
import com.tom.shoppingcartapi.exception.ShoppingCartAlreadyPresentException;
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
		item.setUrl("");
		
		shoppingCartService.validateItem(item);
	}
	
	@Test
	@DisplayName("Negative test of validateItem, url is null")
	public void validateItemUrlThrowsException() {
		Item item = new Item();
		item.setId("1");
		item.setCategory("Sports");
		item.setName("El Yayı");
		item.setPrice(100.3);
		item.setQuantity(1);
		//u.r.l. is not assigned
		
		BadItemException e = assertThrows(BadItemException.class, () -> {
			shoppingCartService.validateItem(item);
		});
		
		assertEquals("Item url cannot be empty.", e.getMessage());
	}
	
	@Test
	@DisplayName("Negative test of validateItem, name is incorrect")
	public void validateItemNameThrowsException() {
		Item item = new Item();
		item.setId("1");
		item.setCategory("Sports");
		item.setPrice(100.3);
		item.setQuantity(1);
		item.setUrl("");
		
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
		item.setUrl("");
		
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
		item.setUrl("example url");
		
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
		item.setUrl("");
		
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
		item.setUrl("");
		
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
		item.setUrl("example url");
		
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
	@DisplayName("Positive test of validateWholeShoppingCart")
	public void validateWholeShoppingCart() {
		ShoppingCart sc = new ShoppingCart();
		
		Item item1 = new Item();
		item1.setCategory("Sports");
		item1.setId("1");
		item1.setName("El Yayı - 200 Lbs");
		item1.setPrice(190);
		item1.setQuantity(2);
		item1.setUrl("");
		
		Item item2 = new Item();
		item2.setCategory("Technology");
		item2.setId("2");
		item2.setName("Kindle PaperWhite 4");
		item2.setPrice(3400);
		item2.setQuantity(1);
		item2.setUrl("");
		
		Coupon coupon1 = new Coupon();
		coupon1.setAmount(100);
		coupon1.setId("1");
		coupon1.setRate(0);
		coupon1.setType("amount");

		Coupon coupon2 = new Coupon();
		coupon2.setAmount(0);
		coupon2.setId("coupon1");
		coupon2.setRate(0.2);
		coupon2.setType("rate");
		
		sc.setCoupons(List.of(coupon1, coupon2));
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		sc.setItems(List.of(item1, item2));
		
		shoppingCartService.validateWholeShoppingCart(sc);
		
		assertEquals(50, sc.getCoupons().get(sc.getCoupons().indexOf(coupon2)).getAmount());
		assertEquals(0, sc.getCoupons().get(sc.getCoupons().indexOf(coupon2)).getRate());
		assertEquals("amount", sc.getCoupons().get(sc.getCoupons().indexOf(coupon2)).getType());
	}
	
	@Test
	@DisplayName("Negative test of validateWholeShoppingCart, total price is negative")
	public void validateWholeShoppingCartTotalPriceThrowsException() {
		ShoppingCart sc = new ShoppingCart();
		sc.setTotalPrice(-1);
		sc.setDiscountedPrice(100);
		
		BadShoppingCartException e = assertThrows(BadShoppingCartException.class, () -> {
			shoppingCartService.validateWholeShoppingCart(sc);
		});
		
		assertEquals("Price must be a positive number. Incorrect shopping cart.", e.getMessage());
	}
	
	@Test
	@DisplayName("Negative test of validateWholeShoppingCart, discounted price is negative")
	public void validateWholeShoppingCartDiscountedPriceThrowsException() {
		ShoppingCart sc = new ShoppingCart();
		sc.setTotalPrice(100);
		sc.setDiscountedPrice(-1);
		
		BadShoppingCartException e = assertThrows(BadShoppingCartException.class, () -> {
			shoppingCartService.validateWholeShoppingCart(sc);
		});
		
		assertEquals("Price must be a positive number. Incorrect shopping cart.", e.getMessage());
	}
	
	//Following 2 tests are about item or coupon id re-occurring in a list

	@Test
	@DisplayName("Negative test of validateWholeShoppingCart, id of items are re-occuring")
	public void validateWholeShoppingCartItemListHasRepetitionThrowsException() {
		ShoppingCart sc = new ShoppingCart();
		
		Item item1 = new Item();
		item1.setCategory("Sports");
		item1.setId("1");
		item1.setName("El Yayı - 200 Lbs");
		item1.setPrice(190);
		item1.setQuantity(2);
		item1.setUrl("example url");
		
		Item item2 = new Item();
		item2.setCategory("Technology");
		item2.setId("1");
		item2.setName("Kindle PaperWhite 4");
		item2.setPrice(3400);
		item2.setQuantity(1);
		item2.setUrl("example url");
		
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		sc.setItems(List.of(item1, item2));
		
		ItemAlreadyPresentException e = assertThrows(ItemAlreadyPresentException.class, () -> {
			shoppingCartService.validateWholeShoppingCart(sc);
		});
		
		assertEquals("There are multiple items with the same id. Incorrect shopping cart.", e.getMessage());
	}
	
	@Test
	@DisplayName("Negative test of validateWholeShoppingCart, id of coupons are re-occuring")
	public void validateWholeShoppingCartCouponListHasRepetitionThrowsException() {
		ShoppingCart sc = new ShoppingCart();

		Coupon coupon1 = new Coupon();
		coupon1.setAmount(100);
		coupon1.setId("1");
		coupon1.setRate(0);
		coupon1.setType("amount");

		Coupon coupon2 = new Coupon();
		coupon2.setAmount(0);
		coupon2.setId("1");
		coupon2.setRate(0.2);
		coupon2.setType("rate");
		
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		sc.setCoupons(List.of(coupon1, coupon2));
		
		CouponAlreadyPresentException e = assertThrows(CouponAlreadyPresentException.class, () -> {
			shoppingCartService.validateWholeShoppingCart(sc);
		});
		
		assertEquals("There are multiple coupons with the same id. Incorrect shopping cart.", e.getMessage());
	}
	
	@Test
	@DisplayName("Negative test of validateWholeShoppingCart, id of items are re-occuring")
	public void validateWholeShoppingCartCouponListIsEmpty() {
		ShoppingCart sc = new ShoppingCart();
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		
		shoppingCartService.validateWholeShoppingCart(sc);
	}

	@Test
	@DisplayName("Positive test of calculatePrices")
	public void calculatePrices() {
		ShoppingCart sc = new ShoppingCart();
		
		Item item1 = new Item();
		item1.setCategory("Sports");
		item1.setId("1");
		item1.setName("El Yayı - 200 Lbs");
		item1.setPrice(190);
		item1.setQuantity(2);
		
		Item item2 = new Item();
		item2.setCategory("Technology");
		item2.setId("2");
		item2.setName("Kindle PaperWhite 4");
		item2.setPrice(3400);
		item2.setQuantity(1);
		
		Coupon coupon1 = new Coupon();
		coupon1.setAmount(100);
		coupon1.setId("1");
		coupon1.setRate(0);
		coupon1.setType("amount");

		Coupon coupon2 = new Coupon();
		coupon2.setAmount(0);
		coupon2.setId("2");
		coupon2.setRate(0.2);
		coupon2.setType("rate");
		
		sc.setCoupons(List.of(coupon1, coupon2));
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		sc.setItems(List.of(item1, item2));
		
		sc = shoppingCartService.calculatePrices(sc);
		
		//1E-10 is recommended value for double comparison
		assertThat(Math.abs(3780 - sc.getTotalPrice()) < 1E-10).isTrue();
		assertThat(Math.abs(2944 - sc.getDiscountedPrice()) < 1E-10).isTrue();
	}

	@Test
	@DisplayName("Test of calculatePrices, total amount is smaller than total coupon 'amount'(not rate but amount type of coupon)")
	public void calculatePricesButTotalAmountOfCouponsBiggerThanCartSum() {
		ShoppingCart sc = new ShoppingCart();
		
		Item item1 = new Item();
		item1.setCategory("Sports");
		item1.setId("1");
		item1.setName("El Yayı - 200 Lbs");
		item1.setPrice(100);
		item1.setQuantity(2);
		
		Item item2 = new Item();
		item2.setCategory("Technology");
		item2.setId("2");
		item2.setName("Kindle PaperWhite 4");
		item2.setPrice(100);
		item2.setQuantity(1);
		
		Coupon coupon1 = new Coupon();
		coupon1.setAmount(500);
		coupon1.setId("1");
		coupon1.setRate(0);
		coupon1.setType("amount");

		Coupon coupon2 = new Coupon();
		coupon2.setAmount(200);
		coupon2.setId("2");
		coupon2.setRate(0);
		coupon2.setType("amount");
		
		sc.setCoupons(List.of(coupon1, coupon2));
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		sc.setItems(List.of(item1, item2));
		
		sc = shoppingCartService.calculatePrices(sc);
		
		//1E-10 is recommended value for double comparison
		assertThat(Math.abs(300 - sc.getTotalPrice()) < 1E-10).isTrue();
		assertThat(Math.abs(0 - sc.getDiscountedPrice()) < 1E-10).isTrue();
	}

	@Test
	@DisplayName("Test of calculatePrices, but coupon type is incorrect")
	public void calculatePricesButCouponTypeIsWrongThrowsException() {
		ShoppingCart sc = new ShoppingCart();
		
		Item item1 = new Item();
		item1.setCategory("Sports");
		item1.setId("1");
		item1.setName("El Yayı - 200 Lbs");
		item1.setPrice(100);
		item1.setQuantity(2);
		
		Coupon coupon1 = new Coupon();
		coupon1.setAmount(500);
		coupon1.setId("1");
		coupon1.setRate(0);
		coupon1.setType("asdoaısjdıoa");
		
		sc.setCoupons(List.of(coupon1));
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		sc.setItems(List.of(item1));
		
		BadCouponException e = assertThrows(BadCouponException.class, () -> {
			shoppingCartService.calculatePrices(sc);
		});
		
		assertEquals("Type of a coupon must be either 'rate' or 'amount'.", e.getMessage());
	}

	@Test
	@DisplayName("Test of calculatePrices, but coupon list is null")
	public void calculatePricesButCouponListIsNull() {
		ShoppingCart sc = new ShoppingCart();
		
		Item item1 = new Item();
		item1.setCategory("Sports");
		item1.setId("1");
		item1.setName("El Yayı - 200 Lbs");
		item1.setPrice(100);
		item1.setQuantity(2);
		
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		sc.setItems(List.of(item1));
		
		shoppingCartService.calculatePrices(sc);
	}

	@Test
	@DisplayName("Test of calculatePrices, but coupon list is null")
	public void calculatePricesButSumIsEqualsToUpperLimit() {
		ShoppingCart sc = new ShoppingCart();

		Item item1 = new Item();
		item1.setCategory("Sports");
		item1.setId("1");
		item1.setName("El Yayı - 200 Lbs");
		item1.setPrice(Double.MAX_VALUE);
		item1.setQuantity(1);
		
		Coupon coupon1 = new Coupon();
		coupon1.setAmount(1);
		coupon1.setId("1");
		coupon1.setRate(0);
		coupon1.setType("amount");
		
		sc.setCoupons(List.of(coupon1));
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		sc.setItems(List.of(item1));
		
		
		shoppingCartService.calculatePrices(sc);
	}

	@Test
	@DisplayName("Positive test for createNewShoppingCart")
	public void createNewShoppingCart() {
		ShoppingCart sc = new ShoppingCart();
		
		Item item1 = new Item();
		item1.setCategory("Sports");
		item1.setId("1");
		item1.setName("El Yayı - 200 Lbs");
		item1.setPrice(190);
		item1.setQuantity(2);
		item1.setUrl("");
		
		Item item2 = new Item();
		item2.setCategory("Technology");
		item2.setId("2");
		item2.setName("Kindle PaperWhite 4");
		item2.setPrice(3400);
		item2.setQuantity(1);
		item2.setUrl("");
		
		Coupon coupon1 = new Coupon();
		coupon1.setAmount(100);
		coupon1.setId("1");
		coupon1.setRate(0);
		coupon1.setType("amount");

		Coupon coupon2 = new Coupon();
		coupon2.setAmount(0);
		coupon2.setId("2");
		coupon2.setRate(0.2);
		coupon2.setType("rate");
		
		sc.setCoupons(List.of(coupon1, coupon2));
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		sc.setItems(List.of(item1, item2));
		
		when(shoppingCartRepository.findByCustomerId(sc.getCustomerId())).thenReturn(Optional.empty());
		when(shoppingCartRepository.save(sc)).thenReturn(sc);
		
		ShoppingCart ans = shoppingCartService.createNewShoppingCart(sc);
		
		verify(shoppingCartRepository).findByCustomerId(sc.getCustomerId());
		verify(shoppingCartRepository).save(sc);
		
		assertEquals(ans.getCoupons().get(0), sc.getCoupons().get(0));
		assertEquals(ans.getCoupons().get(1), sc.getCoupons().get(1));
		assertEquals(ans.getItems().get(0), sc.getItems().get(0));
		assertEquals(3780.0, ans.getTotalPrice());
		assertEquals(2944, ans.getDiscountedPrice());
	}
	
	@Test
	@DisplayName("Negative test of createNewShoppingCart, shopping cart already exists")
	public void createNewShoppingCartButShoppingCartAlreadyExistsThrowException() {
		ShoppingCart sc = new ShoppingCart();
		
		Item item1 = new Item();
		item1.setCategory("Sports");
		item1.setId("1");
		item1.setName("El Yayı - 200 Lbs");
		item1.setPrice(190);
		item1.setQuantity(2);
		
		Item item2 = new Item();
		item2.setCategory("Technology");
		item2.setId("2");
		item2.setName("Kindle PaperWhite 4");
		item2.setPrice(3400);
		item2.setQuantity(1);
		
		Coupon coupon1 = new Coupon();
		coupon1.setAmount(100);
		coupon1.setId("1");
		coupon1.setRate(0);
		coupon1.setType("amount");

		Coupon coupon2 = new Coupon();
		coupon2.setAmount(0);
		coupon2.setId("2");
		coupon2.setRate(0.2);
		coupon2.setType("rate");
		
		sc.setCoupons(List.of(coupon1, coupon2));
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		sc.setItems(List.of(item1, item2));
		
		when(shoppingCartRepository.findByCustomerId(sc.getCustomerId())).thenReturn(Optional.of(sc));
		
		ShoppingCartAlreadyPresentException e = assertThrows(ShoppingCartAlreadyPresentException.class, () -> {
			shoppingCartService.createNewShoppingCart(sc);
		});
		
		assertEquals("Customer already has a shopping cart. You may want to update it or delete then recreate it.", e.getMessage());
	}


	@Test
	@DisplayName("Negative test of createNewShoppingCart, shopping cart already exists")
	public void createNewShoppingCartButShoppingCartAlreadyExists2ThrowException() {
		ShoppingCart sc = new ShoppingCart();
		
		Item item1 = new Item();
		item1.setCategory("Sports");
		item1.setId("1");
		item1.setName("El Yayı - 200 Lbs");
		item1.setPrice(190);
		item1.setQuantity(2);
		
		Item item2 = new Item();
		item2.setCategory("Technology");
		item2.setId("2");
		item2.setName("Kindle PaperWhite 4");
		item2.setPrice(3400);
		item2.setQuantity(1);
		
		Coupon coupon1 = new Coupon();
		coupon1.setAmount(100);
		coupon1.setId("1");
		coupon1.setRate(0);
		coupon1.setType("amount");

		Coupon coupon2 = new Coupon();
		coupon2.setAmount(0);
		coupon2.setId("2");
		coupon2.setRate(0.2);
		coupon2.setType("rate");
		
		sc.setCoupons(List.of(coupon1, coupon2));
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		sc.setItems(List.of(item1, item2));
		
		when(shoppingCartRepository.findById(sc.getId())).thenReturn(Optional.of(sc));
		
		ShoppingCartAlreadyPresentException e = assertThrows(ShoppingCartAlreadyPresentException.class, () -> {
			shoppingCartService.createNewShoppingCart(sc);
		});
		
		assertEquals("ShoppingCart with that id already present. You may want to update it or delete then recreate it.", e.getMessage());
	}
	
	@Test
	@DisplayName("Negative test for createNewShoppingCart, coupon list is null")
	public void createNewShoppingCartButCouponListIsNull() {
		ShoppingCart sc = new ShoppingCart();
		
		Item item1 = new Item();
		item1.setCategory("Sports");
		item1.setId("1");
		item1.setName("El Yayı - 200 Lbs");
		item1.setPrice(190);
		item1.setQuantity(2);
		item1.setUrl("");
		
		Item item2 = new Item();
		item2.setCategory("Technology");
		item2.setId("2");
		item2.setName("Kindle PaperWhite 4");
		item2.setPrice(3400);
		item2.setQuantity(1);
		item2.setUrl("");
		
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		sc.setItems(List.of(item1, item2));
		
		shoppingCartService.createNewShoppingCart(sc);
	}
	
	@Test
	@DisplayName("Negative test for createNewShoppingCart, item list is null")
	public void createNewShoppingCartButItemListIsNull() {
		ShoppingCart sc = new ShoppingCart();
		
		Coupon coupon1 = new Coupon();
		coupon1.setAmount(100);
		coupon1.setId("1");
		coupon1.setRate(0);
		coupon1.setType("amount");

		Coupon coupon2 = new Coupon();
		coupon2.setAmount(0);
		coupon2.setId("2");
		coupon2.setRate(0.2);
		coupon2.setType("rate");
		
		sc.setCoupons(List.of(coupon1, coupon2));
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		
		shoppingCartService.createNewShoppingCart(sc);
	}
	
	@Test
	@DisplayName("Positive test for deleteItem")
	public void deleteItem() {
		ShoppingCart sc = new ShoppingCart();
		
		Item item1 = new Item();
		item1.setCategory("Sports");
		item1.setId("1");
		item1.setName("El Yayı - 200 Lbs");
		item1.setPrice(190);
		item1.setQuantity(2);
		
		Item item2 = new Item();
		item2.setCategory("Technology");
		item2.setId("2");
		item2.setName("Kindle PaperWhite 4");
		item2.setPrice(3400);
		item2.setQuantity(1);
		
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		List<Item> i = new java.util.ArrayList<>();
		i.add(item1);
		i.add(item2);
		sc.setItems(i);
		
		when(shoppingCartRepository.findById(sc.getId())).thenReturn(Optional.of(sc));
		
		shoppingCartService.deleteItem(sc.getId(), item1.getId());
		
		assertEquals(1, sc.getItems().size());
		assertEquals(item2.getId(), sc.getItems().get(0).getId());
	}
	
	@Test
	@DisplayName("Negative test of deleteItem, shopping cart doesn't have an item with the given id")
	public void deleteItemButItemNotFoundThrowsException() {
		ShoppingCart sc = new ShoppingCart();
		
		Item item1 = new Item();
		item1.setCategory("Sports");
		item1.setId("1");
		item1.setName("El Yayı - 200 Lbs");
		item1.setPrice(190);
		item1.setQuantity(2);
		
		Item item2 = new Item();
		item2.setCategory("Technology");
		item2.setId("2");
		item2.setName("Kindle PaperWhite 4");
		item2.setPrice(3400);
		item2.setQuantity(1);
		
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		List<Item> i = new java.util.ArrayList<>();
		i.add(item1);
		i.add(item2);
		sc.setItems(i);
		
		when(shoppingCartRepository.findById(sc.getId())).thenReturn(Optional.of(sc));
		
		ItemNotFoundException e = assertThrows(ItemNotFoundException.class, () -> {
			shoppingCartService.deleteItem(sc.getId(), "inexistent id");
		});
		
		assertEquals("There is no Item with that id.", e.getMessage());
	}
	
	@Test
	@DisplayName("Positive test for applyCoupon")
	public void applyCoupon() {
		ShoppingCart sc = new ShoppingCart();
		
		Item item1 = new Item();
		item1.setCategory("Sports");
		item1.setId("1");
		item1.setName("El Yayı - 200 Lbs");
		item1.setPrice(190);
		item1.setQuantity(2);
		item1.setUrl("");
		
		Item item2 = new Item();
		item2.setCategory("Technology");
		item2.setId("2");
		item2.setName("Kindle PaperWhite 4");
		item2.setPrice(3400);
		item2.setQuantity(1);
		item2.setUrl("");
		
		Coupon coupon1 = new Coupon();
		coupon1.setAmount(100);
		coupon1.setId("1");
		coupon1.setRate(0);
		coupon1.setType("amount");
		
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		sc.setItems(List.of(item1, item2));
		
		when(shoppingCartRepository.findById(sc.getId())).thenReturn(Optional.of(sc));
		when(shoppingCartRepository.save(sc)).thenReturn(sc);
		
		if (sc.getCoupons() == null) sc.setCoupons(new java.util.ArrayList<>());
		sc.getCoupons().add(shoppingCartService.applyCoupon(sc.getId(), coupon1));
		
		verify(shoppingCartRepository).findById(sc.getId());
		verify(shoppingCartRepository).save(sc);
		
		assertThat(sc.getCoupons().contains(coupon1));
		assertThat(sc.getCoupons().size() == 1);
		assertEquals(3680, sc.getDiscountedPrice());
		assertEquals(3780, sc.getTotalPrice());
	}
	
	@Test
	@DisplayName("Test for applyCoupon, coupon list is null")
	public void applyCouponButCouponListIsNull() {
		ShoppingCart sc = new ShoppingCart();
		
		Item item1 = new Item();
		item1.setCategory("Sports");
		item1.setId("1");
		item1.setName("El Yayı - 200 Lbs");
		item1.setPrice(190);
		item1.setQuantity(2);
		item1.setUrl("");
		
		Item item2 = new Item();
		item2.setCategory("Technology");
		item2.setId("2");
		item2.setName("Kindle PaperWhite 4");
		item2.setPrice(3400);
		item2.setQuantity(1);
		item2.setUrl("");
		
		Coupon coupon1 = new Coupon();
		coupon1.setAmount(100);
		coupon1.setId("1");
		coupon1.setRate(0);
		coupon1.setType("amount");
		
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		sc.setItems(List.of(item1, item2));
		
		when(shoppingCartRepository.findById(sc.getId())).thenReturn(Optional.of(sc));
		when(shoppingCartRepository.save(sc)).thenReturn(sc);
		
		shoppingCartService.applyCoupon(sc.getId(), coupon1);
		
		verify(shoppingCartRepository).findById(sc.getId());
		verify(shoppingCartRepository).save(sc);
		
		assertThat(sc.getCoupons().contains(coupon1));
		assertThat(sc.getCoupons().size() == 1);
		assertEquals(3680, sc.getDiscountedPrice());
		assertEquals(3780, sc.getTotalPrice());
	}
	
	@Test
	@DisplayName("Test for applyCoupon, but coupon list is not null")
	public void applyCouponButCouponListIsNotNull() {
		ShoppingCart sc = new ShoppingCart();
		
		Item item1 = new Item();
		item1.setCategory("Sports");
		item1.setId("1");
		item1.setName("El Yayı - 200 Lbs");
		item1.setPrice(190);
		item1.setQuantity(2);
		item1.setUrl("");
		
		Item item2 = new Item();
		item2.setCategory("Technology");
		item2.setId("2");
		item2.setName("Kindle PaperWhite 4");
		item2.setPrice(3400);
		item2.setQuantity(1);
		item2.setUrl("");
		
		Coupon coupon1 = new Coupon();
		coupon1.setAmount(100);
		coupon1.setId("1");
		coupon1.setRate(0);
		coupon1.setType("amount");
		
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		sc.setItems(List.of(item1, item2));
		List<Coupon> c = new java.util.ArrayList<>();
		sc.setCoupons(c);
	
		when(shoppingCartRepository.findById(sc.getId())).thenReturn(Optional.of(sc));
		when(shoppingCartRepository.save(sc)).thenReturn(sc);
		
		sc.getCoupons().add(shoppingCartService.applyCoupon(sc.getId(), coupon1));
		
		verify(shoppingCartRepository).findById(sc.getId());
		verify(shoppingCartRepository).save(sc);
		
		assertThat(sc.getCoupons().contains(coupon1));
		assertThat(sc.getCoupons().size() == 1);
		assertEquals(3680, sc.getDiscountedPrice());
		assertEquals(3780, sc.getTotalPrice());
	}
	
	@Test
	@DisplayName("Positive test for addNewItemToShoppingCart")
	public void addNewItemToShoppingCart() {
		ShoppingCart sc = new ShoppingCart();
		
		Item item1 = new Item();
		item1.setCategory("Sports");
		item1.setId("1");
		item1.setName("El Yayı - 200 Lbs");
		item1.setPrice(190);
		item1.setQuantity(2);
		item1.setUrl("");
		
		Item item2 = new Item();
		item2.setCategory("Technology");
		item2.setId("2");
		item2.setName("Kindle PaperWhite 4");
		item2.setPrice(3400);
		item2.setQuantity(1);
		item2.setUrl("");
		
		//the item that is going to be added
		Item item3 = new Item();
		item3.setCategory("Technology");
		item3.setId("3");
		item3.setName("Kindle PaperWhite 5");
		item3.setPrice(5000);
		item3.setQuantity(1);
		item3.setUrl("");
		
		Coupon coupon1 = new Coupon();
		coupon1.setAmount(100);
		coupon1.setId("1");
		coupon1.setRate(0);
		coupon1.setType("amount");

		Coupon coupon2 = new Coupon();
		coupon2.setAmount(0);
		coupon2.setId("2");
		coupon2.setRate(0.2);
		coupon2.setType("rate");
		
		sc.setCoupons(List.of(coupon1, coupon2));
		sc.setCustomerId("customer1");
		sc.setId("649821aafff");
		List<Item> i = new java.util.ArrayList<>();
		i.add(item1);
		i.add(item2);
		sc.setItems(i);
		
		when(shoppingCartRepository.findById(sc.getId())).thenReturn(Optional.of(sc));
		when(shoppingCartRepository.save(sc)).thenReturn(sc);
		
		sc.setItems(shoppingCartService.addNewItemToShoppingCart(sc.getId(), item3));
		
		verify(shoppingCartRepository).findById(sc.getId());
		verify(shoppingCartRepository).save(sc);
		
		assertEquals(3, sc.getItems().size());
		assertThat(sc.getItems().contains(item3));
		assertEquals(8780, sc.getTotalPrice());
		assertEquals(6944, sc.getDiscountedPrice());
	}
}