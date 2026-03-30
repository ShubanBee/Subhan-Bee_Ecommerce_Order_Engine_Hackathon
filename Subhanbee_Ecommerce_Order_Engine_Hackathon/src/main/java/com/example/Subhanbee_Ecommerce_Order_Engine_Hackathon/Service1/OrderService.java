package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.Service1;

import com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.modal1.*;
import com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.Repository.OrderRepository;
import com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.util.IdGenerator;
import com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.exception.OrderNotFoundException;
import java.util.*;

public class OrderService {
    private OrderRepository orderRepository;
    private ProductService productService;
    private CartService cartService;
    private DiscountService discountService;
    private Set<String> processingOrders;
    
    public OrderService(ProductService productService, CartService cartService) {
        this.orderRepository = new OrderRepository();
        this.productService = productService;
        this.cartService = cartService;
        this.discountService = new DiscountService();
        this.processingOrders = Collections.synchronizedSet(new HashSet<>());
    }
    
    public Order createOrder(String userId) throws Exception {
        // Idempotency check
        if (processingOrders.contains(userId)) {
            throw new Exception("Order already being processed for user");
        }
        
        processingOrders.add(userId);
        
        try {
            Cart cart = cartService.getCart(userId);
            if (cart.isEmpty()) {
                throw new Exception("Cart is empty");
            }
            
            // Validate stock
            for (CartItem item : cart.getItems().values()) {
                if (!productService.hasStock(item.getProduct().getId(), item.getQuantity())) {
                    throw new Exception("Insufficient stock for product: " + item.getProduct().getName());
                }
            }
            
            // Calculate total with discount
            double subtotal = cartService.calculateCartTotal(userId);
            double discount = discountService.calculateDiscount(subtotal, cart);
            double total = subtotal - discount;
            
            // Create order
            String orderId = IdGenerator.generateOrderId();
            Order order = new Order(orderId, userId);
            order.setTotal(total);
            order.setAppliedCoupon(cart.getAppliedCoupon());
            
            // Add items to order
            for (CartItem cartItem : cart.getItems().values()) {
                OrderItem orderItem = new OrderItem(
                    cartItem.getProduct().getId(),
                    cartItem.getProduct().getName(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getPrice()
                );
                order.addItem(orderItem);
            }
            
            orderRepository.save(order);
            
            return order;
        } finally {
            processingOrders.remove(userId);
        }
    }
    
    public void confirmOrder(String orderId) {
        Order order = orderRepository.findById(orderId);
        if (order != null && order.getStatus() == OrderStatus.PAID) {
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.update(order);
            
            // Clear cart after successful order
            cartService.clearCart(order.getUserId());
        }
    }
    
    public void failOrder(String orderId) {
        Order order = orderRepository.findById(orderId);
        if (order != null && order.getStatus() == OrderStatus.PAYMENT_FAILED) {
            // Restore stock
            for (OrderItem item : order.getItems()) {
                productService.releaseStock(item.getProductId(), item.getQuantity());
            }
            orderRepository.update(order);
        }
    }
    
    public void cancelOrder(String orderId) throws Exception {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new OrderNotFoundException("Order not found: " + orderId);
        }
        
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new Exception("Order is already cancelled");
        }
        
        if (order.isCancellable()) {
            // Restore stock
            for (OrderItem item : order.getItems()) {
                productService.releaseStock(item.getProductId(), item.getQuantity());
            }
            
            order.setStatus(OrderStatus.CANCELLED);
            order.setCancellationDate(new Date());
            orderRepository.update(order);
        } else {
            throw new Exception("Order cannot be cancelled in " + order.getStatus() + " status");
        }
    }
    
    public void returnItems(String orderId, String productId, int quantity) throws Exception {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new OrderNotFoundException("Order not found: " + orderId);
        }
        
        if (!order.isReturnable()) {
            throw new Exception("Order cannot be returned in " + order.getStatus() + " status");
        }
        
        // Find the order item
        OrderItem targetItem = null;
        for (OrderItem item : order.getItems()) {
            if (item.getProductId().equals(productId)) {
                targetItem = item;
                break;
            }
        }
        
        if (targetItem == null) {
            throw new Exception("Product not found in order");
        }
        
        if (quantity > targetItem.getQuantity()) {
            throw new Exception("Return quantity exceeds purchased quantity");
        }
        
        // Restore stock
        productService.releaseStock(productId, quantity);
        
        // Update order item quantity
        int newQuantity = targetItem.getQuantity() - quantity;
        if (newQuantity == 0) {
            order.getItems().remove(targetItem);
        } else {
            targetItem.setQuantity(newQuantity);
        }
        
        // Update order total
        double refundAmount = targetItem.getPrice() * quantity;
        order.setTotal(order.getTotal() - refundAmount);
        
        // If all items returned, mark order as returned
        if (order.getItems().isEmpty()) {
            order.setStatus(OrderStatus.RETURNED);
        }
        
        orderRepository.update(order);
    }
    
    public List<Order> getUserOrders(String userId) {
        return orderRepository.findByUserId(userId);
    }
    
    public Order getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    public boolean hasPendingOrder(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
            .anyMatch(order -> order.getStatus() == OrderStatus.PENDING || 
                              order.getStatus() == OrderStatus.PAID);
    }
    
    public void updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId);
        if (order != null) {
            order.setStatus(status);
            orderRepository.update(order);
        }
    }
}