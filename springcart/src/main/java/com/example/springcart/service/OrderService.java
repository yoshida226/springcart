package com.example.springcart.service;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springcart.dto.SessionCart;
import com.example.springcart.entity.CartItem;
import com.example.springcart.entity.OrderDetail;
import com.example.springcart.entity.Orders;
import com.example.springcart.entity.Product;
import com.example.springcart.entity.SessionCartItem;
import com.example.springcart.entity.User;
import com.example.springcart.form.OrderConfirmForm;
import com.example.springcart.repository.CartItemRepository;
import com.example.springcart.repository.OrderDetailRepository;
import com.example.springcart.repository.OrdersRepository;
import com.example.springcart.repository.ProductRepository;
import com.example.springcart.service.helper.CartHelperService;
import com.example.springcart.util.AuthUtil;

@Service
public class OrderService {
	
	@Autowired
	CartItemRepository cartItemRepository;
	
	@Autowired
	CartHelperService cartHelperService;
	
	@Autowired
	AuthUtil authUtil;
	
	@Autowired
	OrdersRepository ordersRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	//セッションカートアイテムとDBのカートアイテムをマージしてDBに保存する
	public void merge(User user,
					  HttpSession session) {
		
		SessionCart sessionCart = (SessionCart) session.getAttribute("sessionCart");
		
		if(sessionCart != null) {
			List<SessionCartItem> sessionCartItems = sessionCart.getItems();
			List<CartItem> cartItems = cartItemRepository.findByUserId(user);
			
			for(SessionCartItem sessionCartItem : sessionCartItems) {
				
				//SessionCartItemとCartItemに同じ商品が入っている場合マージする
				for(CartItem cartItem : cartItems) {
					if(sessionCartItem.getProductId().getId().equals(cartItem.getProductId().getId())) {
						
						if(sessionCartItem.getQuantity() + cartItem.getQuantity() > sessionCartItem.getProductId().getStock()) {
							
		    			} else {
		    				cartItem.setQuantity(sessionCartItem.getQuantity() + cartItem.getQuantity());
		        			cartItemRepository.save(cartItem);
		    			}
					}
				}
					
				CartItem newCartItem = new CartItem(sessionCartItem.getProductId(), sessionCartItem.getQuantity(), user);	
				cartItemRepository.save(newCartItem);
			}
			
			sessionCart.clear();
		}
	}
	
	//orderデータとorder詳細データを作成
	@Transactional
	public void completeOrder(List<OrderConfirmForm> orderConfirmForms,
						      Integer totalPrice,
						      Authentication auth) {
		
		User user = authUtil.getUserByAuth(auth);
		
		//オーダー情報を保存
		Orders order = new Orders(user, totalPrice);
		ordersRepository.save(order);
		
		orderConfirmForms.forEach(item -> {
			//オーダー詳細を保存
			OrderDetail orderDetail = new OrderDetail(
				order,
				item.getProductId(),
				item.getQuantity(),
				item.getProductId().getPrice()
			);
			orderDetailRepository.save(orderDetail);
			
			//商品の在庫を変更する
			Product product = productRepository.findById(item.getProductId().getId()).orElseThrow();
			product.setStock(product.getStock() - item.getQuantity());
			productRepository.save(product);
			
			//カートの内容を削除
			List<CartItem> cartItem = cartItemRepository.findByUserId(user);
			cartItemRepository.deleteAll(cartItem);
		});
	}
	
	//ユーザの注文履歴を取得する
	public List<Orders> getHistory(Authentication auth) {
		User user = authUtil.getUserByAuth(auth);
		return ordersRepository.findByUserIdWithDetails(user.getId());
	}
}
