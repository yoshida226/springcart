package com.example.springcart.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.example.springcart.entity.User;
import com.example.springcart.event.SignUpEventPublisher;
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
	private SignUpEventPublisher signUpEventPublisher;
	
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
							   HttpServletRequest request,
							   Model model) {
		
		//基本的な入力チェック
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterForm", result);
			redirectAttributes.addFlashAttribute("userRegisterForm", userRegisterForm);
			
			return "/user/register";
		}
		
		//Email重複チェック
		if(userService.isEmailAlreadyExists(userRegisterForm)) {
			redirectAttributes.addFlashAttribute("errorMessage", "すでに登録されているEmailです");
			return "redirect:/user/register";
		}
		
		//パスワード一致チェック
		if(userService.isNotSamePassword(userRegisterForm)) {
			redirectAttributes.addFlashAttribute("errorMessage", "パスワードが一致しません");
			return "redirect:/user/register";
		}
		
		userService.registerUser(userRegisterForm);
		
		
		//メール認証
		User user = userRepository.findByEmail(userRegisterForm.getEmail()).orElseThrow();
		
		String domain = UriComponentsBuilder.newInstance()
            .scheme(request.getScheme())
            .host(request.getServerName())
            .port(request.getServerPort())
            .build()
            .toUriString();
		
		signUpEventPublisher.publishSignUpEvent(user, domain);
		
		model.addAttribute("notice", "認証メールを送信しました。メールを確認してください");
		
		return "auth/verify";
	
	}

}