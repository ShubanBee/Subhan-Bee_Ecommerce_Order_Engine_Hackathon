package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.Repository;

import com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.modal1.Product;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ProductRepository {
    private Map<String, Product> products;
    
    public ProductRepository() {
        this.products = new ConcurrentHashMap<>();
    }
    
    public void save(Product product) {
        products.put(product.getId(), product);
    }
    
    public void update(Product product) {
        products.put(product.getId(), product);
    }
    
    public Product findById(String id) {
        return products.get(id);
    }
    
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }
    
    public void delete(String id) {
        products.remove(id);
    }
    
    public boolean exists(String id) {
        return products.containsKey(id);
    }
    
    public int size() {
        return products.size();
    }
    
    public void clear() {
        products.clear();
    }
}