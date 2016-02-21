package org.dmc.services.users;

import java.util.ArrayList;

import org.dmc.services.services.Service;

public class UserRunningServices {

    class RunningService {
        private final String title;
        private final int serviceId;
        private final int projectId;
        private final CurrentStatus currentStatus;
        //        "$$hashKey": "object:56"
        
        RunningService(String title, int serviceId, int projectId, CurrentStatus currentStatus) {
            this.title = title;
            this.serviceId = serviceId;
            this.projectId = projectId;
            this.currentStatus = currentStatus;
        }
        
        public String getTitle() {
            return title;
        }
        
        public int getServiceId() {
            return serviceId;
        }
        
        public int getProjectId() {
            return projectId;
        }
        
        public CurrentStatus getCurrentStatus() {
            return currentStatus;
        }
        
        class CurrentStatus {
            private final int percentCompleted; // measured from 0-100
            private final String startDate;
            private final String startTime;
            
            CurrentStatus(int percentCompleted, String startDate, String startTime) {
                this.percentCompleted = percentCompleted;
                this.startDate = startDate;
                this.startTime = startTime;
            }
            
            public int getPercentCompleted() {
                return percentCompleted;
            }
            
            public String getStartDate() {
                return startDate;
            }
            
            public String getStartTime() {
                return startTime;
            }
        }
    }
    
	private final ArrayList<RunningService> items;
	
	private final String logTag = UserRunningServices.class.getName();
	
    public UserRunningServices(){
		this(new ArrayList<RunningService>());
	}
    
	public UserRunningServices(ArrayList<RunningService> items){
		this.items = items;
	}
	
	public int getTotalItems(){
		return items.size();
	}
	
	public ArrayList<RunningService> getItems(){
		return items;
	}
}
