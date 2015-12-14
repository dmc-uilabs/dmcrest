package vehicle_forge;

public class ProjectTask {
	private final int totalItems;
	private final String link; 
	
	private final String logTag = ProjectTask.class.getName();
	
	public ProjectTask(int total, int projectId){
		totalItems = total;
		link = "/projects/"+projectId+"/tasks";
	}
	
	public int getTotalItems(){
		return totalItems;
	}
	
	public String getLink(){
		return link;
	}
}
