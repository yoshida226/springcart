package com.example.springcart.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.springcart.Entity.Cart;
import com.example.springcart.Entity.Product;
import com.example.springcart.Entity.User;
import com.example.springcart.form.AddToCartForm;
import com.example.springcart.repository.CartRepository;
import com.example.springcart.repository.ProductRepository;
import com.example.springcart.repository.UserRepository;
import com.example.springcart.session.SessionCart;

@Service
public class CartService {
	private final ProductRepository productRepository;
	private final SessionCart sessionCart;
	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	
	
	public CartService(ProductRepository productRepository, SessionCart sessionCart, UserRepository userRepository, CartRepository cartRepository) {
		this.productRepository = productRepository;
		this.sessionCart = sessionCart;
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
	}
	
	//カートへ入れる操作（未ログイン時とログイン時で処理を分ける）
	public void addToCart(AddToCartForm form, Authentication auth) {
	    if (auth == null || auth instanceof AnonymousAuthenticationToken) {
	        // 未ログイン → セッションカートに追加
	        sessionCart.addItem(form);
	    } else {
	        // ログイン済 → DBカートに追加
	        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
	        Product product = productRepository.findById(form.getProductId().getId()).orElseThrow();
	        Cart cart = new Cart();
	        cart.setUserId(user);
	        cart.setProductId(product);
	        cart.setQuantity(form.getQuantity());
	        cartRepository.save(cart);
	    }
	}
}
