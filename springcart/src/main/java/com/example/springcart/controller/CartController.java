package com.example.springcart.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springcart.form.AddToCartForm;
import com.example.springcart.service.CartService;

@Controller
public class CartController {
	private final CartService cartService;
	
	public CartController(CartService cartService) {
		this.cartService = cartService;
	}
	
	@PostMapping("/order/cart")
	public String addToCart(@ModelAttribute @Validated AddToCartForm addToCartForm,
			 				BindingResult result,
			 				RedirectAttributes redirectAttributes,
							Model model) {
		
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addToCartForm", result);
			redirectAttributes.addFlashAttribute("addToCartForm", addToCartForm);
			
			Integer productId = addToCartForm.getProductId().getId();
			
			return "redirect:/product/" + productId;
		}
		
		// 認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //カートへ保存
        cartService.addToCart(addToCartForm, authentication);
		
		return "order/cart";
	}
}
