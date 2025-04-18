package com.example.springcart.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springcart.entity.Cart;
import com.example.springcart.entity.User;
import com.example.springcart.form.AddToCartForm;
import com.example.springcart.repository.CartRepository;
import com.example.springcart.repository.UserRepository;
import com.example.springcart.service.CartService;
import com.example.springcart.session.SessionCart;
import com.example.springcart.session.SessionCartItem;

@Controller
public class CartController {
	private final CartService cartService;
	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	
	public CartController(CartService cartService, UserRepository userRepository, CartRepository cartRepository) {
		this.cartService = cartService;
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
	}
	
	@PostMapping("/order/cart")
	public String addToCart(@ModelAttribute @Validated AddToCartForm addToCartForm,
			 				BindingResult result,
			 				RedirectAttributes redirectAttributes,
			 				HttpSession session,
			 				SessionCart sessionCart,
							Model model) {
		
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addToCartForm", result);
			redirectAttributes.addFlashAttribute("addToCartForm", addToCartForm);
			
			Integer productId = addToCartForm.getProductId().getId();
			
			return "redirect:/product/" + productId;
		}
		
		
		// 認証情報を取得
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //カートへ保存
        cartService.addToCart(addToCartForm, auth, session);
		
		return "redirect:/order/cart";
	}
	
	@GetMapping("/order/cart")
	public String showCart(Authentication auth,
						   HttpSession session,
						   Model model) {
		
        if(auth == null || !auth.isAuthenticated()) {
        	//未ログイン時はセッションカートに保存
        	SessionCart sessionCart = (SessionCart) session.getAttribute("sessionCart");
        	
        	if(sessionCart != null) {
            	List<SessionCartItem> sessionCartItems = sessionCart.getItems();
        		model.addAttribute("cartItem", sessionCartItems);
        		model.addAttribute("totalAmount", cartService.calculateTotalAmount(sessionCartItems, null));
        		model.addAttribute("isEmpty", sessionCartItems.isEmpty());
        	} else {
        		model.addAttribute("cartItem", new SessionCart());
        		model.addAttribute("totalAmount", 0);
        		model.addAttribute("isEmpty", true);
        	}
        	
        } else {
        	//ログイン時はカートテーブルからカートの商品を取得
        	User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        	List<Cart> carts = cartRepository.findByUserId(user);
        
        	model.addAttribute("cartItem", carts);
    		model.addAttribute("totalAmount", cartService.calculateTotalAmount(null, carts));
        	model.addAttribute("isEmpty", carts.isEmpty());
        }
		
		return "order/cart";
	}
}
