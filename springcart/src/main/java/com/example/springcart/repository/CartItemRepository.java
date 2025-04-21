package com.example.springcart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springcart.entity.CartItem;
import com.example.springcart.entity.Product;
import com.example.springcart.entity.User;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
	public List<CartItem> findByUserId(User user);
	public CartItem findByProductId(Product product);
}
