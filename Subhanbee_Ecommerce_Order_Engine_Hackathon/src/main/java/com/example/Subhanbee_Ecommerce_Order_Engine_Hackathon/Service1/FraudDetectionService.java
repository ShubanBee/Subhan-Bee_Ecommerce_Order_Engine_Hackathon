package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.Service1;


import java.util.*;
import java.util.concurrent.*;

public class FraudDetectionService {
    private Map<String, List<Long>> userOrders;
    private Set<String> flaggedUsers;
    private long timeWindowMillis;
    private int maxOrdersInWindow;
    private double highValueThreshold;
    
    public FraudDetectionService() {
        this.userOrders = new ConcurrentHashMap<>();
        this.flaggedUsers = ConcurrentHashMap.newKeySet();
        this.timeWindowMillis = 60000; // 1 minute
        this.maxOrdersInWindow = 3; // 3 orders in 1 minute
        this.highValueThreshold = 50000; // ₹50,000
    }
    
    public boolean isSuspicious(String userId) {
        return flaggedUsers.contains(userId);
    }
    
    public void recordOrder(String userId, double orderAmount) {
        long now = System.currentTimeMillis();
        
        // Record order
        userOrders.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>())
            .add(now);
        
        // Check for fraud
        checkForFraud(userId, orderAmount);
        
        // Clean up old records
        cleanupOldRecords(userId);
    }
    
    private void checkForFraud(String userId, double orderAmount) {
        List<Long> orders = userOrders.get(userId);
        if (orders == null) return;
        
        long now = System.currentTimeMillis();
        long windowStart = now - timeWindowMillis;
        
        // Count orders in time window
        long orderCount = orders.stream()
            .filter(timestamp -> timestamp >= windowStart)
            .count();
        
        // Rule 1: 3 orders in 1 minute
        if (orderCount >= maxOrdersInWindow) {
            flaggedUsers.add(userId);
            System.out.println("FRAUD ALERT: User " + userId + " placed " + 
                orderCount + " orders in " + (timeWindowMillis/1000) + " seconds");
        }
        
        // Rule 2: High-value orders
        if (orderAmount > highValueThreshold) {
            flaggedUsers.add(userId);
            System.out.println("FRAUD ALERT: User " + userId + " placed high-value order: ₹" + orderAmount);
        }
    }
    
    private void cleanupOldRecords(String userId) {
        List<Long> orders = userOrders.get(userId);
        if (orders == null) return;
        
        long cutoff = System.currentTimeMillis() - timeWindowMillis;
        orders.removeIf(timestamp -> timestamp < cutoff);
        
        if (orders.isEmpty()) {
            userOrders.remove(userId);
            // Don't remove from flaggedUsers immediately, keep for investigation
        }
    }
    
    public void setTimeWindow(long millis) {
        this.timeWindowMillis = millis;
    }
    
    public void setMaxOrdersInWindow(int maxOrders) {
        this.maxOrdersInWindow = maxOrders;
    }
    
    public void setHighValueThreshold(double threshold) {
        this.highValueThreshold = threshold;
    }
    
    public Set<String> getFlaggedUsers() {
        return new HashSet<>(flaggedUsers);
    }
    
    public void clearFlaggedUser(String userId) {
        flaggedUsers.remove(userId);
        userOrders.remove(userId);
    }
}
