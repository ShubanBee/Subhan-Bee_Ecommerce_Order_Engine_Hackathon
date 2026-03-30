package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.modal1;


public enum OrderStatus {
    PENDING("Pending"),
    PAID("Paid"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    PAYMENT_FAILED("Payment Failed"),
    RETURNED("Returned");
    
    private final String displayName;
    
    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
