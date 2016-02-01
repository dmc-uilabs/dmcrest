package vehicle_forge;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Task {
	private final String logTag = Task.class.getName();
	private final int id;
	private final String title;
	private final TaskProject tProject;
	private final String assignee;
	private final String reporter;
	private final String dueDate;
	private final int priority;
	private final String summary;
	private final String details;

	public Task(int id, String title, TaskProject taskProject, String assignee, String reporter, String dueDate,
			int priority, String summary, String details) {
		this.id = id;
		this.title = title;
		this.tProject = taskProject;
		this.assignee = assignee;
		this.reporter = reporter;
		this.dueDate = dueDate;
		this.priority = priority;
		this.summary = summary;
		this.details = details;
	}

	public int getId()
    {
    	return id;
    }

	public String getTitle()
    {
    	return title;
    }

	public TaskProject getProject()
    {
    	return tProject;
    }

	public String getAssignee() 
	{
		return assignee;
	}

	public String getReporter() 
	{
		return reporter;
	}

	public String getDueDate() 
	{
		return dueDate;
	}
 
	public int getPriority() 
	{
		return priority;
	}
	
	public String getSummary() 
	{
		return summary;
	}
	
	public String getDetails() 
	{
		return details;
	}
}