package com.example.springcart.entity;

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
@Table(name = "order_detail")
@Data
public class OrderDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	Integer id;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	Orders orderId;

	@ManyToOne
	@JoinColumn(name = "product_id")
	Product productId;
	
	@Column(name = "quantity")
	Integer quantity;
	
	@Column(name = "price")
	Integer price;
	
	public OrderDetail(Orders orderId, Product productId, Integer quantity, Integer price) {
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
	}
}
