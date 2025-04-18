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
@Table(name = "verification_token")
@Data
public class VerificationToken {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	@ManyToOne
	@JoinColumn(name = "user")
	User user;
	
	@Column(name = "token")
	String token;
	
	@Column(name = "created_date", insertable = false, updatable = false)
	Timestamp createdDate;
	
	@Column(name = "expiry_date", insertable = false, updatable = false)
	Timestamp expiryDate;
}
