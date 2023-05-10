# Shopping Cart API

# Important Note for FrontEnd Developers
After creating a shopping cart please store id of shopping cart, because you will need it for nearly every requests.

# How to Use
This is the base url where you can use it directly from clearnet, http://16.171.2.137:8080/v1/shopping-carts
# Endpoints

## Get all shopping carts - ("/v1/shopping-carts")
This endpoint returns list of every shopping carts.
### Returns
List\<ShoppingCart\>.
### Request Type
GET
### Parameters
No parameters.
### Request Body
No body.
### Responses
| Code | Description |
| ----------- | ----------- |
| 200 | Success |

---

## Get a shopping cart by id - ("/v1/shopping-carts/{id}")
This endpoint returns a ShoppingCart object that id={id}.
### Returns
ShoppingCart object.
### Request Type
GET
### Parameters
PathVariable: String id
### Request Body
No body.
### Responses
| Code | Description | Text |
| ----------- | ----------- | ----------- |
| 200 | Success ||
| 404 | ShoppingCartNotFoundException | There is no ShoppingCart with that id. |

---

## Get all items in a shopping cart - ("/v1/shopping-carts/{id}/items")
This endpoint returns a list of Items in a shopping cart that id={id}.
### Returns
List\<Item\>.
### Request Type
GET
### Parameters
PathVariable: String id
### Request Body
No body.
### Responses
| Code | Description | Text |
| ----------- | ----------- | ----------- |
| 200 | Success ||
| 404 | ShoppingCartNotFoundException | There is no ShoppingCart with that id. |

---

## Create new shopping cart - ("/v1/shopping-carts")
This endpoint creates the given shopping card and returns it back to the client.
### Returns
**ShoppingCart object** that is newly created.
### Request Type
POST
### Parameters
No parameters.
### Request Body
ShoppingCart object.
```
{
    "id": "STRING_ID" @Optional
    "customerId": "STRING_ID",
    "items": [ @Optional
        { @Optional
            "id": "STRING_ID_ITEM",
            "name": "STRING_NAME",
            "url": "STRING_URL",
            "price": DOUBLE_PRICE,
            "quantity": INTEGER_QUANTITY,
            "category": "STRING_CATEGORY",
            "createDate": "DATE", @Optional(auto-assigned)
            "changeDate": "DATE" @Optional(auto-assigned)
        }
    ],
    "coupons": [ @Optional
        {@Optional
            "id": "coupon1",
        },
    ],
    "totalPrice": DOUBLE_TOTALPRICE, @Optional(will be calculated and updated anyways)
    "discountedPrice": DOUBLE_DISCOUNTEDPRICE @Optional(will be calculated and updated anyways)
}
```
### Important Notes
- Total price and discounted price are going to be calculated by backend any time it needs to change(like at create, new item, delete item, apply coupon).
- You can use following coupons by changing id: "coupon1", "coupon2", "coupon2"
- You can add custom coupons by filling out every attributes of a coupon like so:
```
{
    "id": "STRING_ID",
    "type": "STRING_TYPE",
    "rate": DOUBLE_RATE,
    "amount": DOUBLE_AMOUNT
}
```
Type can be either 'rate' or 'amount'. If you pick 'rate' as type then amount should be 0, if you pick 'amount' as type then rate should be 0.
Rates should be between 0 and 1, and cannot be 0.
Amounts should be positive.
### Responses
| Code | Description | Text |
| ----------- | ----------- | ----------- |
| 201 | Created ||
| 400 | BadShoppingCartException | Price must be a positive number. Incorrect shopping cart. |
| 400 | BadCouponException | Type of a coupon cannot be neither empty nor null.<br>Type of a coupon must be either 'rate' or 'amount'.<br>Rate of a coupon of type rate, must be a positive value between 0 and 1.<br>Amount of a coupon of type amount, must be a positive value.<br>Amount of, a coupon of type rate must be zero.<br>Rate of, a coupon of type amount must be zero. |
| 400 | BadItemException | Item url cannot be empty.<br>Item name cannot be empty.<br>Category name cannot be empty.<br>Price must be a positive number.<br>Quantity must be a positive number.<br> |
| 409 | ItemAlreadyPresent | There are multiple items with the same id. Incorrect shopping cart. |
| 409 | CouponAlreadyPresentException | There are multiple coupons with the same id. Incorrect shopping cart. |
| 409 | ShoppingCartAlreadyPresentException | Customer already has a shopping cart. You may want to update it or delete then recreate it.<br>ShoppingCart with that id already present. You may want to update it or delete then recreate it. |

