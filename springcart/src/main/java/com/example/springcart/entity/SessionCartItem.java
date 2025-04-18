package com.example.springcart.entity;
import lombok.Data;

@Data
public final class SessionCartItem extends BaseCartItem {
	
	public SessionCartItem(Product productId, Integer quantity) {
		super(productId, quantity);
	}

}
