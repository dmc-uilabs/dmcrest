package org.dmc.services.tasks;

public class Task {
	private final String logTag = Task.class.getName();
	private final int id;
	private final String title;
	private final TaskProject tProject;
	private final String assignee;
	private final String reporter;
	private final String dueDate;
	private final String priority;

	public Task(int id, String title, TaskProject taskProject, String assignee, String reporter, String dueDate,
			String priority) {
		this.id = id;
		this.title = title;
		this.tProject = taskProject;
		this.assignee = assignee;
		this.reporter = reporter;
		this.dueDate = dueDate;
		this.priority = priority;
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

	public String getAssignee() {
		return assignee;
	}

	public String getReporter() {
		return reporter;
	}

	public String getDueDate() {
		return dueDate;
	}
 
	public String getPriority() {
		return priority;
	}
}