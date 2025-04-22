package com.example.springcart.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class UserShopId implements Serializable {
    private Integer userId;
    private Integer shopId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserShopId)) return false;
        UserShopId that = (UserShopId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(shopId, that.shopId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, shopId);
    }

    public Integer getUserId() {
    	return this.userId;
    }
    
    public Integer getShopId() {
    	return this.shopId;
    }

    public void setUserId(Integer userId) {
    	this.userId = userId;
    }
    
    public void setShopId(Integer userId) {
    	this.shopId = shopId;
    }
}
