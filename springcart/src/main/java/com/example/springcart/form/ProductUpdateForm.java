package com.example.springcart.form;

import java.sql.Timestamp;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProductUpdateForm {
	
	private Integer id;
	
	@NotBlank
	@Size(min = 2, max = 255, message = "名前は2文字以上255文字以下の値を設定してください")
	private String name;
	
	private String category;

	@Size(min = 2, max = 255, message = "説明は2文字以上1000文字以下の値を設定してください")
	private String description;

	@Min(value = 1, message = "金額は1以上の値を設定してください")
	private Integer price;

	private Integer stock;
	
	private MultipartFile imageContent;//フォームからアップロードされる画像
	
	private String image;// 表示用：今登録されてる画像のパス
	
	private Timestamp updatedDate;
	
	public ProductUpdateForm(Integer id, String name, String category, String description, Integer price, Integer stock, String image, Timestamp updatedDate) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.description = description;
		this.price = price;
		this.stock = stock;
		this.image = image;
		this.updatedDate = updatedDate;
	}
}
