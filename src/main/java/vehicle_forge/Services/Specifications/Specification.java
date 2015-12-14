package vehicle_forge;

public class Specification {

	private final String logTag = Specification.class.getName();
	private String description = "";
    private final int num_inputs;
    private final int num_outputs;
    private final Usage usage_stats;
    private final Trial trial_stats;

    public Specification(String desc, int inputs, int outputs, Usage usage, Trial trial) {
    	
    	this.description = desc;
        this.num_inputs = inputs; 
        this.num_outputs = outputs;
        this.usage_stats = usage;
        this.trial_stats = trial;
    }
    
    public String getDescription(){
    	return description;
    }
    
    public int getInput() {
        return num_inputs;
    }
    
    public int getOutput() {
    	return num_outputs;
    }
    
    public Usage getUsageStats(){
    	return usage_stats;
    }
   
    public Trial getRunStats(){
    	return trial_stats;
    }
    
   
}