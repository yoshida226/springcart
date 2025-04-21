package com.example.springcart.entity;
import lombok.Data;

@Data
public final class SessionCartItem extends BaseCartItem {
	
	private Product productId;
	private Integer quantity;
	
	public SessionCartItem(Product productId, Integer quantity) {
		this.productId = productId;
        this.quantity = quantity;
	}
	
	@Override
    public Product getProductId() {
        return this.productId;
    }
    
    @Override
    public Integer getQuantity() {
        return this.quantity;
    }

}
