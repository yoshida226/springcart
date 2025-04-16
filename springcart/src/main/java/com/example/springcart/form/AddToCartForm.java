package com.example.springcart.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.example.springcart.Entity.Product;
import com.example.springcart.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddToCartForm {

	@NotNull
	private Product productId;
	
	private User userId;
	
	@Min(value = 1, message = "1つ以上の個数を選択してください")
	private Integer quantity;
}
