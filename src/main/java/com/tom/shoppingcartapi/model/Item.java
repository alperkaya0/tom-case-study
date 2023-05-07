package com.tom.shoppingcartapi.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //will create getters and setters
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	/*What does an item have?
	  - Id
	  - Name
	  - Price
	  - (Maybe) Category
	  - Create date
	  - Change date
	*/
	@Id
	private String id;
	private String name;
	private double price;
	private int quantity;
	private String category;
	@JsonInclude(JsonInclude.Include.NON_NULL) //with this, you don't have to give totalPrice at POST request, but it will show up at GET request
	private Date createDate = new Date();
	@JsonInclude(JsonInclude.Include.NON_NULL) //with this, you don't have to give totalPrice at POST request, but it will show up at GET request
	private Date changeDate = new Date();
}
