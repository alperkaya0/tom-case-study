# Shopping Cart API

# How to Use

# Endpoints

## Get all shopping carts - ("/v1/shopping-carts")
This endpoint returns list of every shopping carts.
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
### Request Type
GET
### Parameters
PathVariable: String id
### Request Body
No body.
### Responses
| Code | Description |
| ----------- | ----------- |
| 200 | Success |
| 404 | ShoppingCartNotFoundException |

---

## Get items in a shopping cart - ("/v1/shopping-carts/{id}/items")
This endpoint returns a list of Items in a shopping cart that id={id}.
### Request Type
GET
### Parameters
PathVariable: String id
### Request Body
No body.
### Responses
| Code | Description |
| ----------- | ----------- |
| 200 | Success |
| 404 | ShoppingCartNotFoundException |

---

## Create new shopping cart - ("/v1/shopping-carts")
This endpoint creates the given shopping card and returns it back to the client.
### Request Type
POST
### Parameters
No parameters.
### Request Body
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
| Code | Description |
| ----------- | ----------- |
| 201 | Created |
| 400 | BadShoppingCartException |
| 400 | BadCouponException |
| 400 | BadItemException |
| 409 | ItemAlreadyPresent |
| 409 | CouponAlreadyPresentException |

---

## Add new item to a shopping cart - ("/v1/shopping-carts/{id}/items")
This endpoint returns a list of items after adding the given item to the items list of a shopping cart that id={id}.
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
| Code | Description |
| ----------- | ----------- |
| 200 | Success |
| 400 | BadItemException |
| 400 | BadShoppingCartException |
| 400 | BadCouponException |
| 404 | ShoppingCartNotFoundException |
| 409 | ItemAlreadyPresent |

---

