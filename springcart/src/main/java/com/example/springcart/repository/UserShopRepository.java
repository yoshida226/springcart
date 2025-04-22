package com.example.springcart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.springcart.entity.Shop;
import com.example.springcart.entity.UserShop;
import com.example.springcart.entity.UserShopId;

@Repository
public interface UserShopRepository extends JpaRepository<UserShop, UserShopId> {
	@Query("SELECT us.shopId FROM UserShop us WHERE us.userId.id = :userId")
	public List<Shop> findShopsByUserId(@Param("userId") Integer userId);
}
