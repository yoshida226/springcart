package com.example.springcart.service;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.springcart.dto.SessionCart;
import com.example.springcart.entity.BaseCartItem;
import com.example.springcart.entity.CartItem;
import com.example.springcart.entity.Product;
import com.example.springcart.entity.SessionCartItem;
import com.example.springcart.entity.User;
import com.example.springcart.form.AddToCartForm;
import com.example.springcart.repository.CartItemRepository;
import com.example.springcart.repository.ProductRepository;
import com.example.springcart.repository.UserRepository;
import com.example.springcart.util.AuthUtil;

@Service
public class CartService {
	private final ProductRepository productRepository;
	private final ObjectFactory<SessionCart> sessionCartFactory;
	private final UserRepository userRepository;
	private final CartItemRepository cartItemRepository;
	private final AuthUtil authUtil;
	
	public CartService(ProductRepository productRepository, ObjectFactory<SessionCart> sessionCartFactory, UserRepository userRepository, CartItemRepository cartItemRepository, AuthUtil authUtil) {
		this.productRepository = productRepository;
		this.sessionCartFactory = sessionCartFactory;
		this.userRepository = userRepository;
		this.cartItemRepository = cartItemRepository;
		this.authUtil = authUtil;
	}
	
	//カートの商品を取得
	public List<? extends BaseCartItem> getCartItems(Authentication auth,
								   			     	 HttpSession session) {

		if(authUtil.isLoggedIn(auth)) {
			//ログイン時はカートテーブルからカートの商品を取得
			User user = userRepository.findByEmail(auth.getName()).orElseThrow();
			List<CartItem> cartItems = cartItemRepository.findByUserId(user);

			return cartItems;
		
		} else {
			//未ログイン時はセッションカートを確認
			SessionCart sessionCart = (SessionCart) session.getAttribute("sessionCart");
			//すでにセッションカートが存在する場合、その中のアイテムを取得
			if(sessionCart != null) {
				List<SessionCartItem> sessionCartItems = sessionCart.getItems();
				
				return sessionCartItems;
			}
			
			return null;
		}
	}
	
	
	//カート内のアイテムの重複をチェックし、重複している場合マージする
	public String addToCart(AddToCartForm form, Authentication auth, HttpSession session) {
		
		if(authUtil.isLoggedIn(auth)) {
	        // ログイン済
	    	User user = userRepository.findByEmail(auth.getName()).orElseThrow();
	    	List<CartItem> items = cartItemRepository.findByUserId(user);
        	
	    	//同じ商品がすでにカートに存在する場合マージする
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
        	
        	//同じ商品がすでにカートに存在しない場合
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

	    } else {
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
	    } 
		
	}
	
	//カートから商品を削除
	public void remove(Integer productId, Authentication auth, HttpSession session) {
		
		if(authUtil.isLoggedIn(auth)) {
			//ログイン中の場合カートテーブルから削除
			Optional<Product> product = productRepository.findById(productId);
			CartItem cartItem = cartItemRepository.findByProductId(product.orElseThrow());
			cartItemRepository.delete(cartItem);
		} else {
			//未ログインの場合セッションカートから削除
			SessionCart sessionCart = (SessionCart) session.getAttribute("sessionCart");
			List<SessionCartItem> sessionCartItem = sessionCart.getItems();
			sessionCartItem.removeIf(item -> item.getProductId().getId().equals(productId));
		}
		
	}

}
