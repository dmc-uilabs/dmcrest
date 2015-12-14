package vehicle_forge;

public class ProjectComponent {
	private final int totalItems;
	private final String link; 
	
	private final String logTag = ProjectComponent.class.getName();
	
	public ProjectComponent(int total, int projectId){
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
