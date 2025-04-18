package com.example.springcart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springcart.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	public Page<Product> findByNameLike(String name, Pageable pageable);
	public Page<Product> findByCategory(String category, Pageable pageable);
	public Optional<Product> findById(Integer product);
	public List<Product> findTop4ByCategoryOrderByCreatedDateDesc(String category);
	
}
