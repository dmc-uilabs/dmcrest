package org.dmc.services.projects;

public class ProjectTag {
	private int id;
	private int projectId;
	private String name;
		
	public ProjectTag(int id, String projectId, String name){
		this.id = id;
		this.projectId = Integer.parseInt(projectId);
		this.name = name;
	}

	public int getId(){
		return id;
	}
	
	public String getProjectId(){
		return String.format("%d", projectId);
	}
	
	public String getName(){
		return name;
	}
	
}
