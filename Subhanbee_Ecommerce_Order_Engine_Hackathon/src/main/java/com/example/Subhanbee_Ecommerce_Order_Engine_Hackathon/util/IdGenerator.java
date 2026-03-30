package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.util;



import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    private static AtomicLong orderCounter = new AtomicLong(1000);
    private static AtomicLong productCounter = new AtomicLong(1000);
    
    public static String generateOrderId() {
        return "ORD-" + orderCounter.incrementAndGet();
    }
    
    public static String generateProductId() {
        return "PRD-" + productCounter.incrementAndGet();
    }
    
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
    
    public static String generateTransactionId() {
        return "TXN-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}