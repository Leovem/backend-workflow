package com.workflow.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;    
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.backend.DTO.BulkWorkflowFormRequest;
import com.workflow.backend.DTO.UpdateWorkflowFormRequest;
import com.workflow.backend.model.WorkflowForm;
import com.workflow.backend.service.WorkflowFormService;

@RestController
@RequestMapping("/api/workflow-forms")
public class WorkflowFormController {

    private final WorkflowFormService workflowFormService;

    public WorkflowFormController(WorkflowFormService workflowFormService) {
        this.workflowFormService = workflowFormService;
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<WorkflowForm>> saveBulk(
        @RequestBody BulkWorkflowFormRequest request
    ) {
        return ResponseEntity.ok(workflowFormService.saveBulk(request));
    }

    @GetMapping("/process-version/{processVersionId}")
    public ResponseEntity<List<WorkflowForm>> findByProcessVersion(
        @PathVariable String processVersionId
    ) {
        return ResponseEntity.ok(
            workflowFormService.findByProcessVersion(processVersionId)
        );
    }

    @GetMapping("/process-version/{processVersionId}/node/{nodeId}")
    public ResponseEntity<List<WorkflowForm>> findByProcessVersionAndNode(
        @PathVariable String processVersionId,
        @PathVariable String nodeId
    ) {
        return ResponseEntity.ok(
            workflowFormService.findByProcessVersionAndNode(
                processVersionId,
                nodeId
            )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowForm> findById(@PathVariable String id) {
        return workflowFormService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
public ResponseEntity<WorkflowForm> updateForm(
    @PathVariable String id,
    @RequestBody UpdateWorkflowFormRequest request
) {
    return ResponseEntity.ok(
        workflowFormService.updateForm(id, request)
    );
}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        workflowFormService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}