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

import com.example.springcart.entity.CartItem;
import com.example.springcart.entity.SessionCartItem;
import com.example.springcart.entity.User;
import com.example.springcart.form.AddToCartForm;
import com.example.springcart.repository.CartItemRepository;
import com.example.springcart.repository.UserRepository;
import com.example.springcart.service.CartService;
import com.example.springcart.session.SessionCart;

@Controller
public class CartController {
	private final CartService cartService;
	private final UserRepository userRepository;
	private final CartItemRepository cartItemRepository;
	
	public CartController(CartService cartService, UserRepository userRepository, CartItemRepository cartItemRepository) {
		this.cartService = cartService;
		this.userRepository = userRepository;
		this.cartItemRepository = cartItemRepository;
	}
	
	@PostMapping("/order/cart")
	public String addToCart(@ModelAttribute @Validated AddToCartForm addToCartForm,
			 				BindingResult result,
			 				RedirectAttributes redirectAttributes,
			 				HttpSession session,
			 				SessionCart sessionCart,
							Model model) {

		Integer productId = addToCartForm.getProductId().getId();
		
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addToCartForm", result);
			redirectAttributes.addFlashAttribute("addToCartForm", addToCartForm);
			
			
			return "redirect:/product/" + productId;
		}
		
		
		// 認証情報を取得
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //カートへ保存
        String addToCartResult = cartService.addToCart(addToCartForm, auth, session);
        
        if(addToCartResult == "在庫超過") {
			redirectAttributes.addFlashAttribute("error", "カートの商品との合計個数が在庫を超過しています");
        	return "redirect:/product/" + productId;
        }
		
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
        		//すでにセッションカートが存在する場合、その中のアイテムを取得
            	List<SessionCartItem> sessionCartItems = sessionCart.getItems();

        		model.addAttribute("isEmpty", sessionCartItems.isEmpty());
        		model.addAttribute("cartItem", sessionCartItems);
        		model.addAttribute("totalAmount", cartService.calculateTotalAmount(sessionCartItems));
        		
        	} else {
        		
        		model.addAttribute("isEmpty", true);
        		model.addAttribute("cartItem", new SessionCart());
        		model.addAttribute("totalAmount", 0);
        	}
        	
        } else {
        	//ログイン時はカートテーブルからカートの商品を取得
        	User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        	List<CartItem> cartItems = cartItemRepository.findByUserId(user);

        	model.addAttribute("isEmpty", cartItems.isEmpty());
        	model.addAttribute("cartItem", cartItems);
    		model.addAttribute("totalAmount", cartService.calculateTotalAmount(cartItems));
        }
		
		return "order/cart";
	}
}
