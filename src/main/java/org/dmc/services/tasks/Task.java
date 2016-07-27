package org.dmc.services.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {

    private String id;
    private String title;
    private String projectId;
    private String assignee;
    private String assigneeId;
    private String reporter;
    private String reporterId;
    private long dueDate;
    private String additionalDetails;
    private String status;
    private int priority;

    @JsonProperty("id")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("projectId")
    public String getProjectId() {
        return projectId;
    }
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @JsonProperty("assignee")
    public String getAssignee() {
        return assignee;
    }
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @JsonProperty("assigneeId")
    public String getAssigneeId() {
        return assigneeId;
    }
    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    @JsonProperty("reporter")
    public String getReporter() {
        return reporter;
    }
    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    @JsonProperty("reporterId")
    public String getReporterId() {
        return reporterId;
    }
    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }

    @JsonProperty("dueDate")
    public long getDueDate() {
        return dueDate;
    }
    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    @JsonProperty("additionalDetails")
    public String getAdditionalDetails() {
        return additionalDetails;
    }
    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("priority")
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }

	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class Task {\n");
		
		sb.append("  id: ").append(id).append("\n");
		sb.append("  title: ").append(title).append("\n");
		sb.append("  projectId: ").append(projectId).append("\n");
		sb.append("  assignee: ").append(assignee).append("\n");
		sb.append("  assigneeId: ").append(assigneeId).append("\n");
		sb.append("  reporter: ").append(reporter).append("\n");
		sb.append("  reporterId: ").append(reporterId).append("\n");
		sb.append("  dueDate: ").append(dueDate).append("\n");
		sb.append("  additionalDetails: ").append(additionalDetails).append("\n");
		sb.append("  status: ").append(status).append("\n");
		sb.append("  priority: ").append(priority).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}