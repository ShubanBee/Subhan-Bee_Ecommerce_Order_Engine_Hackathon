package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.Service1;


import com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.modal1.Event;
import java.util.*;
import java.util.concurrent.*;

public class EventService {
    private Queue<Event> eventQueue;
    private List<Event> processedEvents;
    private boolean processingEnabled;
    
    public EventService() {
        this.eventQueue = new ConcurrentLinkedQueue<>();
        this.processedEvents = new CopyOnWriteArrayList<>();
        this.processingEnabled = true;
        
        // Start event processor
        startEventProcessor();
    }
    
    public void publish(Event event) {
        eventQueue.offer(event);
        System.out.println("Event published: " + event);
    }
    
    private void startEventProcessor() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            while (processingEnabled) {
                try {
                    Event event = eventQueue.poll();
                    if (event != null) {
                        processEvent(event);
                    }
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }
    
    private void processEvent(Event event) {
        try {
            System.out.println("Processing event: " + event.getType() + " - " + event.getData());
            
            // Simulate event processing
            switch (event.getType()) {
                case "ORDER_CREATED":
                    handleOrderCreated(event);
                    break;
                case "PAYMENT_SUCCESS":
                    handlePaymentSuccess(event);
                    break;
                case "PAYMENT_FAILED":
                    handlePaymentFailed(event);
                    break;
                case "INVENTORY_UPDATED":
                    handleInventoryUpdated(event);
                    break;
                default:
                    System.out.println("Unknown event type: " + event.getType());
            }
            
            processedEvents.add(event);
        } catch (Exception e) {
            System.err.println("Failed to process event: " + event + " - " + e.getMessage());
            // Re-queue failed event
            eventQueue.offer(event);
        }
    }
    
    private void handleOrderCreated(Event event) {
        System.out.println("Sending order confirmation for order: " + event.getData());
        // Send email, SMS, etc.
    }
    
    private void handlePaymentSuccess(Event event) {
        System.out.println("Updating order status for order: " + event.getData());
        // Update order status, send confirmation
    }
    
    private void handlePaymentFailed(Event event) {
        System.out.println("Payment failed for order: " + event.getData());
        // Notify user, retry payment
    }
    
    private void handleInventoryUpdated(Event event) {
        System.out.println("Inventory updated: " + event.getData());
        // Update cache, notify other services
    }
    
    public List<Event> getProcessedEvents() {
        return new ArrayList<>(processedEvents);
    }
    
    public int getPendingEventCount() {
        return eventQueue.size();
    }
    
    public void stopProcessing() {
        processingEnabled = false;
    }
}