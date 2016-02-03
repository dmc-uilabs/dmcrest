package org.dmc.services.projects;

public class ProjectDiscussion {
	
	private final int totalItems;
	private final String link; 
	
	private final String logTag = ProjectDiscussion.class.getName();
	
	public ProjectDiscussion(int total, int projectId){
		totalItems = total;
		link = "/projects/"+projectId+"/discussions";
	}
	
	public int getTotalItems(){
		return totalItems;
	}
	
	public String getLink(){
		return link;
	}

}
