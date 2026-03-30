package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.exception;



public class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) {
        super(message);
    }
    
    public InsufficientStockException(String productId, int requested, int available) {
        super(String.format("Insufficient stock for product %s. Requested: %d, Available: %d", 
            productId, requested, available));
    }
}