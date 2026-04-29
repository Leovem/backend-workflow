package com.workflow.backend.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class WorkflowMonitorResponse {

    private String processVersionId;

    private long totalTasks;
    private long newTasks;
    private long inProgressTasks;
    private long completedTasks;
    private long cancelledTasks;

    private LocalDateTime generatedAt;

    private List<WorkflowMonitorTaskResponse> tasks;

    public WorkflowMonitorResponse() {}

    public WorkflowMonitorResponse(
        String processVersionId,
        long totalTasks,
        long newTasks,
        long inProgressTasks,
        long completedTasks,
        long cancelledTasks,
        LocalDateTime generatedAt,
        List<WorkflowMonitorTaskResponse> tasks
    ) {
        this.processVersionId = processVersionId;
        this.totalTasks = totalTasks;
        this.newTasks = newTasks;
        this.inProgressTasks = inProgressTasks;
        this.completedTasks = completedTasks;
        this.cancelledTasks = cancelledTasks;
        this.generatedAt = generatedAt;
        this.tasks = tasks;
    }

    public String getProcessVersionId() {
        return processVersionId;
    }

    public long getTotalTasks() {
        return totalTasks;
    }

    public long getNewTasks() {
        return newTasks;
    }

    public long getInProgressTasks() {
        return inProgressTasks;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }

    public long getCancelledTasks() {
        return cancelledTasks;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public List<WorkflowMonitorTaskResponse> getTasks() {
        return tasks;
    }
}