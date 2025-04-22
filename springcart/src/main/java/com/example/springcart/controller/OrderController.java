package com.example.springcart.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.springcart.entity.CartItem;
import com.example.springcart.entity.Orders;
import com.example.springcart.entity.User;
import com.example.springcart.form.OrderConfirmForm;
import com.example.springcart.repository.CartItemRepository;
import com.example.springcart.repository.UserRepository;
import com.example.springcart.service.OrderService;
import com.example.springcart.service.helper.CartHelperService;
import com.example.springcart.util.AuthUtil;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private AuthUtil authUtil;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderService orderService;
	
	@Autowired
	CartItemRepository cartItemRepository;
	
	@Autowired
	CartHelperService cartHelperService;
	
	//注文確認
	@GetMapping("/confirm")
	public String showConfirmPage(Authentication auth,
								  HttpSession session,
								  Model model) {
		
		//未ログインの場合、一旦ログインする
		if (!authUtil.isLoggedIn(auth)) {
	        session.setAttribute("redirectAfterLogin", "/order/confirm");
	        return "redirect:/login";
	    }
		
		User user = authUtil.getUserByAuth(auth);
		
		//セッションに保存していたアイテムをDBに移す
		orderService.merge(user, session);
		
		//OrderConfirmFormにログインユーザのCartItemを格納
		List<CartItem> cartItem = cartItemRepository.findByUserId(user);
		List<OrderConfirmForm> orderConfirmForms = new ArrayList<>();
		cartItem.forEach(item -> orderConfirmForms.add(new OrderConfirmForm(item.getProductId(), item.getUserId(), item.getQuantity())));
		
		Integer totalPrice = cartHelperService.calculateTotalPrice(cartItem);
		model.addAttribute("orderConfirmForms", orderConfirmForms);
		model.addAttribute("totalPrice", totalPrice);
		session.setAttribute("orderConfirmForms", orderConfirmForms);;
		session.setAttribute("totalPrice", totalPrice);
		
		return "order/confirm";
	}
	
	//注文確定
	@PostMapping("/complete")
	public String completeOrder(HttpSession session,
							    Authentication auth) {
		
		List<OrderConfirmForm> orderConfirmForms = (List<OrderConfirmForm>) session.getAttribute("orderConfirmForms");
		Integer totalPrice = (Integer) session.getAttribute("totalPrice");
		orderService.completeOrder(orderConfirmForms, totalPrice, auth);
		
		return "redirect:/order/history";
	}
	
	//注文履歴閲覧
	@GetMapping("/history")
	public String showOrderHistory(Authentication auth, Model model) {
		List<Orders> orders = orderService.getHistory(auth);
		model.addAttribute("orders", orders);
		
		return "order/history";
	}
}
