package org.dmc.services.services;

import java.lang.Integer;

public class ServiceCurrentStatus {

    private final String logTag = ServiceCurrentStatus.class.getName();
	          
    private final int percentCompleted;
    private final String startDate;    
    private final String startTime;   
    
    public ServiceCurrentStatus(int percentCompleted, String startDate, String startTime) {
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