package vehicle_forge;
import java.util.ArrayList;

public class Discussion {

	private final int id;
	private final String text;
	private final String full_name;
	private final String created_at;
	private final int projectId;
	private String avatar;
	
	public Discussion(DiscussionBuilder build){
		this.id = build.id;
		this.text = build.text;
		this.full_name = build.full_name;
		this.created_at = build.created_at;
		this.projectId = build.projectId;
		this.avatar = build.avatar;
	}
	
	public int getId(){
		return id;
	}
	
	public int getProjectId(){
		return projectId;
	}
	
	public String getCreated_at(){
		return created_at;
	}
	
	public String getText(){
		return text;
	}
	
	public String getFull_name(){
		return full_name;
	}
	
	public String getAvatar(){
		return avatar;
	}
	
	
	public static class DiscussionBuilder{
		private int id;
		private String text;
		private String full_name;
		private String created_at;
		private int projectId;
		private String avatar;
		
		public DiscussionBuilder(int id) {
    		this.id = id;
    	}  	
		
		public DiscussionBuilder text(String text){
			this.text = text;
			return this;
		}
    	
    	public DiscussionBuilder full_name(String full_name) {
    		this.full_name = full_name;
    		return this;
    	}
    	
    	public DiscussionBuilder avatar(){
    		this.avatar = "";
    		return this;
    	}
    	
    	public DiscussionBuilder avatar(String avatar){
    		this.avatar = avatar;
    		return this;
    	}
    	
    	public DiscussionBuilder created_at(String created_at) {
    		this.created_at = created_at;
    		return this;
    	}
    	
    	public DiscussionBuilder projectId(int projectId){
    		this.projectId = projectId;
    		return this;
    	}
    	
    	    	
    	public Discussion build() {
    		return new Discussion(this);
    	}
		
		
	}
	
	
	
}
