package vehicle_forge;

public class Role {

	private final String logTag = Role.class.getName();
    private final int id;
    private final String name;
    private final String email = "test@test.com";

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    
    public int getId() {
        return id;
    }
    
    public String getName() {
    	return name;
    }
    
    public Test getTest() {
    	return new Test(email);
    }
}