---

## Add new item to a shopping cart - ("/v1/shopping-carts/{id}/items")
This endpoint returns a list of items after adding the given item to the items list of a shopping cart that id={id}.
### Returns
**List\<Item\>** that is updated recently.
### Request Type
POST
### Parameters
PathVariable: String id
### Request Body
Item object.
```
{
    "id": "STRING_ID_ITEM",
    "name": "STRING_NAME",
    "url": "STRING_URL",
    "price": DOUBLE_PRICE,
    "quantity": INTEGER_QUANTITY,
    "category": "STRING_CATEGORY",
    "createDate": "DATE", @Optional(auto-assigned)
    "changeDate": "DATE" @Optional(auto-assigned)
}
```
### Responses
| Code | Description | Text |
| ----------- | ----------- | ----------- |
| 200 | Success ||
| 400 | BadItemException | Item url cannot be empty.<br>Item name cannot be empty.<br>Category name cannot be empty.<br>Price must be a positive number.<br>Quantity must be a positive number.<br> |
| 400 | BadShoppingCartException | Price must be a positive number. Incorrect shopping cart. |
| 404 | ShoppingCartNotFoundException | There is no ShoppingCart with that id. |
| 409 | ItemAlreadyPresent | There are multiple items with the same id. Incorrect shopping cart. |

---

## Apply a coupon - ("/v1/shopping-carts/{id}/coupons")
Apply a coupon to a specific shopping cart that id={id}. Total price and discounted price will be automatically updated if coupon is correct.
### Returns
**Coupon object** that is newly created.
### Request Type
POST
### Parameters
PathVariable: String id
### Request Body
Coupon object.
If you are going to use the first structure then id must be either 'coupon1', 'coupon2' or 'coupon3'.
```
{
    "id": "STRING_ID"
}
If you are going to use the second stucture then coupon id must be different than first one's and other coupons at the shopping cart.
{
    "id": "STRING_ID",
    "type": "STRING_TYPE",
    "rate": DOUBLE_RATE,
    "amount": DOUBLE_AMOUNT
}
```
### Important Notes
Type must be either 'rate' or 'amount'. If you picked 'rate' then amount should be 0 and rate should be between 0 and 1. If you picked 'amount' then rate should be 0 and amount should be positive. Id must be different than other coupons' id at the specific shopping cart.
### Responses
| Code | Description | Text |
| ----------- | ----------- | ----------- |
| 201 | Created ||
| 400 | BadCouponException | Type of a coupon cannot be neither empty nor null.<br>Type of a coupon must be either 'rate' or 'amount'.<br>Rate of a coupon of type rate, must be a positive value between 0 and 1.<br>Amount of a coupon of type amount, must be a positive value.<br>Amount of, a coupon of type rate must be zero.<br>Rate of, a coupon of type amount must be zero. |
| 404 | ShoppingCartNotFoundException | There is no ShoppingCart with that id. |
| 409 | CouponAlreadyPresentException | There are multiple coupons with the same id. Incorrect shopping cart. |

## Delete an item - ("/v1/shopping-carts/{id}/items/{itemId}")
This endpoint deletes an item from a shopping cart. And returns nothing.
### Returns
Void.
### Request Type
DELETE
### Parameters
PathVariable: String id <br>
PathVariable: String itemId
### Request Body
No body.
### Responses
| Code | Description | Text |
| ----------- | ----------- | ----------- |
| 201 | Created ||
| 404 | ShoppingCartNotFoundException | There is no ShoppingCart with that id. |
| 404 | ItemNotFoundException | There is no Item with that id. |