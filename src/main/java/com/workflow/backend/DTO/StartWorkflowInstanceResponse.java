package com.workflow.backend.DTO;

import java.time.LocalDateTime;
import java.util.List;

import com.workflow.backend.model.WorkflowTask;

public class StartWorkflowInstanceResponse {

    private String id;
    private String trackingCode;
    private String processId;
    private String processVersionId;
    private String title;
    private String status;
    private String currentNodeId;
    private LocalDateTime createdAt;
    private List<WorkflowTask> tasks;

    public StartWorkflowInstanceResponse() {}

    public StartWorkflowInstanceResponse(
        String id,
        String trackingCode,
        String processId,
        String processVersionId,
        String title,
        String status,
        String currentNodeId,
        LocalDateTime createdAt,
        List<WorkflowTask> tasks
    ) {
        this.id = id;
        this.trackingCode = trackingCode;
        this.processId = processId;
        this.processVersionId = processVersionId;
        this.title = title;
        this.status = status;
        this.currentNodeId = currentNodeId;
        this.createdAt = createdAt;
        this.tasks = tasks;
    }

    public String getId() {
        return id;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public String getProcessId() {
        return processId;
    }

    public String getProcessVersionId() {
        return processVersionId;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public String getCurrentNodeId() {
        return currentNodeId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<WorkflowTask> getTasks() {
        return tasks;
    }
}