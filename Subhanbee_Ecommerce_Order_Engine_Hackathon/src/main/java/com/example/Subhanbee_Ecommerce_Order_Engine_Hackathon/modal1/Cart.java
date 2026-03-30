package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.modal1;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Cart {
    private String userId;
    private Map<String, CartItem> items;
    private String appliedCoupon;
    
    public Cart(String userId) {
        this.userId = userId;
        this.items = new ConcurrentHashMap<>();
        this.appliedCoupon = null;
    }
    
    public String getUserId() { return userId; }
    public Map<String, CartItem> getItems() { return items; }
    public String getAppliedCoupon() { return appliedCoupon; }
    public void setAppliedCoupon(String coupon) { this.appliedCoupon = coupon; }
    
    public boolean addItem(Product product, int quantity) {
        if (!product.hasStock(quantity)) {
            return false;
        }
        
        CartItem item = items.get(product.getId());
        if (item == null) {
            items.put(product.getId(), new CartItem(product, quantity));
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }
        return true;
    }
    
    public boolean removeItem(String productId) {
        CartItem removed = items.remove(productId);
        return removed != null;
    }
    
    public void updateQuantity(String productId, int quantity) {
        CartItem item = items.get(productId);
        if (item != null) {
            item.setQuantity(quantity);
            if (quantity <= 0) {
                items.remove(productId);
            }
        }
    }
    
    public void clear() {
        items.clear();
        appliedCoupon = null;
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
}