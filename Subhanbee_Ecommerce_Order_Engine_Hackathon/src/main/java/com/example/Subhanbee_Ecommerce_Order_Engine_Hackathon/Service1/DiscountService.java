package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.Service1;



import com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.modal1.*;

public class DiscountService {
    
    public double calculateDiscount(double total, Cart cart) {
        double discount = 0;
        
        // Rule 1: Total > ₹1000 → 10% discount
        if (total > 1000) {
            discount += total * 0.10;
        }
        
        // Rule 2: Quantity > 3 (same product) → extra 5%
        for (CartItem item : cart.getItems().values()) {
            if (item.getQuantity() > 3) {
                double itemTotal = item.getProduct().getPrice() * item.getQuantity();
                discount += itemTotal * 0.05;
            }
        }
        
        // Apply coupon discount
        if (cart.getAppliedCoupon() != null) {
            discount += calculateCouponDiscount(total, cart.getAppliedCoupon());
        }
        
        return discount;
    }
    
    public void applyCoupon(Cart cart, String couponCode) {
        // Validate coupon
        if (isValidCoupon(couponCode)) {
            cart.setAppliedCoupon(couponCode);
        } else {
            System.out.println("Invalid coupon code!");
        }
    }
    
    private double calculateCouponDiscount(double total, String couponCode) {
        switch (couponCode.toUpperCase()) {
            case "SAVE10":
                return total * 0.10;
            case "FLAT200":
                return Math.min(200, total);
            default:
                return 0;
        }
    }
    
    private boolean isValidCoupon(String couponCode) {
        return couponCode != null && 
               (couponCode.equalsIgnoreCase("SAVE10") || 
                couponCode.equalsIgnoreCase("FLAT200"));
    }
    
    public double getMaxDiscount(double total, Cart cart) {
        double volumeDiscount = calculateVolumeDiscount(cart);
        double couponDiscount = 0;
        
        if (cart.getAppliedCoupon() != null) {
            couponDiscount = calculateCouponDiscount(total, cart.getAppliedCoupon());
        }
        
        return Math.max(volumeDiscount, couponDiscount);
    }
    
    private double calculateVolumeDiscount(Cart cart) {
        double discount = 0;
        
        // Volume discounts
        for (CartItem item : cart.getItems().values()) {
            int qty = item.getQuantity();
            double itemTotal = item.getProduct().getPrice() * qty;
            
            if (qty >= 10) {
                discount += itemTotal * 0.15; // 15% off for bulk purchase
            } else if (qty >= 5) {
                discount += itemTotal * 0.10; // 10% off for medium quantity
            } else if (qty >= 3) {
                discount += itemTotal * 0.05; // 5% off for small quantity
            }
        }
        
        return discount;
    }
}