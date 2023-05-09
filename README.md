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

## Get items in a shopping cart - ("/v1/shopping-carts/{id}/items")
This endpoint returns a list of Items in a shopping cart that id={id};
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
