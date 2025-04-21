package com.example.springcart.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springcart.dto.SessionCart;
import com.example.springcart.entity.BaseCartItem;
import com.example.springcart.form.AddToCartForm;
import com.example.springcart.repository.CartItemRepository;
import com.example.springcart.repository.UserRepository;
import com.example.springcart.service.CartService;
import com.example.springcart.service.helper.CartHelperService;
import com.example.springcart.util.AuthUtil;

@Controller
@RequestMapping("/order/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private AuthUtil authUtil;
	
	@Autowired
	private CartHelperService cartHelperService;
	
	//カートを表示
	@GetMapping
	public String showCart(Authentication auth,
						   HttpSession session,
						   Model model) {
		
		List<? extends BaseCartItem> cartItems = cartService.getCartItems(auth, session);
		
        if(cartItems != null) {
        	model.addAttribute("isEmpty", cartItems.isEmpty());
        	model.addAttribute("cartItem", cartItems);
    		model.addAttribute("totalPrice", cartHelperService.calculateTotalPrice(cartItems));
        } else {
    		model.addAttribute("isEmpty", true);
    		model.addAttribute("cartItem", new SessionCart());
    		model.addAttribute("totalPrice", 0);
        }
		
		return "order/cart";
	}
	
	//カートに入れる
	@PostMapping
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
	
	//カートから商品を削除
	@PostMapping("/remove/{productId}")
	public String removeItemFromCart(@PathVariable Integer productId,
									 Authentication auth,
									 HttpSession session,
									 RedirectAttributes redirectAttributes) {
		
		cartService.remove(productId, auth, session);
		redirectAttributes.addFlashAttribute("message", "カートからアイテムを削除しました");
		
		return "redirect:/order/cart";
	}
	
}
