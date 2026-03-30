package com.example.Subhanbee_Ecommerce_Order_Engine_Hackathon.Service1;



import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class FailureInjectionService {
    private Set<String> enabledFailures;
    private double failureProbability;
    
    public FailureInjectionService() {
        this.enabledFailures = new HashSet<>();
        this.failureProbability = 0.3; // 30% failure rate when enabled
    }
    
    public void enableFailure(String failureType) {
        enabledFailures.add(failureType);
    }
    
    public void disableFailure(String failureType) {
        enabledFailures.remove(failureType);
    }
    
    public void disableAllFailures() {
        enabledFailures.clear();
    }
    
    public boolean shouldFail(String operationType) {
        if (!enabledFailures.contains(operationType)) {
            return false;
        }
        
        return ThreadLocalRandom.current().nextDouble() < failureProbability;
    }
    
    public void setFailureProbability(double probability) {
        this.failureProbability = Math.max(0, Math.min(1, probability));
    }
    
    public Set<String> getEnabledFailures() {
        return new HashSet<>(enabledFailures);
    }
    
    public String getStatus() {
        if (enabledFailures.isEmpty()) {
            return "No failures enabled";
        }
        return "Enabled failures: " + enabledFailures + " (rate: " + 
            (failureProbability * 100) + "%)";
    }
    
    public boolean isFailureEnabled(String failureType) {
        return enabledFailures.contains(failureType);
    }
}
