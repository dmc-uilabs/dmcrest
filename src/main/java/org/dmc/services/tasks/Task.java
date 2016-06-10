package org.dmc.services.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {
    private final String id;
    private final String title;
    private final TaskProject taskProject;
    private final String assignee;
    private final String assigneeId;
    private final String reporter;
    private final String reporterId;
    private final long dueDate;
    private final String additionalDetails;
    private final String status;
    private final int priority;

    public Task(String id, String title, TaskProject taskProject, String assignee, String assigneeId, 
            String reporter, String reporterId, long dueDate,
            String additionalDetails, String status, int priority) {
        this.id = id;
        this.title = title;
        this.taskProject = taskProject;
        this.assignee = assignee;
        this.assigneeId = assigneeId;
        this.reporter = reporter;
        this.reporterId = reporterId;
        this.dueDate = dueDate;
        this.additionalDetails = additionalDetails;
        this.status = status;
        this.priority = priority;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("taskProject")
    public TaskProject getProject() {
        return taskProject;
    }

    @JsonProperty("assignee")
    public String getAssignee() {
        return assignee;
    }

    @JsonProperty("assigneeId")
    public String getAssigneeId() {
        return assigneeId;
    }

    @JsonProperty("reporter")
    public String getReporter() {
        return reporter;
    }

    @JsonProperty("reporterId")
    public String getReporterId() {
        return reporterId;
    }

    @JsonProperty("dueDate")
    public long getDueDate() {
        return dueDate;
    }

    @JsonProperty("additionalDetails")
    public String getAdditionalDetails() {
        return additionalDetails;
    }
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }
    @JsonProperty("priority")
    public int getPriority() {
        return priority;
    }

}