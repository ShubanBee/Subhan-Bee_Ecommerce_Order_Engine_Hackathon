package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.exception;


public class PaymentFailedException extends Exception {
    public PaymentFailedException(String message) {
        super(message);
    }
    
    public PaymentFailedException(double amount) {
        super("Payment failed for amount: ₹" + amount);
    }
}
