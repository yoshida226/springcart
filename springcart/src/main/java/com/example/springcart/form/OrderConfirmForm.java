package com.example.springcart.form;

import com.example.springcart.entity.Product;
import com.example.springcart.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderConfirmForm {
	private Product productId;
	private User userId;
	private Integer quantity;
}
