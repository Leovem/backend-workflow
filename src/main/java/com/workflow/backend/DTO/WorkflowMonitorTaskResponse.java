package com.workflow.backend.DTO;

import java.time.LocalDateTime;

public class WorkflowMonitorTaskResponse {

    private String taskId;
    private String instanceId;
    private String processId;
    private String processVersionId;

    private String nodeId;
    private String nodeLabel;

    private String departmentId;
    private String departmentName;

    private String assignedUserId;

    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime claimedAt;
    private LocalDateTime completedAt;
    private LocalDateTime updatedAt;

    public WorkflowMonitorTaskResponse() {}

    public WorkflowMonitorTaskResponse(
        String taskId,
        String instanceId,
        String processId,
        String processVersionId,
        String nodeId,
        String nodeLabel,
        String departmentId,
        String departmentName,
        String assignedUserId,
        String status,
        LocalDateTime createdAt,
        LocalDateTime claimedAt,
        LocalDateTime completedAt,
        LocalDateTime updatedAt
    ) {
        this.taskId = taskId;
        this.instanceId = instanceId;
        this.processId = processId;
        this.processVersionId = processVersionId;
        this.nodeId = nodeId;
        this.nodeLabel = nodeLabel;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.assignedUserId = assignedUserId;
        this.status = status;
        this.createdAt = createdAt;
        this.claimedAt = claimedAt;
        this.completedAt = completedAt;
        this.updatedAt = updatedAt;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getProcessId() {
        return processId;
    }

    public String getProcessVersionId() {
        return processVersionId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getNodeLabel() {
        return nodeLabel;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getAssignedUserId() {
        return assignedUserId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getClaimedAt() {
        return claimedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}