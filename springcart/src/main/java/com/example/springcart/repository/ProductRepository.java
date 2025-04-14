package com.example.springcart.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springcart.Entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	public Page<Product> findByNameLike(String name, Pageable pageable);
	public Page<Product> findByCategory(String name, Pageable pageable);
	
}
