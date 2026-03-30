package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.Service1;



import com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.modal1.*;
import java.util.*;
import java.util.concurrent.*;

public class CartService {
    private Map<String, Cart> userCarts;
    private ProductService productService;
    private Map<String, Map<String, Long>> reservations; // productId -> userId -> expiryTime
    
    public CartService(ProductService productService) {
        this.userCarts = new ConcurrentHashMap<>();
        this.productService = productService;
        this.reservations = new ConcurrentHashMap<>();
        
        // Start reservation expiry checker
        startReservationExpiryChecker();
    }
    
    public Cart getCart(String userId) {
        return userCarts.computeIfAbsent(userId, k -> new Cart(k));
    }
    
    public boolean addToCart(String userId, String productId, int quantity) {
        Product product = productService.getProduct(productId);
        if (product == null) {
            return false;
        }
        
        // Check stock availability
        if (!productService.hasStock(productId, quantity)) {
            return false;
        }
        
        // Reserve stock
        if (!productService.reserveStock(productId, quantity)) {
            return false;
        }
        
        // Add to cart
        Cart cart = getCart(userId);
        cart.addItem(product, quantity);
        
        // Track reservation with expiry
        trackReservation(userId, productId, quantity);
        
        return true;
    }
    
    public boolean removeFromCart(String userId, String productId) {
        Cart cart = getCart(userId);
        CartItem item = cart.getItems().get(productId);
        
        if (item != null) {
            // Release reserved stock
            productService.releaseStock(productId, item.getQuantity());
            cart.removeItem(productId);
            
            // Remove reservation tracking
            removeReservation(userId, productId);
            return true;
        }
        return false;
    }
    
    public void clearCart(String userId) {
        Cart cart = getCart(userId);
        
        // Release all reserved stock
        for (CartItem item : cart.getItems().values()) {
            productService.releaseStock(item.getProduct().getId(), item.getQuantity());
            removeReservation(userId, item.getProduct().getId());
        }
        
        cart.clear();
    }
    
    public double calculateCartTotal(String userId) {
        Cart cart = getCart(userId);
        double total = 0;
        for (CartItem item : cart.getItems().values()) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }
    
    private void trackReservation(String userId, String productId, int quantity) {
        reservations.computeIfAbsent(productId, k -> new ConcurrentHashMap<>())
            .put(userId, System.currentTimeMillis() + 300000); // 5 minutes expiry
    }
    
    private void removeReservation(String userId, String productId) {
        Map<String, Long> productReservations = reservations.get(productId);
        if (productReservations != null) {
            productReservations.remove(userId);
            if (productReservations.isEmpty()) {
                reservations.remove(productId);
            }
        }
    }
    
    private void startReservationExpiryChecker() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkExpiredReservations();
            }
        }, 60000, 60000); // Check every minute
    }
    
    private void checkExpiredReservations() {
        long now = System.currentTimeMillis();
        List<String> expiredProducts = new ArrayList<>();
        
        for (Map.Entry<String, Map<String, Long>> entry : reservations.entrySet()) {
            String productId = entry.getKey();
            Map<String, Long> userReservations = entry.getValue();
            
            List<String> expiredUsers = new ArrayList<>();
            for (Map.Entry<String, Long> userEntry : userReservations.entrySet()) {
                if (userEntry.getValue() < now) {
                    expiredUsers.add(userEntry.getKey());
                }
            }
            
            for (String userId : expiredUsers) {
                // Release expired reservation
                Cart cart = getCart(userId);
                CartItem item = cart.getItems().get(productId);
                if (item != null) {
                    productService.releaseStock(productId, item.getQuantity());
                    cart.removeItem(productId);
                    userReservations.remove(userId);
                    System.out.println("Released expired reservation for user " + userId + ", product " + productId);
                }
            }
            
            if (userReservations.isEmpty()) {
                expiredProducts.add(productId);
            }
        }
        
        for (String productId : expiredProducts) {
            reservations.remove(productId);
        }
    }
    
    public void updateCartItemQuantity(String userId, String productId, int quantity) {
        Cart cart = getCart(userId);
        CartItem item = cart.getItems().get(productId);
        
        if (item != null) {
            int oldQuantity = item.getQuantity();
            int quantityDiff = quantity - oldQuantity;
            
            if (quantityDiff > 0) {
                // Need to reserve more stock
                if (productService.reserveStock(productId, quantityDiff)) {
                    item.setQuantity(quantity);
                }
            } else if (quantityDiff < 0) {
                // Release excess stock
                productService.releaseStock(productId, -quantityDiff);
                item.setQuantity(quantity);
            }
            
            if (quantity <= 0) {
                cart.removeItem(productId);
            }
        }
    }
}