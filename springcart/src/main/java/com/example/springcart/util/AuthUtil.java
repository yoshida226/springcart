package com.example.springcart.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.springcart.entity.User;
import com.example.springcart.repository.UserRepository;

@Component
public class AuthUtil {
	
	@Autowired
	private UserRepository userRepository;
	
	//ログイン判定
	public boolean isLoggedIn(Authentication auth) {
		if (!(auth == null || auth instanceof AnonymousAuthenticationToken)) {
			return true;
		}
		return false;
	}
	
	//ログイン者取得
	public User getUserByAuth(Authentication auth) {
		return userRepository.findByEmail(auth.getName()).orElseThrow();
	}
}