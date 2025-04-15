package com.example.springcart.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.springcart.Entity.Product;
import com.example.springcart.form.AddToCartForm;
import com.example.springcart.repository.ProductRepository;
import com.example.springcart.service.AddToCartService;

@Controller
@RequestMapping("/product")
public class ProductController {
	
	private ProductRepository productRepositoty;
	private AddToCartService addToCartService;
	
	public ProductController(ProductRepository productRepositoty, AddToCartService addToCartService) {
		this.productRepositoty = productRepositoty;
		this.addToCartService = addToCartService;
	}

	@GetMapping("/list")
	public String displayList(
		@RequestParam(required = false) String keyword,
		@RequestParam(required = false) String category,
		@PageableDefault(page = 0, size = 6, sort = "id", direction = Direction.ASC) Pageable pageable,
		Model model
	) {
		Page<Product> productPage;
		
		if (keyword != null && !keyword.isEmpty()) {
			productPage = productRepositoty.findByNameLike("%" + keyword + "%", pageable);
	    } else if (category != null && !category.isEmpty()) {
	    	productPage = productRepositoty.findByCategory(category, pageable);
	    } else {
	    	productPage = productRepositoty.findAll(pageable); // 全件
	    }
		
		// ページネーション用の範囲計算
        int maxDisplayPages = 5; // 常に5つ表示
        int startPage = Math.max(0, productPage.getNumber() - 2); // 現在ページを中央に配置するための計算
        int endPage = Math.min(productPage.getTotalPages() - 1, startPage + maxDisplayPages - 1);

        // 開始ページが固定範囲内に収まるよう調整
        if (endPage - startPage < maxDisplayPages - 1) {
            startPage = Math.max(0, endPage - maxDisplayPages + 1);
        }

        // 表示するページ番号のリストを作成
        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(i);
        }
		
		model.addAttribute("products", productPage);
		model.addAttribute("pageNumbers", pageNumbers);
		model.addAttribute("currentPage", productPage.getNumber());
		model.addAttribute("totalPages", productPage.getTotalPages());
		model.addAttribute("keyword", keyword);
		model.addAttribute("category", category);
		
		return "product/list";
	}

	@GetMapping("/{id}")
	public String displaytDetail(
		@PathVariable Integer id,
		Model model
	) {
		
		Optional<Product> productOpt = productRepositoty.findById(id);
        Product product = productOpt.get();
        Integer stock = product.getStock();
        if(stock > 5) {
        	stock = 5;
        }
        
        AddToCartForm addToCartForm = new AddToCartForm(product.getId(), 0, product.getPrice());
        
        model.addAttribute("product", product);
        model.addAttribute("stock", stock);
        model.addAttribute("addToCartForm", addToCartForm);
		return "product/detail";
	}
	
	@GetMapping("/addToCart")
	public String addToCart(@ModelAttribute @Validated AddToCartForm addToCartForm, Model model) {
		Integer calculatedResult = addToCartService.calculatePrice(addToCartForm.getPrice(), addToCartForm.getAmount());
		
		return "order/cart";
	}
}
