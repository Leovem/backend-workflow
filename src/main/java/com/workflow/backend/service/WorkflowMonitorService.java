package com.workflow.backend.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.workflow.backend.DTO.WorkflowMonitorResponse;
import com.workflow.backend.DTO.WorkflowMonitorTaskResponse;
import com.workflow.backend.model.WorkflowTask;
import com.workflow.backend.repository.WorkflowTaskRepository;

@Service
public class WorkflowMonitorService {

    private final WorkflowTaskRepository workflowTaskRepository;

    public WorkflowMonitorService(
        WorkflowTaskRepository workflowTaskRepository
    ) {
        this.workflowTaskRepository = workflowTaskRepository;
    }

    public WorkflowMonitorResponse getMonitorByProcessVersion(
        String processVersionId
    ) {
        if (!StringUtils.hasText(processVersionId)) {
            throw new IllegalArgumentException("processVersionId es obligatorio.");
        }

        List<WorkflowTask> tasks = workflowTaskRepository
            .findByProcessVersionId(processVersionId);

        List<WorkflowMonitorTaskResponse> taskResponses = tasks.stream()
            .sorted(
                Comparator.comparing(
                    WorkflowTask::getCreatedAt,
                    Comparator.nullsLast(Comparator.reverseOrder())
                )
            )
            .map(this::mapTask)
            .toList();

        long total = tasks.size();
        long newCount = countByStatus(tasks, "NEW");
        long inProgressCount = countByStatus(tasks, "IN_PROGRESS");
        long completedCount = countByStatus(tasks, "COMPLETED");
        long cancelledCount = countByStatus(tasks, "CANCELLED");

        return new WorkflowMonitorResponse(
            processVersionId,
            total,
            newCount,
            inProgressCount,
            completedCount,
            cancelledCount,
            LocalDateTime.now(),
            taskResponses
        );
    }

    private long countByStatus(List<WorkflowTask> tasks, String status) {
        return tasks.stream()
            .filter(task -> status.equals(task.getStatus()))
            .count();
    }

    private WorkflowMonitorTaskResponse mapTask(WorkflowTask task) {
        return new WorkflowMonitorTaskResponse(
            task.getId(),
            task.getInstanceId(),
            task.getProcessId(),
            task.getProcessVersionId(),
            task.getNodeId(),
            task.getNodeLabel(),
            task.getDepartmentId(),
            task.getDepartmentName(),
            task.getAssignedUserId(),
            task.getStatus(),
            task.getCreatedAt(),
            task.getClaimedAt(),
            task.getCompletedAt(),
            task.getUpdatedAt()
        );
    }
}