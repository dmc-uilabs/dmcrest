package org.dmc.services.components;
import org.dmc.services.sharedattributes.FeatureImage;

import java.util.ArrayList;

public class Component {

	private final int id;
	private final String title;
	private final String description;
	private final String owner;
	private ArrayList<String> tags;
	private FeatureImage image;
	private String servicesLink;
	private String releaseDate;
	
	public Component(ComponentBuilder build){
		this.id = build.id;
		this.description = build.description;
		this.title = build.title;
		this.owner = build.owner;
		this.tags = build.tags;
		this.image = build.image;
		this.servicesLink = build.servicesLink;
		this.releaseDate = build.releaseDate;
	}
	
	public int getId(){
		return id;
	}
	
	public String getReleaseDate(){
		return releaseDate;
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
	
	public ArrayList<String> getTags(){
		return tags;
	}
	
	public FeatureImage getFeatureImage(){
		return image;
	}
	
	public String getServices(){
		return servicesLink;
	}
	
	public static class ComponentBuilder{
		private int id;
		private String title;
		private String description;
		private String owner;
		private ArrayList<String> tags;
		private FeatureImage image; 
		private String servicesLink;
		private String releaseDate;
		
		public ComponentBuilder(int id, String title, String description) {
    		this.id = id;
    		this.title = title;
    		this.description = description;
    	}  	
    	
    	public ComponentBuilder(int id, String title) {
    		this.id = id;
    		this.title = title;
    	}
    	
    	public ComponentBuilder servicesLink(){
    		this.servicesLink = "/components/" + id + "/services";
    		return this;
    	}
    	
    	public ComponentBuilder servicesLink(String link){
    		servicesLink = link;
    		return this;
    	}
    	
    	public ComponentBuilder image(FeatureImage image) {
    		this.image = image;
    		return this;
    	}
    	
    	public ComponentBuilder releaseDate(String releaseDate){
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
    	
    	
    	public Component build() {
    		return new Component(this);
    	}
		
		
	}
	
	
	
}
