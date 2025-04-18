package com.example.springcart.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.springcart.entity.User;

@Component
public class SignUpEventPublisher {
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	public void publishSignUpEvent(User user, String domain) {
		
		eventPublisher.publishEvent(new SignUpEvent(user, domain));
	}
	
}
