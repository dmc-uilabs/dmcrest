package org.dmc.services.projects;

public class ProjectTag {
	private final int id;
	private final String tag;
		
	public ProjectTag(int id, String tag){
		
		this.id = id;
		this.tag = tag;
	}

	public int getId(){
		return id;
	}
	
	public String getTag(){
		return tag;
	}
	
}
