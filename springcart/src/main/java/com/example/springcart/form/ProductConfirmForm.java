package com.example.springcart.form;

import org.springframework.web.multipart.MultipartFile;

import com.example.springcart.entity.Shop;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductConfirmForm {

	private String name;

	private String category;
	
	private Shop shop;
	
	private String description;

	private Integer price;

	private Integer stock;
	
	private MultipartFile imageContent;//フォームからアップロードされる画像
	
	private String image;// 表示用：今登録されてる画像のパス
}
