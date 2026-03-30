package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.Repository;


import com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.modal1.Order;
import com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.modal1.OrderStatus;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class OrderRepository {
    private Map<String, Order> orders;
    
    public OrderRepository() {
        this.orders = new ConcurrentHashMap<>();
    }
    
    public void save(Order order) {
        orders.put(order.getId(), order);
    }
    
    public void update(Order order) {
        orders.put(order.getId(), order);
    }
    
    public Order findById(String id) {
        return orders.get(id);
    }
    
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }
    
    public List<Order> findByUserId(String userId) {
        return orders.values().stream()
            .filter(order -> order.getUserId().equals(userId))
            .collect(Collectors.toList());
    }
    
    public List<Order> findByStatus(OrderStatus status) {
        return orders.values().stream()
            .filter(order -> order.getStatus() == status)
            .collect(Collectors.toList());
    }
    
    public void delete(String id) {
        orders.remove(id);
    }
    
    public boolean exists(String id) {
        return orders.containsKey(id);
    }
    
    public int size() {
        return orders.size();
    }
}
