package org.dmc.services.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskToCreate {

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

    public TaskToCreate() {
    }

    public TaskToCreate(String id, String title, String projectId, String assignee, String assigneeId, 
            String reporter, String reporterId, long dueDate,
            String additionalDetails, String status, int priority) {
        this.id = id;
        this.title = title;
        this.projectId = projectId;
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
        return this.projectId;
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

}