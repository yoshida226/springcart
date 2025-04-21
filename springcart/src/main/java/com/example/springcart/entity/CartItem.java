package com.example.springcart.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "cart_item")
@Data
public final class CartItem extends BaseCartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User userId;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product productId;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "added_at", insertable = false, updatable = false)
	private Timestamp addedAt;
	
	public CartItem() {}
	
	public CartItem(Product productId, Integer quantity, User userId) {
		this.productId = productId;
        this.quantity = quantity;
		this.userId = userId;
	}
	
	@Override
    public Product getProductId() {
        return this.productId;  // フィールド名を変更しない場合
    }
    
    @Override
    public Integer getQuantity() {
        return this.quantity;
    }
}
