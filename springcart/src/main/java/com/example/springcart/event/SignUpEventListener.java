package com.example.springcart.event;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.springcart.entity.User;
import com.example.springcart.entity.VerificationToken;
import com.example.springcart.repository.VerificationTokenRepository;

@Component
public class SignUpEventListener {
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	
	@EventListener
	public void publishSignUpEmail (SignUpEvent e) {
		User user = e.getUser();
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setUser(user);
		verificationToken.setToken(token);
		verificationTokenRepository.save(verificationToken);
		
		String subject = "アカウント確認メール";
        String confirmationUrl = e.getDomain() + "/auth/verify?token=" + token;
        String message = "アカウントを有効化するには、以下のリンクをクリックしてください：\n" + confirmationUrl;
        
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
	}
}
