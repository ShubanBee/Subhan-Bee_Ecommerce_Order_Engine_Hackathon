package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon;


import  com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.Service1.*;
import  com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.modal1.*;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static ProductService productService;
    private static CartService cartService;
    private static OrderService orderService;
    private static PaymentService paymentService;
    private static DiscountService discountService;
    private static InventoryService inventoryService;
    private static EventService eventService;
    private static AuditService auditService;
    private static FraudDetectionService fraudService;
    private static FailureInjectionService failureService;
    
    private static String currentUser = "user1";
    
    public static void main(String[] args) {
        initializeServices();
        
        System.out.println("==========================================");
        System.out.println("  DISTRIBUTED E-COMMERCE ORDER ENGINE");
        System.out.println("  Hackathon Technical Assessment");
        System.out.println("==========================================");
        
        while (true) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            
            try {
                switch (choice) {
                    case 1:
                        addProduct();
                        break;
                    case 2:
                        viewProducts();
                        break;
                    case 3:
                        addToCart();
                        break;
                    case 4:
                        removeFromCart();
                        break;
                    case 5:
                        viewCart();
                        break;
                    case 6:
                        applyCoupon();
                        break;
                    case 7:
                        placeOrder();
                        break;
                    case 8:
                        cancelOrder();
                        break;
                    case 9:
                        viewOrders();
                        break;
                    case 10:
                        lowStockAlert();
                        break;
                    case 11:
                        returnProduct();
                        break;
                    case 12:
                        simulateConcurrentUsers();
                        break;
                    case 13:
                        viewLogs();
                        break;
                    case 14:
                        triggerFailureMode();
                        break;
                    case 0:
                        System.out.println("Thank you for using the system!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private static void initializeServices() {
        productService = new ProductService();
        cartService = new CartService(productService);
        orderService = new OrderService(productService, cartService);
        paymentService = new PaymentService();
        discountService = new DiscountService();
        inventoryService = new InventoryService(productService);
        eventService = new EventService();
        auditService = new AuditService();
        fraudService = new FraudDetectionService();
        failureService = new FailureInjectionService();
        
        // Add sample products
        productService.addProduct(new Product("P001", "Laptop", 50000, 10));
        productService.addProduct(new Product("P002", "Mouse", 500, 50));
        productService.addProduct(new Product("P003", "Keyboard", 1500, 30));
        productService.addProduct(new Product("P004", "Monitor", 15000, 15));
        productService.addProduct(new Product("P005", "USB Cable", 300, 100));
    }
    
    private static void displayMenu() {
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1. Add Product");
        System.out.println("2. View Products");
        System.out.println("3. Add to Cart");
        System.out.println("4. Remove from Cart");
        System.out.println("5. View Cart");
        System.out.println("6. Apply Coupon");
        System.out.println("7. Place Order");
        System.out.println("8. Cancel Order");
        System.out.println("9. View Orders");
        System.out.println("10. Low Stock Alert");
        System.out.println("11. Return Product");
        System.out.println("12. Simulate Concurrent Users");
        System.out.println("13. View Logs");
        System.out.println("14. Trigger Failure Mode");
        System.out.println("0. Exit");
        System.out.println("====================");
    }
    
    private static void addProduct() {
        System.out.println("\n--- Add New Product ---");
        String id = getStringInput("Product ID: ");
        String name = getStringInput("Product Name: ");
        double price = getDoubleInput("Price: ");
        int stock = getIntInput("Stock Quantity: ");
        
        Product product = new Product(id, name, price, stock);
        if (productService.addProduct(product)) {
            System.out.println("Product added successfully!");
            auditService.log(currentUser, "ADDED_PRODUCT", product.getId());
        } else {
            System.out.println("Product ID already exists!");
        }
    }
    
    private static void viewProducts() {
        System.out.println("\n--- Available Products ---");
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        System.out.printf("%-10s %-20s %-10s %-10s\n", "ID", "Name", "Price", "Stock");
        System.out.println("------------------------------------------------");
        for (Product p : products) {
            System.out.printf("%-10s %-20s ₹%-9.2f %-10d\n", 
                p.getId(), p.getName(), p.getPrice(), p.getStock());
        }
    }
    
    private static void addToCart() {
        System.out.println("\n--- Add to Cart ---");
        viewProducts();
        String productId = getStringInput("Enter Product ID: ");
        int quantity = getIntInput("Enter Quantity: ");
        
        if (cartService.addToCart(currentUser, productId, quantity)) {
            System.out.println("Added to cart successfully!");
            auditService.log(currentUser, "ADDED_TO_CART", productId + " qty=" + quantity);
        } else {
            System.out.println("Failed to add to cart. Insufficient stock!");
        }
    }
    
    private static void removeFromCart() {
        System.out.println("\n--- Remove from Cart ---");
        viewCart();
        String productId = getStringInput("Enter Product ID to remove: ");
        
        if (cartService.removeFromCart(currentUser, productId)) {
            System.out.println("Removed from cart successfully!");
            auditService.log(currentUser, "REMOVED_FROM_CART", productId);
        } else {
            System.out.println("Product not found in cart!");
        }
    }
    
    private static void viewCart() {
        System.out.println("\n--- Your Cart ---");
        Cart cart = cartService.getCart(currentUser);
        if (cart.getItems().isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        
        System.out.printf("%-10s %-20s %-10s %-10s %-10s\n", 
            "ID", "Name", "Price", "Quantity", "Subtotal");
        System.out.println("--------------------------------------------------------");
        double total = 0;
        for (CartItem item : cart.getItems().values()) {
            double subtotal = item.getProduct().getPrice() * item.getQuantity();
            total += subtotal;
            System.out.printf("%-10s %-20s ₹%-9.2f %-10d ₹%-9.2f\n",
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getQuantity(),
                subtotal);
        }
        System.out.println("--------------------------------------------------------");
        System.out.printf("Total: ₹%.2f\n", total);
        
        // Apply discount if applicable
        double discount = discountService.calculateDiscount(total, cart);
        if (discount > 0) {
            System.out.printf("Discount Applied: -₹%.2f\n", discount);
            System.out.printf("Final Total: ₹%.2f\n", total - discount);
        }
    }
    
    private static void applyCoupon() {
        System.out.println("\n--- Apply Coupon ---");
        viewCart();
        String coupon = getStringInput("Enter coupon code (SAVE10/FLAT200): ");
        
        Cart cart = cartService.getCart(currentUser);
        discountService.applyCoupon(cart, coupon);
        
        System.out.println("Coupon applied successfully!");
        auditService.log(currentUser, "APPLIED_COUPON", coupon);
        viewCart();
    }
    
    private static void placeOrder() {
        System.out.println("\n--- Place Order ---");
        Cart cart = cartService.getCart(currentUser);
        
        if (cart.getItems().isEmpty()) {
            System.out.println("Cart is empty! Cannot place order.");
            return;
        }
        
        // Check fraud detection
        if (fraudService.isSuspicious(currentUser)) {
            System.out.println("WARNING: Suspicious activity detected!");
            String confirm = getStringInput("Continue anyway? (y/n): ");
            if (!confirm.equalsIgnoreCase("y")) {
                return;
            }
        }
        
        // Check idempotency - prevent duplicate orders
        if (orderService.hasPendingOrder(currentUser)) {
            System.out.println("You already have a pending order! Please wait.");
            return;
        }
        
        // Inject failure if enabled
        if (failureService.shouldFail("ORDER_CREATION")) {
            System.out.println("Order creation failed due to injected failure!");
            return;
        }
        
        try {
            Order order = orderService.createOrder(currentUser);
            
            // Process payment
            System.out.println("Processing payment...");
            boolean paymentSuccess = paymentService.processPayment(order.getTotal());
            
            if (paymentSuccess) {
                order.setStatus(OrderStatus.PAID);
                orderService.confirmOrder(order.getId());
                System.out.println("Order placed successfully!");
                System.out.println("Order ID: " + order.getId());
                System.out.printf("Total Amount: ₹%.2f\n", order.getTotal());
                
                auditService.log(currentUser, "ORDER_PLACED", order.getId());
                eventService.publish(new Event("ORDER_CREATED", order.getId()));
                eventService.publish(new Event("PAYMENT_SUCCESS", order.getId()));
            } else {
                // Payment failed - restore stock
                order.setStatus(OrderStatus.PAYMENT_FAILED);
                orderService.failOrder(order.getId());
                System.out.println("Payment failed! Order cancelled.");
                auditService.log(currentUser, "ORDER_PAYMENT_FAILED", order.getId());
                eventService.publish(new Event("PAYMENT_FAILED", order.getId()));
            }
        } catch (Exception e) {
            System.out.println("Order placement failed: " + e.getMessage());
        }
    }
    
    private static void cancelOrder() {
        System.out.println("\n--- Cancel Order ---");
        viewOrders();
        String orderId = getStringInput("Enter Order ID to cancel: ");
        
        try {
            orderService.cancelOrder(orderId);
            System.out.println("Order cancelled successfully!");
            auditService.log(currentUser, "ORDER_CANCELLED", orderId);
        } catch (Exception e) {
            System.out.println("Failed to cancel order: " + e.getMessage());
        }
    }
    
    private static void viewOrders() {
        System.out.println("\n--- Your Orders ---");
        List<Order> orders = orderService.getUserOrders(currentUser);
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        
        System.out.printf("%-15s %-12s %-10s %-20s\n", 
            "Order ID", "Status", "Total", "Date");
        System.out.println("--------------------------------------------------------");
        for (Order order : orders) {
            System.out.printf("%-15s %-12s ₹%-9.2f %-20s\n",
                order.getId(),
                order.getStatus(),
                order.getTotal(),
                order.getOrderDate().toString());
        }
    }
    
    private static void lowStockAlert() {
        System.out.println("\n--- Low Stock Alert ---");
        List<Product> lowStockProducts = inventoryService.getLowStockProducts(10);
        
        if (lowStockProducts.isEmpty()) {
            System.out.println("All products have sufficient stock.");
            return;
        }
        
        System.out.println("Products with low stock (<10 units):");
        System.out.printf("%-10s %-20s %-10s\n", "ID", "Name", "Stock");
        System.out.println("----------------------------------------");
        for (Product p : lowStockProducts) {
            System.out.printf("%-10s %-20s %-10d\n", p.getId(), p.getName(), p.getStock());
        }
    }
    
    private static void returnProduct() {
        System.out.println("\n--- Return Product ---");
        viewOrders();
        String orderId = getStringInput("Enter Order ID: ");
        String productId = getStringInput("Enter Product ID to return: ");
        int quantity = getIntInput("Enter quantity to return: ");
        
        try {
            orderService.returnItems(orderId, productId, quantity);
            System.out.println("Product returned successfully!");
            auditService.log(currentUser, "PRODUCT_RETURNED", orderId + " " + productId);
        } catch (Exception e) {
            System.out.println("Failed to return product: " + e.getMessage());
        }
    }
    
    private static void simulateConcurrentUsers() {
        System.out.println("\n--- Simulate Concurrent Users ---");
        System.out.println("Testing concurrency with 5 users accessing same product...");
        
        String productId = "P001"; // Laptop
        Product product = productService.getProduct(productId);
        if (product == null) {
            System.out.println("Product not found!");
            return;
        }
        
        int initialStock = product.getStock();
        System.out.println("Initial stock: " + initialStock);
        
        // Create multiple threads to simulate concurrent access
        List<Thread> threads = new ArrayList<>();
        List<Boolean> results = Collections.synchronizedList(new ArrayList<>());
        
        for (int i = 1; i <= 5; i++) {
            final int userId = i;
            Thread t = new Thread(() -> {
                String user = "concurrent_user_" + userId;
                boolean success = cartService.addToCart(user, productId, 3);
                results.add(success);
                if (success) {
                    System.out.println("User " + userId + " added 3 items successfully");
                } else {
                    System.out.println("User " + userId + " failed to add items");
                }
            });
            threads.add(t);
            t.start();
        }
        
        // Wait for all threads to complete
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        int finalStock = productService.getProduct(productId).getStock();
        System.out.println("Final stock: " + finalStock);
        System.out.println("Total reserved: " + (initialStock - finalStock));
        System.out.println("Successful reservations: " + results.stream().filter(r -> r).count());
    }
    
    private static void viewLogs() {
        System.out.println("\n--- Audit Logs (Last 20) ---");
        List<AuditLog> logs = auditService.getRecentLogs(20);
        if (logs.isEmpty()) {
            System.out.println("No logs found.");
            return;
        }
        
        for (AuditLog log : logs) {
            System.out.println(log);
        }
    }
    
    private static void triggerFailureMode() {
        System.out.println("\n--- Failure Mode Configuration ---");
        System.out.println("1. Enable Payment Failure");
        System.out.println("2. Enable Order Creation Failure");
        System.out.println("3. Enable Inventory Update Failure");
        System.out.println("4. Disable All Failures");
        System.out.println("5. Show Current Failure Status");
        
        int choice = getIntInput("Select option: ");
        
        switch (choice) {
            case 1:
                failureService.enableFailure("PAYMENT");
                System.out.println("Payment failures enabled!");
                break;
            case 2:
                failureService.enableFailure("ORDER_CREATION");
                System.out.println("Order creation failures enabled!");
                break;
            case 3:
                failureService.enableFailure("INVENTORY");
                System.out.println("Inventory update failures enabled!");
                break;
            case 4:
                failureService.disableAllFailures();
                System.out.println("All failures disabled!");
                break;
            case 5:
                System.out.println("Current failure status: " + failureService.getStatus());
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }
    
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
    
    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input! " + prompt);
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
    
    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input! " + prompt);
            scanner.next();
        }
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }
}