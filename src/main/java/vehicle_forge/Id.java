package vehicle_forge;

public class Id {
	private final int id;

	private final String logTag = Id.class.getName();
	
	public Id(int Id){
		
		this.id = Id;
	}
	
	public Id(IdBuilder build){
		this.id = build.id;
	}
	
	public int getId(){
		return id;
	}
	
	//Service Builder
    public static class IdBuilder {
    	
    	private int id;
   
    	public IdBuilder(int id) {
    		this.id = id;
    	}  	
    	    	
    	public Id build() {
    		return new Id(this);
    	}
    }
}
