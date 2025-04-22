package com.example.springcart.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "user_shop")
@Data
public class UserShop {
	@ManyToOne
	@JoinColumn(name = "user_id")
	User userId;
	
	@ManyToOne
	@JoinColumn(name = "shop_id")
	User shopId;
	
	@Column(name = "created_date", insertable = false, updatable = false)
	Timestamp createdDate;
}
