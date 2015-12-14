package vehicle_forge;

public class Usage {

	private final String logTag = Usage.class.getName();
    private final int members;
    private final int added;
    
    public Usage(int members, int added) {
        this.members = members;
        this.added = added;
    }
    
    public int getMembers() {
        return members;
    }
    
    public int getAdded() {
    	return added;
    }
    
    @Override
    public String toString(){
    	return "Members using this product: " + members 
    			+ "\nProjects utilizing this product: " + added;
    }
}