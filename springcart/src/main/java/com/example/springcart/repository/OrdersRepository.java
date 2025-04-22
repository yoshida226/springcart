package com.example.springcart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.springcart.entity.Orders;
import com.example.springcart.entity.User;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
	public List<Orders> findByUserId(User user);
	
	@Query("SELECT o FROM Orders o JOIN FETCH o.orderDetails WHERE o.userId.id = :userId ORDER BY o.orderedAt DESC")
	List<Orders> findByUserIdWithDetails(@Param("userId") Integer userId);
}
