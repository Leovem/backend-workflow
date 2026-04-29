package com.workflow.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;    
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.backend.DTO.SaveWorkflowFormSubmissionRequest;
import com.workflow.backend.model.WorkflowFormSubmission;
import com.workflow.backend.service.WorkflowFormSubmissionService;

@RestController
@RequestMapping("/api/workflow-form-submissions")
public class WorkflowFormSubmissionController {

    private final WorkflowFormSubmissionService submissionService;

    public WorkflowFormSubmissionController(
        WorkflowFormSubmissionService submissionService
    ) {
        this.submissionService = submissionService;
    }

    @PostMapping
    public ResponseEntity<WorkflowFormSubmission> saveSubmission(
        @RequestBody SaveWorkflowFormSubmissionRequest request
    ) {
        return ResponseEntity.ok(
            submissionService.saveSubmission(request)
        );
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<WorkflowFormSubmission> findByTaskId(
        @PathVariable String taskId
    ) {
        WorkflowFormSubmission submission = submissionService.findByTaskId(taskId);

        if (submission == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(submission);
    }
}