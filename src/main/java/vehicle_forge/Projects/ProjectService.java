package vehicle_forge;

public class ProjectService {
	
	private final int totalItems;
	private final String link; 
	
	private final String logTag = ProjectService.class.getName();	
	
	public ProjectService(int total, int projectId){
		totalItems = total;
		link = "/projects/"+projectId+"/services";
	}
	
	public int getTotalItems(){
		return totalItems;
	}
	
	public String getLink(){
		return link;
	}

}
