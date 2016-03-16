package org.dmc.services.projects;

import org.dmc.services.sharedattributes.FeatureImage;

public class Project {
	private final int id;
	private final String title;
	private final String description; 
	private final String imagesLink;
	private final FeatureImage image;
	private final ProjectTask task;
	private final ProjectService service;
	private final ProjectDiscussion discussion; 
	private final ProjectComponent component;
	private final String projectManager;
	private final long dueDate;
	
	public static final String PUBLIC = "public";
	public static final String PRIVATE = "private";

	private final String logTag = Project.class.getName();
	
	public Project(int projectId, String title, String description, FeatureImage img, 
			ProjectTask pTask, ProjectService pService,
			ProjectDiscussion pDiscussion, ProjectComponent component, String projectManager, long dueDate){
		
		this.id = projectId;
		this.title = title;
		this.description = description;
		this.imagesLink = "/project/" + projectId + "/images";
		this.image = img;
		this.task = pTask;
		this.service = pService;
		this.discussion = pDiscussion;
		this.component = component;
		this.projectManager = projectManager;
		this.dueDate = dueDate;
				
	}

	public Project(ProjectBuilder build){
		this.id = build.id;
		this.title = build.title;
		this.description = build.description;
		this.imagesLink = build.imagesLink;
		this.image = build.image;
		this.task = build.task;
		this.service = build.service;
		this.discussion = build.discussion;
		this.component = build.component;
		this.projectManager = build.projectManager;
		this.dueDate = build.dueDate;
	}
	
	public int getId(){
		return id;
	}
	
	public String getProjectManager(){
		return projectManager;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getDescription(){
		return description;
	}
	
	public long getDueDate() {
		return dueDate;
	}
	
	public String getImages(){
		return imagesLink;
	}
	
	public FeatureImage getFeatureImage(){
		return image;
	}
	
	public ProjectTask getTasks(){
		return task;
	}
	
	public ProjectService getServices(){
		return service;
	}
	
	public ProjectDiscussion getDiscussions(){
		return discussion;
	}
	
	public ProjectComponent getComponents(){
		return component;
	}
	
	public static int IsPublic(String projectType) {
		if (null == projectType) return 0;
		if (projectType.toLowerCase().equals(PRIVATE)) return 0;
		if (projectType.toLowerCase().equals(PUBLIC)) return 1;
		return 0;
	}
	//Service Builder
    public static class ProjectBuilder {
    	
    	private int id;
    	private String title;
    	private String description; 
    	private String imagesLink;
    	private FeatureImage image;
    	private ProjectTask task;
    	private ProjectService service;
    	private ProjectDiscussion discussion; 
    	private ProjectComponent component;
    	private String projectManager;
    	private long dueDate;

    	public ProjectBuilder(int id, String title, String description) {
    		this.id = id;
    		this.title = title;
    		this.description = description;
    	}  	
    	
    	public ProjectBuilder(int id, String title) {
    		this.id = id;
    		this.title = title;
    	}
    	
    	public ProjectBuilder imgLink(){
    		this.imagesLink = "/project/" + id + "/images";
    		return this;
    	}
    	
    	public ProjectBuilder imgLink(String link){
    		imagesLink = link;
    		return this;
    	}
    	
    	public ProjectBuilder image(FeatureImage image) {
    		this.image = image;
    		return this;
    	}
    	
    	public ProjectBuilder task(ProjectTask task) {
    		this.task = task;
    		return this;
    	}
    	
    	public ProjectBuilder service(ProjectService service) {
    		this.service = service;
    		return this;
    	}
    	
    	public ProjectBuilder discussion(ProjectDiscussion discussion) {
    		this.discussion = discussion;
    		return this;
    	}
    	
    	public ProjectBuilder component(ProjectComponent component){
    		this.component = component;
    		return this;
    	}
    	
    	public ProjectBuilder projectManager(String pm){
    		this.projectManager = pm;
    		return this;
    	}
    	
    	public ProjectBuilder dueDate(long dueDate){
    		this.dueDate = dueDate;
    		return this;
    	}
    	    	public Project build() {
    		return new Project(this);
    	}

    }

}
