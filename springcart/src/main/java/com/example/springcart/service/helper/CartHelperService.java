package com.example.springcart.service.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springcart.entity.BaseCartItem;

@Service
public class CartHelperService {
	//合計金額を計算
	public Integer calculateTotalPrice(List<? extends BaseCartItem> baseCartItem) {
		
		List<Integer> prices = new ArrayList<>();
		
		if(baseCartItem != null && !baseCartItem.isEmpty()) {
			for(BaseCartItem s : baseCartItem) {
				prices.add(( s.getProductId().getPrice() * s.getQuantity() ));
			}
 		} else {
 			prices.add(0);
 		}
		
		Integer sum = prices.stream().mapToInt(Integer::intValue).sum();
		
		return sum;
	}
	
}
