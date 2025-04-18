package com.example.springcart.service;

import java.util.Calendar;
import java.util.Collections;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springcart.entity.User;
import com.example.springcart.entity.VerificationToken;
import com.example.springcart.repository.UserRepository;
import com.example.springcart.repository.VerificationTokenRepository;

@Service
public class AuthService {
	
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Transactional
	public String validateVerificationToken(String token) {
		
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
		
		if(verificationToken == null) {
			return "無効なトークンです";
		}
		
		if(verificationToken.getExpiryDate().getTime() < Calendar.getInstance().getTime().getTime()) {
			return "期限切れのトークンです";
		}
		

		User user = verificationToken.getUser();
		user.setEnabled(true);
		userRepository.save(user);
		
		verificationTokenRepository.delete(verificationToken);
		
		return "確認完了";
		
	}
	
	public void autoLogin(User user, HttpServletRequest request) {
	    UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
                user, null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_General"))
            );

        // セキュリティコンテキストに認証済みユーザーをセット
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // セッションにも反映（重要）
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                             SecurityContextHolder.getContext());
	}
}
