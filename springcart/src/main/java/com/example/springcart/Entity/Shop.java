package com.example.springcart.Entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "shop")
@Data
public class Shop {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;

	@Column(name = "created_date", insertable = false, updatable = false)
	private Timestamp createdDate;

	@Column(name = "updated_date", insertable = false, updatable = false)
	private Timestamp updatedDate;	
}
