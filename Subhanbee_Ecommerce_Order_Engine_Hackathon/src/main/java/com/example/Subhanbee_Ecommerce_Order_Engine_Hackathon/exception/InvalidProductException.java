package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.exception;





public class InvalidProductException extends Exception {
    
    // Constructor for custom message
    public InvalidProductException(String message) {
        super(message);
    }
    
    // Static factory method for product ID
    public static InvalidProductException forProductId(String productId) {
        return new InvalidProductException("Product not found: " + productId);
    }
}