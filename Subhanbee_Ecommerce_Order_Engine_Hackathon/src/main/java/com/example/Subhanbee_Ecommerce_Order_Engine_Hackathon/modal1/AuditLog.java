package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.modal1;



import java.text.SimpleDateFormat;
import java.util.Date;

public class AuditLog {
    private String timestamp;
    private String userId;
    private String action;
    private String details;
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public AuditLog(String userId, String action, String details) {
        this.timestamp = DATE_FORMAT.format(new Date());
        this.userId = userId;
        this.action = action;
        this.details = details;
    }
    
    public String getTimestamp() { return timestamp; }
    public String getUserId() { return userId; }
    public String getAction() { return action; }
    public String getDetails() { return details; }
    
    @Override
    public String toString() {
        return String.format("[%s] %s %s %s", timestamp, userId, action, details);
    }
}