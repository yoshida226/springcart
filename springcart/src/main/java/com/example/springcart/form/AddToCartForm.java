package com.example.springcart.form;

import jakarta.validation.constraints.NotNull;

import com.example.springcart.entity.Product;
import com.example.springcart.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddToCartForm {

	@NotNull
	private Product productId;
	
	private User userId;
	
	@NotNull(message = "1以上の値を選択してください")
	private Integer quantity;
}
