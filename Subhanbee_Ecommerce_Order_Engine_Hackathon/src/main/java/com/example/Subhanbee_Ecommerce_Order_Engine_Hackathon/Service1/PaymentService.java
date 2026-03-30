package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.Service1;


import java.util.Random;

public class PaymentService {
    private Random random;
    private double successRate;
    
    public PaymentService() {
        this.random = new Random();
        this.successRate = 0.7; // 70% success rate
    }
    
    public PaymentService(double successRate) {
        this.random = new Random();
        this.successRate = successRate;
    }
    
    public boolean processPayment(double amount) {
        // Simulate payment processing delay
        try {
            Thread.sleep(1000); // 1 second processing time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Random success/failure based on success rate
        boolean success = random.nextDouble() < successRate;
        
        if (success) {
            System.out.println("Payment of ₹" + amount + " processed successfully!");
        } else {
            System.out.println("Payment of ₹" + amount + " failed!");
        }
        
        return success;
    }
    
    public void setSuccessRate(double rate) {
        this.successRate = rate;
    }
    
    public double getSuccessRate() {
        return successRate;
    }
}
