package com.example.springcart.event;

import com.example.springcart.entity.User;

import lombok.Getter;

@Getter
public class SignUpEvent {
	
	private String domain;
	private User user;
	
	public SignUpEvent(User user, String domain) {
		this.user = user;
		this.domain = domain;
	}

}
