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
	
	private MultipartFile imageContent;//画像の内容
	
	private String base64Image;//画面表示用base64画像
	
	private String image;// 表示用：今登録されてる画像のパス
}
