package com.example.springcart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springcart.form.UserRegisterForm;
import com.example.springcart.repository.UserRepository;
import com.example.springcart.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	private final UserRepository userRepository;
	private final UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public UserController(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}

	//ユーザ登録画面表示
	@GetMapping("/register")
	public String showResisterForm(Model model) {
		
		model.addAttribute("userRegisterForm", new UserRegisterForm());
		
		return "user/register";
	}
	
	//ユーザ登録
	@PostMapping("/register")
	public String resisterUser(@ModelAttribute @Validated UserRegisterForm userRegisterForm,
							   BindingResult result,
							   RedirectAttributes redirectAttributes,
							   Model model) {
		
		//基本的な入力チェック
		if(result.hasErrors()) {
			System.out.println("エラー情報:" + result);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterForm", result);
			redirectAttributes.addFlashAttribute("userRegisterForm", userRegisterForm);
			
			return "/user/register";
		}
		
		//Email重複チェック
		if(userService.isEmailAlreadyExists(userRegisterForm)) {
			redirectAttributes.addFlashAttribute("errorMessage", "すでに登録されているEmailです");
			redirectAttributes.addFlashAttribute("userRegisterForm", userRegisterForm);
			
			return "redirect:/user/register";
		}
		
		//パスワード一致チェック
		if(userService.isNotSamePassword(userRegisterForm)) {
			redirectAttributes.addFlashAttribute("errorMessage", "パスワードが一致しません");
			redirectAttributes.addFlashAttribute("userRegisterForm", userRegisterForm);
			
			return "redirect:/user/register";
		}
		
		userService.registerUser(userRegisterForm);
		
		redirectAttributes.addFlashAttribute("successMessage", "ユーザが登録されました");
		
		// 登録後に自動ログイン
		autoLogin(userRegisterForm.getEmail(), passwordEncoder.encode(userRegisterForm.getPassword()));

		
		return "redirect:/";
	
	}
	
	private void autoLogin(String email, String rawPassword) {
	    // UsernamePasswordAuthenticationToken を作成
	    UsernamePasswordAuthenticationToken authToken =
	        new UsernamePasswordAuthenticationToken(email, rawPassword);

	    // AuthenticationManager に渡して認証
	    Authentication authentication = authenticationManager.authenticate(authToken);

	    // 認証済みとしてセキュリティコンテキストにセット
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}