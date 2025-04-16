package com.example.springcart.Entity;

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
@Table(name = "cart")
@Data
public class Cart {
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
}
