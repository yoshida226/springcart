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
@Table(name = "orders")
@Data
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	Integer id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	User userId;
	
	@Column(name = "total_price")
	Integer totalPrice;
	
	@Column(name = "ordered_at", insertable = false, updatable = false)
	Timestamp orderedAt;
	
	public Orders(User userId, Integer totalPrice) {
		this.userId = userId;
		this.totalPrice = totalPrice; 
	}
}
