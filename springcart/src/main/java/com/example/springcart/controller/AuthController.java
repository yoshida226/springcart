package com.example.springcart.controller;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.springcart.entity.User;
import com.example.springcart.entity.VerificationToken;
import com.example.springcart.repository.UserRepository;
import com.example.springcart.repository.VerificationTokenRepository;
import com.example.springcart.service.AuthService;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/login")
	public String login() {
		return "auth/login";
	}
	
	@GetMapping("/verify")
	public String verifyUser(@RequestParam("token") String token,
							 HttpServletRequest request,
							 Model model) {
		
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
		Optional<User> user = userRepository.findById(verificationToken.getUser().getId());
		
		String result = authService.validateVerificationToken(token);
		
		if(result.equals("確認完了")) {
			model.addAttribute("success", "ユーザが登録されました");
			
			// 登録後に自動ログイン
			authService.autoLogin(user.get(), request);
			
		} else {
			model.addAttribute("error", "エラー：" + result);
		}

		return "auth/verify";
	}
	
}
