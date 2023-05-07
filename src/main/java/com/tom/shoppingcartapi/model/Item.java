package com.tom.shoppingcartapi.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

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
	private int price;
	private String category;
	private Date createDate = new Date();
	private Date changeDate = new Date();
}
