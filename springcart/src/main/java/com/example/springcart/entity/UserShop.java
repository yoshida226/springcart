package com.example.springcart.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "user_shop")
@Data
public class UserShop {
	@EmbeddedId
    private UserShopId id;

	@ManyToOne
    @MapsId("userId")
	@JoinColumn(name = "user_id")
	private User userId;
	
	@ManyToOne
    @MapsId("shopId")
	@JoinColumn(name = "shop_id")
	private Shop shopId;
	
	@Column(name = "created_date", insertable = false, updatable = false)
	private Timestamp createdDate;
}
