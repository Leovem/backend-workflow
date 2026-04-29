package com.workflow.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.workflow.backend.DTO.CreateWorkflowProjectRequest;
import com.workflow.backend.DTO.JoinWorkflowProjectRequest;
import com.workflow.backend.DTO.UpdateDiagramSnapshotRequest;
import com.workflow.backend.DTO.WorkflowProjectResponse;
import com.workflow.backend.service.WorkflowProjectService;

@RestController
@RequestMapping("/api/workflow-projects")
@CrossOrigin
public class WorkflowProjectController {

    private final WorkflowProjectService service;

    public WorkflowProjectController(WorkflowProjectService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<WorkflowProjectResponse> create(
            @RequestBody CreateWorkflowProjectRequest request,
            Authentication authentication
    ) {
        String userId = authentication.getName();

        return ResponseEntity.ok(service.create(request, userId));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<WorkflowProjectResponse>> findAllForAdmin(
            Authentication authentication
    ) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(service.findAllForAdmin());
    }

    @GetMapping("/my")
    public ResponseEntity<List<WorkflowProjectResponse>> findMyProjects(
            Authentication authentication
    ) {
        String userId = authentication.getName();

        return ResponseEntity.ok(service.findMyProjects(userId));
    }

    @PostMapping("/join")
    public ResponseEntity<WorkflowProjectResponse> joinByCode(
            @RequestBody JoinWorkflowProjectRequest request,
            Authentication authentication
    ) {
        String userId = authentication.getName();

        return ResponseEntity.ok(service.joinByCode(request, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowProjectResponse> findById(
            @PathVariable String id,
            Authentication authentication
    ) {
        String userId = authentication.getName();
        boolean isAdmin = isAdmin(authentication);

        return ResponseEntity.ok(service.findById(id, userId, isAdmin));
    }

    @PutMapping("/{id}/snapshot")
    public ResponseEntity<WorkflowProjectResponse> updateSnapshot(
            @PathVariable String id,
            @RequestBody UpdateDiagramSnapshotRequest request,
            Authentication authentication
    ) {
        String userId = authentication.getName();
        boolean isAdmin = isAdmin(authentication);

        return ResponseEntity.ok(service.updateSnapshot(id, request, userId, isAdmin));
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<WorkflowProjectResponse> archive(
            @PathVariable String id,
            Authentication authentication
    ) {
        String userId = authentication.getName();
        boolean isAdmin = isAdmin(authentication);

        return ResponseEntity.ok(service.archive(id, userId, isAdmin));
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .anyMatch(auth ->
                        auth.getAuthority().equals("ROLE_ADMIN") ||
                        auth.getAuthority().equals("ADMIN")
                );
    }
}