package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.modal1;


import java.util.*;

public class Order {
    private String id;
    private String userId;
    private List<OrderItem> items;
    private OrderStatus status;
    private double total;
    private Date orderDate;
    private Date paymentDate;
    private Date cancellationDate;
    private String appliedCoupon;
    
    public Order(String id, String userId) {
        this.id = id;
        this.userId = userId;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.orderDate = new Date();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    
    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }
    
    public Date getCancellationDate() { return cancellationDate; }
    public void setCancellationDate(Date cancellationDate) { this.cancellationDate = cancellationDate; }
    
    public String getAppliedCoupon() { return appliedCoupon; }
    public void setAppliedCoupon(String appliedCoupon) { this.appliedCoupon = appliedCoupon; }
    
    public void addItem(OrderItem item) {
        items.add(item);
    }
    
    public boolean isCancellable() {
        return status == OrderStatus.PENDING || status == OrderStatus.PAID;
    }
    
    public boolean isReturnable() {
        return status == OrderStatus.COMPLETED;
    }
}
