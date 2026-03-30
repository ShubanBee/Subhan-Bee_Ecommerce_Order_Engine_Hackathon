package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.Service1;



import com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.modal1.Product;
import com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.Repository.ProductRepository;
import com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.lock.ProductLockManager;
import java.util.*;

public class ProductService {
    private ProductRepository productRepository;
    private ProductLockManager lockManager;
    
    public ProductService() {
        this.productRepository = new ProductRepository();
        this.lockManager = new ProductLockManager();
    }
    
    public boolean addProduct(Product product) {
        if (productRepository.findById(product.getId()) != null) {
            return false;
        }
        productRepository.save(product);
        return true;
    }
    
    public Product getProduct(String id) {
        return productRepository.findById(id);
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public void updateStock(String productId, int quantity) {
        lockManager.acquireLock(productId);
        try {
            Product product = getProduct(productId);
            if (product != null) {
                product.setStock(quantity);
                productRepository.update(product);
            }
        } finally {
            lockManager.releaseLock(productId);
        }
    }
    
    public boolean reserveStock(String productId, int quantity) {
        lockManager.acquireLock(productId);
        try {
            Product product = getProduct(productId);
            if (product != null && product.hasStock(quantity)) {
                product.reduceStock(quantity);
                productRepository.update(product);
                return true;
            }
            return false;
        } finally {
            lockManager.releaseLock(productId);
        }
    }
    
    public void releaseStock(String productId, int quantity) {
        lockManager.acquireLock(productId);
        try {
            Product product = getProduct(productId);
            if (product != null) {
                product.increaseStock(quantity);
                productRepository.update(product);
            }
        } finally {
            lockManager.releaseLock(productId);
        }
    }
    
    public boolean hasStock(String productId, int quantity) {
        Product product = getProduct(productId);
        return product != null && product.hasStock(quantity);
    }
    
    public void updateProduct(Product product) {
        productRepository.update(product);
    }
}