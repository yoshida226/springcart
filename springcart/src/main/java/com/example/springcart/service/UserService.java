package com.example.springcart.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springcart.entity.User;
import com.example.springcart.form.UserRegisterForm;
import com.example.springcart.repository.RoleRepository;
import com.example.springcart.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	//メール重複チェック
	public Boolean isEmailAlreadyExists(UserRegisterForm userRegisterForm) {
		
		Optional<User> user = userRepository.findByEmail(userRegisterForm.getEmail());
		
		return !user.isEmpty();
	}
	
	//パスワード一致チェック
	public Boolean isNotSamePassword(UserRegisterForm userRegisterForm) {		
		return !(userRegisterForm.getPassword().equals(userRegisterForm.getPasswordConfirmation()));
	}
	
	//パスワードをハッシュ化したうえでinsert
	@Transactional
	public void registerUser(UserRegisterForm userRegisterForm) {
		User user = new User();
		user.setName(userRegisterForm.getName());
		user.setEmail(userRegisterForm.getEmail());
		user.setPassword(passwordEncoder.encode(userRegisterForm.getPassword()));
		user.setEnabled(false);
		user.setRole(roleRepository.findById(1).orElseThrow());
		
		userRepository.save(user);
	}
	
}
