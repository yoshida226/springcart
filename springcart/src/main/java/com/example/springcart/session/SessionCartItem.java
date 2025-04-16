package com.example.springcart.session;
import jakarta.validation.constraints.NotNull;

import com.example.springcart.Entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionCartItem {
	@NotNull
	private Product productId;
	
	private Integer quantity;
}
