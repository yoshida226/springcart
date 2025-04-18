package com.example.springcart.service;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.springcart.entity.BaseCartItem;
import com.example.springcart.entity.CartItem;
import com.example.springcart.entity.Product;
import com.example.springcart.entity.SessionCartItem;
import com.example.springcart.entity.User;
import com.example.springcart.form.AddToCartForm;
import com.example.springcart.repository.CartItemRepository;
import com.example.springcart.repository.ProductRepository;
import com.example.springcart.repository.UserRepository;
import com.example.springcart.session.SessionCart;

@Service
public class CartService {
	private final ProductRepository productRepository;
	private final ObjectFactory<SessionCart> sessionCartFactory;
	private final UserRepository userRepository;
	private final CartItemRepository cartItemRepository;
	
	
	public CartService(ProductRepository productRepository, ObjectFactory<SessionCart> sessionCartFactory, UserRepository userRepository, CartItemRepository cartItemRepository) {
		this.productRepository = productRepository;
		this.sessionCartFactory = sessionCartFactory;
		this.userRepository = userRepository;
		this.cartItemRepository = cartItemRepository;
	}
	
	
	//カート内のアイテムの重複をチェックし、重複している場合マージする
	public String addToCart(AddToCartForm form, Authentication auth, HttpSession session) {
		
		if (auth == null || auth instanceof AnonymousAuthenticationToken) {
	        // 未ログイン    	
	    	SessionCart sessionCart = (SessionCart) session.getAttribute("sessionCart");
	        if (sessionCart == null) {
	        	//初めてカートに保存する場合
	            sessionCart = new SessionCart();
	            session.setAttribute("sessionCart", sessionCart);

		        sessionCart.addItem(form);
		        
		        return "保存成功";
		        
	        } else {
	        	//2回目以降の場合、これまでカートに保存したアイテムを取得する
	        	List<SessionCartItem> items = sessionCart.getItems();
	        	
	        	for(SessionCartItem item : items) {
	        		if(item.getProductId().getId().equals(form.getProductId().getId())) {
	        			//重複した商品をすでにカートに入れている場合
	        			
	        			if(item.getQuantity() + form.getQuantity() > form.getProductId().getStock()) {
	        				//在庫を超える場合
	        				return "在庫超過";
	        			} else {
		        			item.setQuantity(item.getQuantity() + form.getQuantity());
		        			//在庫の範囲内の場合
		        			return "保存成功";
	        			}
	        			
	        		}
	        	}
        		
	        	sessionCart.addItem(form);
    			return "保存成功";
	        }
	    } else {
	        // ログイン済
	    	User user = userRepository.findByEmail(auth.getName()).orElseThrow();
	    	List<CartItem> items = cartItemRepository.findByUserId(user);
        	
        	for(CartItem item : items) {
        		if(item.getProductId().getId().equals(form.getProductId().getId())) {
        			
        			if(item.getQuantity() + form.getQuantity() > form.getProductId().getStock()) {
        				//在庫を超える場合
        				return "在庫超過";
        			} else {
        				//在庫の範囲内の場合
	        			item.setQuantity(item.getQuantity() + form.getQuantity());
	        			cartItemRepository.save(item);
	        			
	        			return "保存成功";
        			}
        		}
        	}
        			
			if(form.getQuantity() > form.getProductId().getStock()) {
				//在庫を超える場合
				return "在庫超過";
			} else {
				//在庫の範囲内の場合
				Product product = productRepository.findById(form.getProductId().getId()).orElseThrow();
    	        CartItem cartItem = new CartItem(product, form.getQuantity(), user);
    	        cartItemRepository.save(cartItem);
    			return "保存成功";
    			
			}

	    }
		
	}

	
	//カートへ入れる操作（未ログイン時とログイン時で処理を分ける）
//	public void addToCart(AddToCartForm form, Authentication auth, HttpSession session) {
//	    if (auth == null || auth instanceof AnonymousAuthenticationToken) {
//	        // 未ログイン → セッションカートに追加	    	
//	    	SessionCart sessionCart = (SessionCart) session.getAttribute("sessionCart");
//	        if (sessionCart == null) {
//	            sessionCart = new SessionCart();
//	            session.setAttribute("sessionCart", sessionCart);
//	        }
//	        sessionCart.addItem(form);
//	    } else {
//	        // ログイン済 → DBカートに追加
//	        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
//	        Product product = productRepository.findById(form.getProductId().getId()).orElseThrow();
//	        CartItem cartItem = new CartItem(product, form.getQuantity(), user);
//	        cartItemRepository.save(cartItem);
//	    }
//	}

	
	//カートの合計金額を計算
	public Integer calculateTotalAmount(List<? extends BaseCartItem> baseCartItem) {
		
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
