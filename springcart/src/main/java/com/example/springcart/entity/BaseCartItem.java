package com.example.springcart.entity;

import lombok.Data;

@Data
public sealed abstract class BaseCartItem permits CartItem, SessionCartItem {
	public BaseCartItem() {}
	
    public abstract Product getProductId();
    
    public abstract Integer getQuantity();
}
