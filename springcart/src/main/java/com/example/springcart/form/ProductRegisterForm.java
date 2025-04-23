package com.example.springcart.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.example.springcart.entity.Shop;

import lombok.Data;

@Data
public class ProductRegisterForm {
	
	@Size(min = 2, max = 255, message = "名前は2文字以上255文字以下の値を入力してください")
	private String name;

	@NotBlank
	private String category;
	
	@NotNull
	private Shop shop;
	
	@Size(min = 2, max = 255, message = "説明は2文字以上1000文字以下の値を入力してください")
	private String description;

	@Min(value = 1, message = "金額は1以上の値を設定してください")
	private Integer price;

	@Min(value = 0, message = "在庫は0以上の値を設定してください")
	private Integer stock;
	
	private MultipartFile imageContent;//フォームからアップロードされる画像
	
	private String image;// 表示用：今登録されてる画像のパス

}
