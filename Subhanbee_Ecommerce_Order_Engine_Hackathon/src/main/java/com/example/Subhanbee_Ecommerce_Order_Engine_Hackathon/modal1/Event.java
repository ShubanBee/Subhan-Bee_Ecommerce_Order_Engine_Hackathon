package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.modal1;



import java.util.Date;

public class Event {
    private String type;
    private String data;
    private Date timestamp;
    
    public Event(String type, String data) {
        this.type = type;
        this.data = data;
        this.timestamp = new Date();
    }
    
    public String getType() { return type; }
    public String getData() { return data; }
    public Date getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return String.format("Event{type='%s', data='%s', timestamp=%s}", 
            type, data, timestamp);
    }
}