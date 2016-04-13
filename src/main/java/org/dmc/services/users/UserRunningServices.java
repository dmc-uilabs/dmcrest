package org.dmc.services.users;

import java.util.ArrayList;
import java.util.Objects;

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
        
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            RunningService runningService = (RunningService) o;
            return Objects.equals(title, runningService.title) &&
            Objects.equals(serviceId, runningService.serviceId) &&
            Objects.equals(projectId, runningService.projectId)  &&
            Objects.equals(currentStatus, runningService.currentStatus);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(title, serviceId, projectId, currentStatus);
        }
        
        @Override
        public String toString()  {
            StringBuilder sb = new StringBuilder();
            sb.append("class CurrentStatus {\n");
            
            sb.append("  title: ").append(title).append("\n");
            sb.append("  serviceId: ").append(serviceId).append("\n");
            sb.append("  projectId: ").append(projectId).append("\n");
            sb.append("  currentStatus: ").append(currentStatus).append("\n");
            sb.append("}\n");
            return sb.toString();
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
            
            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                CurrentStatus currentStatus = (CurrentStatus) o;
                return Objects.equals(percentCompleted, currentStatus.percentCompleted) &&
                Objects.equals(startDate, currentStatus.startDate)  &&
                Objects.equals(startTime, currentStatus.startTime);
            }
            
            @Override
            public int hashCode() {
                return Objects.hash(percentCompleted, startDate, startTime);
            }
            
            @Override
            public String toString()  {
                StringBuilder sb = new StringBuilder();
                sb.append("class CurrentStatus {\n");
                
                sb.append("  percentCompleted: ").append(percentCompleted).append("\n");
                sb.append("  startDate: ").append(startDate).append("\n");
                sb.append("  startTime: ").append(startTime).append("\n");
                sb.append("}\n");
                return sb.toString();
            }
        }
    }
    
	private final ArrayList<RunningService> items;
    private final int totalItems;
	
	private final String logTag = UserRunningServices.class.getName();
	
    public UserRunningServices(){
		this(new ArrayList<RunningService>());
	}
    
	public UserRunningServices(ArrayList<RunningService> items){
		this.items = items;
        this.totalItems = items.size();
	}
	
	public int getTotalItems(){
		return items.size();
	}
	
	public ArrayList<RunningService> getItems(){
		return items;
	}
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserRunningServices userRunningServices = (UserRunningServices) o;
        return Objects.equals(totalItems, userRunningServices.totalItems) &&
        Objects.equals(items, userRunningServices.items);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(totalItems, items);
    }
    
    @Override
    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserRunningServices {\n");
        
        sb.append("  totalItems: ").append(totalItems).append("\n");
        sb.append("  items: ").append(items).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

}
