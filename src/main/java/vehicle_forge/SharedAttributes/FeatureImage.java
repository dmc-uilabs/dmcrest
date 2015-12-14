package vehicle_forge;

public class FeatureImage {

    private final String logTag = FeatureImage.class.getName();
	          
    private final String thumbnail;    
    private final String large;   
    
    public FeatureImage(String thumbnail, String large) {
        this.thumbnail = thumbnail;
        this.large = large;
    }

    public String getThumbnail() {
    	return thumbnail;
    }
    
    public String getLarge() {
    	return large;
    }
}