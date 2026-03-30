package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.exception;



public class OrderNotFoundException extends Exception {
    
    // Constructor with custom message
    public OrderNotFoundException(String message) {
        super(message);
    }
    
    // Constructor with order ID and cause
    public OrderNotFoundException(String orderId, Throwable cause) {
        super("Order not found: " + orderId, cause);
    }
}