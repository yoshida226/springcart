package com.example.springcart.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springcart.entity.Product;
import com.example.springcart.entity.Shop;
import com.example.springcart.form.ProductConfirmForm;
import com.example.springcart.form.ProductRegisterForm;
import com.example.springcart.form.ProductUpdateForm;
import com.example.springcart.repository.ProductRepository;
import com.example.springcart.repository.ShopRepository;
import com.example.springcart.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private AdminService adminService;
	
	//管理画面の商品一覧を表示
	@GetMapping("/list")
	public String showProductList(@RequestParam(required = false) Integer shopId,
								  Authentication auth,
								  Model model) {

		List<Shop> shops = adminService.getShopsByUser(auth);
		
		List<Product> products = new ArrayList<>();
		if(shopId != null) {
			//ショップが選択されたらそのショップの商品を表示
			products.addAll(productRepository.findByShopOrderByUpdatedDateDesc(shopRepository.findById(shopId).orElseThrow()));
		} else {
			//初期表示時は先頭のショップの商品を表示
			products.addAll(productRepository.findByShopOrderByUpdatedDateDesc(shops.get(0)));
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
	
	@GetMapping("/register")
	public String register(Authentication auth,
						   Model model) {

		List<String> categories = Arrays.asList("食品", "スポーツ", "日用品・雑貨", "ファッション", "ビューティー", "本", "アウトドア", "楽器");
		List<Shop> shops = adminService.getShopsByUser(auth);

		model.addAttribute("productForm", new ProductRegisterForm());
		model.addAttribute("categories", categories);
		model.addAttribute("shops", shops);
		
		return "admin/register";
	}
	
	@PostMapping("/confirm")
	public String confirmProduct(@ModelAttribute("productForm") @Validated ProductRegisterForm productRegisterForm,
								BindingResult result,
								Authentication auth,
								Model model) throws IOException {

		//バリデーションエラー発生時の処理
		if(result.hasErrors()) {
			System.out.println(result);
			List<String> categories = Arrays.asList("食品", "スポーツ", "日用品・雑貨", "ファッション", "ビューティー", "本", "アウトドア", "楽器");
			List<Shop> shops = adminService.getShopsByUser(auth);

			model.addAttribute("categories", categories);
			model.addAttribute("shops", shops);
			
			return "admin/register";
		}
		
		//画像をベース64に変換
		MultipartFile image =  productRegisterForm.getImageContent();
		String base64Image = "";
		if (image != null && !image.isEmpty()) {
	        byte[] fileContent = image.getBytes();
	        base64Image = Base64.getEncoder().encodeToString(fileContent);
	    }
		
		ProductConfirmForm productConfirmForm = new ProductConfirmForm(productRegisterForm.getName(),
																	   productRegisterForm.getCategory(),
																	   productRegisterForm.getShop(),
																	   productRegisterForm.getDescription(),
																	   productRegisterForm.getPrice(),
																	   productRegisterForm.getStock(),
																	   productRegisterForm.getImageContent(),
																	   base64Image,
																	   productRegisterForm.getImageContent().getOriginalFilename());
		model.addAttribute("productConfirmForm", productConfirmForm);
		
		return "admin/confirm";
	}
	
	@PostMapping("/complete")
	public String saveProduct(@ModelAttribute("productConfirmForm") ProductConfirmForm form,
							  RedirectAttributes redirectAttributes) {
		
		try {
            // 画像ファイルが選択されている場合は保存
			String base64Image = form.getBase64Image();
            if (base64Image != null && !base64Image.isEmpty()) {
            	// Content-Typeを取得（Base64文字列からMIMEタイプを抽出）
                String contentType = "image/jpeg"; // デフォルト値
                if (base64Image.startsWith("data:") && base64Image.contains(";base64,")) {
                    contentType = base64Image.substring(5, base64Image.indexOf(";base64,"));
                }
                
                // ファイル名を生成（実際の実装では適切なファイル名生成ロジックを使用）
                String fileName = "image." + getExtensionFromContentType(contentType);
                
                // Base64からMultipartFileに変換
                MultipartFile file = adminService.convertBase64ToMultipartFile(base64Image, fileName, contentType);
                
                //ファイルをストレージに保存
                String fineNameRandomUUID = adminService.saveImage(file);
                
                // フォームに保存した画像名をセット
                form.setImage(fineNameRandomUUID);
            }
            
            // 商品情報を更新
            adminService.insert(form);
            
            redirectAttributes.addFlashAttribute("successMessage", "商品を追加しました");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "商品の追加に失敗しました: " + e.getMessage());
        }
		
		return "redirect:/admin/list";
	}
	
	// Content-Typeから適切な拡張子を取得するヘルパーメソッド
	private String getExtensionFromContentType(String contentType) {
	    switch (contentType.toLowerCase()) {
	        case "image/jpeg":
	            return "jpg";
	        case "image/png":
	            return "png";
	        case "image/gif":
	            return "gif";
	        case "image/webp":
	            return "webp";
	        default:
	            return "jpg";
	    }
	}
}