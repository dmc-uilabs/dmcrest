package org.dmc.services.components;

import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.services.Service;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;


public class Component {

	private final int id;
	private final String title;
	private final String description;
	private final String owner;
    private final Date releaseDate;  //new
	private ArrayList<String> tags;
	private FeatureImage featureImage;
    private ArrayList<Service> services; //new
	private String projectId = null;  // new
    private String type = null;  //new
	
    public Component() {
        this.id = -1;
        this.title = null;
        this.description = null;
        this.owner = null;
        this.releaseDate = null;
        this.tags = new ArrayList<String>();
        this.featureImage = null;
        this.services = new ArrayList<Service>();
        this.projectId = null;
        this.type = null;
    }
    
	public Component(ComponentBuilder build){
		this.id = build.id;
		this.title = build.title;
		this.description = build.description;
		this.owner = build.owner;
		this.releaseDate = build.releaseDate;
		this.tags = build.tags;
		this.featureImage = build.image;
        this.services = build.services;
        this.projectId = build.projectId;
        this.type = build.type;
	}
	
	public int getId(){
		return id;
	}
		
	public String getTitle(){
		return title;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getOwner(){
		return owner;
	}

    public String getReleaseDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
		return formatter.format(releaseDate);
	}
	
	public ArrayList<String> getTags(){
		return tags;
	}
	
	public FeatureImage getFeatureImage(){
		return featureImage;
	}
	
	public ArrayList<Service> getServices(){
		return services;
	}

	public String getProjectId() {
        return projectId;
    }
    
    public String getType() {
        return type;
    }
	
	public static class ComponentBuilder{
		private int id;
		private String title;
		private String description;
		private String owner;
		private Date releaseDate;
		private ArrayList<String> tags;
		private FeatureImage image; 
        private ArrayList<Service> services;
        private String projectId;
        private String type;
        
		public ComponentBuilder(int id, String title, String description) {
    		this.id = id;
    		this.title = title;
    		this.description = description;
    	}  	
    	
    	public ComponentBuilder(int id, String title) {
    		this.id = id;
    		this.title = title;
    	}
    	   
    	public ComponentBuilder image(FeatureImage image) {
    		this.image = image;
    		return this;
    	}
    	
    	public ComponentBuilder releaseDate(Date releaseDate){
    		this.releaseDate = releaseDate;
    		return this;
    	}
    	
    	public ComponentBuilder owner(String owner) {
    		this.owner = owner;
    		return this;
    	}
    	
    	public ComponentBuilder tags(ArrayList<String> tags) {
    		this.tags = tags;
    		return this;
    	}
    	
    	public ComponentBuilder services(ArrayList<Service> services) {
    		this.services = services;
    		return this;
    	}
        
        public ComponentBuilder projectId(String projectId) {
    		this.projectId = projectId;
    		return this;
    	}
        
        public ComponentBuilder type(String type) {
    		this.type = type;
    		return this;
    	}
        
    	public Component build() {
    		return new Component(this);
    	}
	}
}
