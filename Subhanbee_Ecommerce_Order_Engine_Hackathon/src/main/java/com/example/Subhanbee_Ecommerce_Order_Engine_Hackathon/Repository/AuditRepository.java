package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.Repository;



import com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.modal1.AuditLog;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class AuditRepository {
    private List<AuditLog> auditLogs;
    private int maxSize;
    
    public AuditRepository() {
        this.auditLogs = new CopyOnWriteArrayList<>();
        this.maxSize = 10000;
    }
    
    public AuditRepository(int maxSize) {
        this.auditLogs = new CopyOnWriteArrayList<>();
        this.maxSize = maxSize;
    }
    
    public void save(AuditLog log) {
        auditLogs.add(log);
        
        // Maintain size limit
        while (auditLogs.size() > maxSize) {
            auditLogs.remove(0);
        }
    }
    
    public List<AuditLog> findAll() {
        return new ArrayList<>(auditLogs);
    }
    
    public List<AuditLog> findByUserId(String userId) {
        List<AuditLog> result = new ArrayList<>();
        for (AuditLog log : auditLogs) {
            if (log.getUserId().equals(userId)) {
                result.add(log);
            }
        }
        return result;
    }
    
    public List<AuditLog> findByAction(String action) {
        List<AuditLog> result = new ArrayList<>();
        for (AuditLog log : auditLogs) {
            if (log.getAction().equals(action)) {
                result.add(log);
            }
        }
        return result;
    }
    
    public List<AuditLog> findRecent(int count) {
        int start = Math.max(0, auditLogs.size() - count);
        return new ArrayList<>(auditLogs.subList(start, auditLogs.size()));
    }
    
    public void clear() {
        auditLogs.clear();
    }
    
    public int size() {
        return auditLogs.size();
    }
}