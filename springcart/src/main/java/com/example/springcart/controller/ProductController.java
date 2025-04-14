package com.example.springcart.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.springcart.Entity.Product;
import com.example.springcart.repository.ProductRepository;

@Controller
@RequestMapping("/product")
public class ProductController {
	
	private ProductRepository productRepositoty;
	
	public ProductController(ProductRepository productRepositoty) {
		this.productRepositoty = productRepositoty; 
	}

	@GetMapping("/list")
	public String displayList(
		@RequestParam(required = false) String keyword,
		@RequestParam(required = false) String category,
		@PageableDefault(page = 0, size = 8, sort = "id", direction = Direction.ASC) Pageable pageable,
		Model model
	) {
		Page<Product> products;
		
		if (keyword != null && !keyword.isEmpty()) {
	        products = productRepositoty.findByNameLike("%" + keyword + "%", pageable);
	    } else if (category != null && !category.isEmpty()) {
	        products = productRepositoty.findByCategory(category, pageable);
	    } else {
	        products = productRepositoty.findAll(pageable); // 全件
	    }
		
		model.addAttribute("products", products);
		
		return "product/list";
	}

	@GetMapping("/{id}")
	public String displaytDetail(
		@PathVariable Integer id,
		Model model
	) {
		
		Optional<Product> productOpt = productRepositoty.findById(id);
        Product product = productOpt.get();
        model.addAttribute("product", product);
		
		return "product/detail";
	}
}
