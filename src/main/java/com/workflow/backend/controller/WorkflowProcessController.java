package com.workflow.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;    
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.backend.DTO.CreateWorkflowProcessRequest;
import com.workflow.backend.DTO.SaveWorkflowProcessVersionRequest;
import com.workflow.backend.DTO.WorkflowProcessEditorResponse;
import com.workflow.backend.model.WorkflowProcessVersion;
import com.workflow.backend.service.WorkflowProcessService;

@RestController
@RequestMapping("/api/workflow-processes")
public class WorkflowProcessController {

    private final WorkflowProcessService workflowProcessService;

    public WorkflowProcessController(
        WorkflowProcessService workflowProcessService
    ) {
        this.workflowProcessService = workflowProcessService;
    }

    @PostMapping
    public ResponseEntity<WorkflowProcessEditorResponse> createProcess(
        @RequestBody CreateWorkflowProcessRequest request
    ) {
        return ResponseEntity.ok(
            workflowProcessService.createProcess(request)
        );
    }

    @GetMapping("/{processId}/editor")
    public ResponseEntity<WorkflowProcessEditorResponse> findEditorData(
        @PathVariable String processId
    ) {
        return ResponseEntity.ok(
            workflowProcessService.findEditorData(processId)
        );
    }

    @PutMapping("/versions/{processVersionId}/draft")
    public ResponseEntity<WorkflowProcessVersion> saveDraftVersion(
        @PathVariable String processVersionId,
        @RequestBody SaveWorkflowProcessVersionRequest request
    ) {
        return ResponseEntity.ok(
            workflowProcessService.saveDraftVersion(processVersionId, request)
        );
    }

    @PostMapping("/versions/{processVersionId}/publish")
public ResponseEntity<WorkflowProcessVersion> publishVersion(
    @PathVariable String processVersionId
) {
    return ResponseEntity.ok(
        workflowProcessService.publishVersion(processVersionId)
    );
}
}