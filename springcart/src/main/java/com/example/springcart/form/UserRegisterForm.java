package com.example.springcart.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UserRegisterForm {
	
	@NotEmpty(message = "名前は必須です")
	private String name;
	
	@NotEmpty(message = "メールアドレスは必須です")
	@Email(message = "メールアドレスの形式で入力してください")
	private String email;
	
	@NotEmpty(message = "パスワードは必須です")
	private String password;
	
	@NotEmpty(message = "確認用パスワードを入力してください")
	private String passwordConfirmation;
}