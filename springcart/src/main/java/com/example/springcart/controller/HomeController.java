package com.example.springcart.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.springcart.entity.Product;
import com.example.springcart.repository.ProductRepository;

@Controller
public class HomeController {
	
	private final ProductRepository productRepository;
	
	public HomeController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@GetMapping("/")
	public String showIndex(Model model) {
		
		Category c1 = new Category("日用品・雑貨", productRepository.findTop4ByCategoryOrderByCreatedDateDesc("日用品・雑貨"));
		Category c2 = new Category("食品", productRepository.findTop4ByCategoryOrderByCreatedDateDesc("食品"));
		Category c3 = new Category("アウトドア", productRepository.findTop4ByCategoryOrderByCreatedDateDesc("アウトドア"));
		Category c4 = new Category("ファッション", productRepository.findTop4ByCategoryOrderByCreatedDateDesc("ファッション"));
		
		List<Category> categories = List.of(c1, c2, c3, c4);
		
		model.addAttribute("categories", categories);
		
		return "index";
	}
	
	//インナークラス
	public  class Category {
		private String name;
		private List<Product> products;
		
		public Category(String name, List<Product> products) {
			this.name = name;
			this.products = products;
		}
		
		public String getName() {
			return name;
		}
		
		public List<Product> getProducts() {
			return products;
		}
	}
}