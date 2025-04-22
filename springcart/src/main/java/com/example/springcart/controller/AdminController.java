package com.example.springcart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@GetMapping("/list")
	public String showProductList(@RequestParam(required = false) Integer shopId) {
		
		
		return "redirect:admin/list";
	}
}
