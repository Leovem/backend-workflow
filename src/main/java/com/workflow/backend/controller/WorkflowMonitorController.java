package com.workflow.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;    
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.backend.DTO.WorkflowMonitorResponse;
import com.workflow.backend.service.WorkflowMonitorService;

@RestController
@RequestMapping("/api/workflow-monitor")
public class WorkflowMonitorController {

    private final WorkflowMonitorService workflowMonitorService;

    public WorkflowMonitorController(
        WorkflowMonitorService workflowMonitorService
    ) {
        this.workflowMonitorService = workflowMonitorService;
    }

    @GetMapping("/process-version/{processVersionId}")
    public ResponseEntity<WorkflowMonitorResponse> getMonitorByProcessVersion(
        @PathVariable String processVersionId
    ) {
        return ResponseEntity.ok(
            workflowMonitorService.getMonitorByProcessVersion(processVersionId)
        );
    }
}