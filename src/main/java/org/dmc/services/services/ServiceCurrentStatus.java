package org.dmc.services.services;

import java.lang.Integer;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceCurrentStatus {

    private final String logTag = ServiceCurrentStatus.class.getName();
	          
    private final int percentCompleted;
    private final String startDate;    
    private final String startTime;   
    
    public ServiceCurrentStatus(@JsonProperty("percentCompleted") int percentCompleted, 
            @JsonProperty("startDate") String startDate,
            @JsonProperty("startTime") String startTime) {
        this.percentCompleted = percentCompleted;
        this.startDate = startDate;
        this.startTime = startTime;
    }

    public String getPercentCompleted() {
    	return Integer.toString(percentCompleted);
    }
    
    public String getStartDate() {
    	return startDate;
    }
    
    public String getStartTime() {
    	return startTime;
    }
}