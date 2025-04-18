package com.example.springcart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springcart.entity.Cart;
import com.example.springcart.entity.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
	public List<Cart> findByUserId(User user);
}
