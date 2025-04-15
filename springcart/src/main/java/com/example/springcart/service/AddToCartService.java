package com.example.springcart.service;

import org.springframework.stereotype.Service;

@Service
public class AddToCartService {

	public Integer calculatePrice(Integer price, Integer amount) {
		return price * amount;
	}
}
