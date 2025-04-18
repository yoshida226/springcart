package com.example.springcart.entity;

import lombok.Data;

@Data
public sealed class BaseCartItem permits CartItem, SessionCartItem {
	protected Product productId;
	protected Integer quantity;
	
	public BaseCartItem(Product productId, Integer quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}
}
