package com.example.springcart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.springcart.form.UserResisterForm;

@Controller
@RequestMapping("/user")
public class UserController {

	@GetMapping("/register")
	public String showResisterForm(Model model) {
		
		model.addAttribute("userRegisterForm", new UserResisterForm());
		
		return "user/register";
	}
	
	@PostMapping("/register")
	public String resisterUser(@ModelAttribute @Validated UserResisterForm userResisterForm,
							   Model model) {
		
		model.addAttribute("userRegisterForm", new UserResisterForm());
		
		return "/";
	
	}
}