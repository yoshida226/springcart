package com.example.springcart.session;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.springcart.form.AddToCartForm;

@Component
public class SessionCart {
	private List<SessionCartItem> items = new ArrayList<>();
	
	public void addItem(AddToCartForm form) {
		SessionCartItem sessionCartItem = new SessionCartItem(form.getProductId(), form.getQuantity());
		items.add(sessionCartItem);
	}
	
	public List<SessionCartItem> getItems() {
		return items;
	}
	
	public void clear() {
		items.clear();
	}
}