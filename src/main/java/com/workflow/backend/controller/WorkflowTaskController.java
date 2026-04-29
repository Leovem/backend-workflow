package com.workflow.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;    
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.backend.model.WorkflowTask;
import com.workflow.backend.service.WorkflowTaskService;

@RestController
@RequestMapping("/api/workflow-tasks")
public class WorkflowTaskController {

    private final WorkflowTaskService workflowTaskService;

    public WorkflowTaskController(WorkflowTaskService workflowTaskService) {
        this.workflowTaskService = workflowTaskService;
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<WorkflowTask>> findByDepartment(
        @PathVariable String departmentId
    ) {
        return ResponseEntity.ok(
            workflowTaskService.findActiveTasksByDepartment(departmentId)
        );
    }

    @GetMapping("/assigned/{userId}")
    public ResponseEntity<List<WorkflowTask>> findAssignedToUser(
        @PathVariable String userId
    ) {
        return ResponseEntity.ok(
            workflowTaskService.findActiveTasksByAssignedUser(userId)
        );
    }

    @PostMapping("/{taskId}/claim/{userId}")
    public ResponseEntity<WorkflowTask> claimTask(
        @PathVariable String taskId,
        @PathVariable String userId
    ) {
        return ResponseEntity.ok(
            workflowTaskService.claimTask(taskId, userId)
        );
    }

    @PostMapping("/{taskId}/complete/{userId}")
public ResponseEntity<WorkflowTask> completeTask(
    @PathVariable String taskId,
    @PathVariable String userId
) {
    return ResponseEntity.ok(
        workflowTaskService.completeTask(taskId, userId)
    );
}
}