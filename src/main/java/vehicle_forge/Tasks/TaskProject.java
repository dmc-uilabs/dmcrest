package vehicle_forge;
import java.util.Date;

public class TaskProject {
	private final String logTag = TaskProject.class.getName();
    private final int id;
    private final String title;
    public TaskProject(int id, String title)
    {
    	this.id=id;
    	this.title=title;
    }
    
    public int getId()
    {
    	return id;
    }
    public String getTitle()
    {
    	return title;
    }
}