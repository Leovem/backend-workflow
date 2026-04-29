package com.workflow.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;    
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.backend.DTO.StartWorkflowInstanceRequest;
import com.workflow.backend.DTO.StartWorkflowInstanceResponse;
import com.workflow.backend.model.WorkflowTask;
import com.workflow.backend.repository.WorkflowTaskRepository;
import com.workflow.backend.service.WorkflowInstanceService;

@RestController
@RequestMapping("/api/workflow-instances")
public class WorkflowInstanceController {

    private final WorkflowInstanceService workflowInstanceService;
    private final WorkflowTaskRepository workflowTaskRepository;

    public WorkflowInstanceController(
        WorkflowInstanceService workflowInstanceService,
        WorkflowTaskRepository workflowTaskRepository
    ) {
        this.workflowInstanceService = workflowInstanceService;
        this.workflowTaskRepository = workflowTaskRepository;
    }

    @PostMapping("/start")
public ResponseEntity<StartWorkflowInstanceResponse> startInstance(
    @RequestBody StartWorkflowInstanceRequest request
) {
    System.out.println("=== [INSTANCE] Iniciando trámite ===");
    System.out.println("processId: " + request.getProcessId());
    System.out.println("processVersionId: " + request.getProcessVersionId());
    System.out.println("title: " + request.getTitle());

    StartWorkflowInstanceResponse response =
        workflowInstanceService.startInstance(request);

    System.out.println("=== [INSTANCE] Trámite creado correctamente ===");
    System.out.println("instanceId: " + response.getId());
    System.out.println("trackingCode: " + response.getTrackingCode());

    return ResponseEntity.ok(response);
}

    @GetMapping("/{instanceId}/tasks")
    public ResponseEntity<List<WorkflowTask>> findTasksByInstance(
        @PathVariable String instanceId
    ) {
        return ResponseEntity.ok(
            workflowTaskRepository.findByInstanceId(instanceId)
        );
    }
}