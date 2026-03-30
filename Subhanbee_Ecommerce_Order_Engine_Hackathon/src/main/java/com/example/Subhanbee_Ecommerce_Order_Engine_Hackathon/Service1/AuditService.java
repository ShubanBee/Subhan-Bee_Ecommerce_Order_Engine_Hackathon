package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.Service1;
import com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.modal1.AuditLog;
import java.util.*;
import java.util.concurrent.*;

public class AuditService {
    private List<AuditLog> auditLogs;
    private int maxLogSize;
    
    public AuditService() {
        this.auditLogs = new CopyOnWriteArrayList<>();
        this.maxLogSize = 1000; // Keep last 1000 logs
    }
    
    public AuditService(int maxLogSize) {
        this.auditLogs = new CopyOnWriteArrayList<>();
        this.maxLogSize = maxLogSize;
    }
    
    public void log(String userId, String action, String details) {
        AuditLog log = new AuditLog(userId, action, details);
        auditLogs.add(log);
        
        // Maintain max log size
        while (auditLogs.size() > maxLogSize) {
            auditLogs.remove(0);
        }
        
        // Also write to console for real-time monitoring
        System.out.println("[AUDIT] " + log);
    }
    
    public List<AuditLog> getLogs() {
        return new ArrayList<>(auditLogs);
    }
    
    public List<AuditLog> getLogsByUser(String userId) {
        List<AuditLog> userLogs = new ArrayList<>();
        for (AuditLog log : auditLogs) {
            if (log.getUserId().equals(userId)) {
                userLogs.add(log);
            }
        }
        return userLogs;
    }
    
    public List<AuditLog> getLogsByAction(String action) {
        List<AuditLog> actionLogs = new ArrayList<>();
        for (AuditLog log : auditLogs) {
            if (log.getAction().equals(action)) {
                actionLogs.add(log);
            }
        }
        return actionLogs;
    }
    
    public List<AuditLog> getRecentLogs(int count) {
        int start = Math.max(0, auditLogs.size() - count);
        return new ArrayList<>(auditLogs.subList(start, auditLogs.size()));
    }
    
    public void clearLogs() {
        auditLogs.clear();
    }
    
    public int getLogCount() {
        return auditLogs.size();
    }
}

