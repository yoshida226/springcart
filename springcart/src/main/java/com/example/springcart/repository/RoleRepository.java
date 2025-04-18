package com.example.springcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springcart.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
