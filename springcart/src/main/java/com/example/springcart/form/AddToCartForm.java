package com.example.springcart.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddToCartForm {

	@NotNull
	private Integer id;
	
	@Min(value = 1, message = "1つ以上の数をせんたくしてください")
	private Integer amount;
	
	private Integer price;
}
