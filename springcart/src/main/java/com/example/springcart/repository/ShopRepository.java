package com.example.springcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springcart.entity.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Integer> {

}
