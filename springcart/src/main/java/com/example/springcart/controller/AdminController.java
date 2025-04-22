package com.example.springcart.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springcart.entity.Product;
import com.example.springcart.entity.Shop;
import com.example.springcart.entity.User;
import com.example.springcart.form.ProductUpdateForm;
import com.example.springcart.repository.ProductRepository;
import com.example.springcart.repository.ShopRepository;
import com.example.springcart.repository.UserShopRepository;
import com.example.springcart.service.AdminService;
import com.example.springcart.util.AuthUtil;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AuthUtil authUtil;
	
	@Autowired
	private UserShopRepository userShopRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private AdminService adminService;
	
	@GetMapping("/list")
	public String showProductList(@RequestParam(required = false) Integer shopId,
								  Authentication auth,
								  Model model) {
		
		System.out.println("test1");
		User user = authUtil.getUserByAuth(auth);
		System.out.println("test2");
		List<Shop> shops = userShopRepository.findShopsByUserId(user.getId());
		
		System.out.println("test3");
		
		List<Product> products = new ArrayList<>();
		if(shopId != null) {
			//ショップが選択されたらそのショップの商品を表示
			products.addAll(productRepository.findByShop(shopRepository.findById(shopId).orElseThrow()));
		} else {
			//初期表示時は先頭のショップの商品を表示
			products.addAll(productRepository.findByShop(shops.get(0)));
		}
		
		List<ProductUpdateForm> productUpdateForms = products.stream()
	        .map(product -> {
	            ProductUpdateForm form = new ProductUpdateForm(product.getId(),
										            		   product.getName(),
										            		   product.getCategory(),
										            		   product.getDescription(),
										            		   product.getPrice(),
										            		   product.getStock(),
										            		   product.getImage(),
										            		   product.getUpdatedDate());
	            return form;
	        })
	        .collect(Collectors.toList());
		
		model.addAttribute("shops", shops);
		model.addAttribute("selectedShopId", (shopId == null ? shops.get(0).getId() : shopId));
		model.addAttribute("productUpdateForms", productUpdateForms);
		
		return "admin/list";
	}
	
	@PostMapping("/list/update")
	public String updateProduct(@ModelAttribute @Validated ProductUpdateForm form,
								BindingResult result,
								@RequestParam(required = false) Long shopId,
								RedirectAttributes redirectAttributes,
								Model model) {
		
		//バリデーションエラーがあった場合
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("errorMessage", "入力内容に誤りがあります。確認してください。");
			
			return "redirect:/admin/list" + (shopId != null ? ("?shopId=" + shopId) : "");
		}
		
		try {
            // 画像ファイルが選択されている場合は保存
            if (form.getImageContent() != null && !form.getImageContent().isEmpty()) {
                String fileName = adminService.saveImage(form.getImageContent());
                form.setImage(fileName);
            }
            
            // 商品情報を更新
            adminService.update(form);
            
            redirectAttributes.addFlashAttribute("successMessage", "商品情報を更新しました");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "商品情報の更新に失敗しました: " + e.getMessage());
        }

		return "redirect:/admin/list" + (shopId != null ? ("?shopId=" + shopId) : "");
	}
	
	
}
