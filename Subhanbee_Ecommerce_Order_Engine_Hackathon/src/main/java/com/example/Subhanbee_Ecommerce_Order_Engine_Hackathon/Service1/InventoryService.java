package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.Service1;


import com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.modal1.Product;
import java.util.*;
import java.util.stream.Collectors;

public class InventoryService {
    private ProductService productService;
    
    public InventoryService(ProductService productService) {
        this.productService = productService;
    }
    
    public List<Product> getLowStockProducts(int threshold) {
        return productService.getAllProducts().stream()
            .filter(product -> product.getStock() <= threshold)
            .collect(Collectors.toList());
    }
    
    public List<Product> getOutOfStockProducts() {
        return productService.getAllProducts().stream()
            .filter(product -> product.getStock() == 0)
            .collect(Collectors.toList());
    }
    
    public boolean checkAvailability(String productId, int quantity) {
        Product product = productService.getProduct(productId);
        return product != null && product.hasStock(quantity);
    }
    
    public Map<String, Integer> getInventorySnapshot() {
        Map<String, Integer> snapshot = new HashMap<>();
        for (Product product : productService.getAllProducts()) {
            snapshot.put(product.getId(), product.getStock());
        }
        return snapshot;
    }
    
    public void restockProduct(String productId, int quantity) {
        productService.updateStock(productId, 
            productService.getProduct(productId).getStock() + quantity);
    }
    
    public void setLowStockAlertThreshold(int threshold) {
        // This would typically send notifications
        List<Product> lowStock = getLowStockProducts(threshold);
        if (!lowStock.isEmpty()) {
            System.out.println("ALERT: Low stock products: " + lowStock);
        }
    }
}