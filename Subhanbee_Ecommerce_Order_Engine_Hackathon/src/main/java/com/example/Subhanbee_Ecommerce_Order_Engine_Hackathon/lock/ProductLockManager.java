package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.lock;


import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class ProductLockManager {
    private ConcurrentHashMap<String, ReentrantLock> locks;
    
    public ProductLockManager() {
        this.locks = new ConcurrentHashMap<>();
    }
    
    public void acquireLock(String productId) {
        ReentrantLock lock = locks.computeIfAbsent(productId, k -> new ReentrantLock());
        lock.lock();
    }
    
    public boolean tryAcquireLock(String productId, long timeout, TimeUnit unit) throws InterruptedException {
        ReentrantLock lock = locks.computeIfAbsent(productId, k -> new ReentrantLock());
        return lock.tryLock(timeout, unit);
    }
    
    public void releaseLock(String productId) {
        ReentrantLock lock = locks.get(productId);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
    
    public boolean isLocked(String productId) {
        ReentrantLock lock = locks.get(productId);
        return lock != null && lock.isLocked();
    }
    
    public void clear() {
        locks.clear();
    }
}