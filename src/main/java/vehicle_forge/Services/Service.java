package vehicle_forge;

import java.util.ArrayList;

public class Service {

    private final String logTag = Service.class.getName();
	
    private final int id;          
    private final String title;    
    private final String description;   
    private final String owner;
    private final String releaseDate;
    private final ArrayList<String> tags;
    private final String specifications;
    private final FeatureImage featureImage;
    private final ServiceCurrentStatus currentStatus;
    private final String serviceType;
    
    private Service(ServiceBuilder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.owner = builder.owner;
        this.releaseDate = builder.releaseDate;
        this.tags = builder.tags;
        this.specifications = "/services/" + builder.id + "/specifications";
        this.featureImage = builder.featureImage;
        this.currentStatus = builder.currentStatus;
        this.serviceType = builder.serviceType;
    }
    
    public String getServiceType(){
    	return serviceType;
    }
    
    public int getId() {
        return id;
    }
    
    public String getTitle() {
    	return title;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public String getOwner() {
    	return owner;
    }
    
    public String getReleaseDate() {
    	return releaseDate;
    }
    
    public ArrayList<String> getTags() {
    	return tags;
    }
    
    public String getSpecifications() {
    	return specifications;
    }
    
    public FeatureImage getFeatureImage() {
    	return featureImage;
    }
    
    public ServiceCurrentStatus getCurrentStatus() {
    	return currentStatus;
    }
    
    //Service Builder
    public static class ServiceBuilder {
    	
    	private final int id;
    	private final String title;
    	private final String description;
    	private String owner;
    	private String releaseDate;
    	private ArrayList<String> tags;
    	private FeatureImage featureImage;
    	private ServiceCurrentStatus currentStatus;
    	private String serviceType;
   
    	public ServiceBuilder(int id, String title, String description) {
    		this.id = id;
    		this.title = title;
    		this.description = description;
    	}
    	
    	public ServiceBuilder serviceType(String type){
    		serviceType = type;
    		return this;
    	}
    	
    	public ServiceBuilder owner(String owner) {
    		this.owner = owner;
    		return this;
    	}
    	
    	public ServiceBuilder releaseDate(String releaseDate) {
    		this.releaseDate = releaseDate;
    		return this;
    	}
    	
    	public ServiceBuilder tags(ArrayList<String> tags) {
    		this.tags = tags;
    		return this;
    	}
    	
    	public ServiceBuilder featureImage(FeatureImage featureImage) {
    		this.featureImage = featureImage;
    		return this;
    	}
    	
    	public ServiceBuilder currentStatus(ServiceCurrentStatus currentStatus) {
    		this.currentStatus = currentStatus;
    		return this;
    	}
    	
    	public Service build() {
    		return new Service(this);
    	}
    }
}