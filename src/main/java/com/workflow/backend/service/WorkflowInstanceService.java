package com.workflow.backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.workflow.backend.DTO.StartWorkflowInstanceRequest;
import com.workflow.backend.DTO.StartWorkflowInstanceResponse;
import com.workflow.backend.model.WorkflowInstance;
import com.workflow.backend.model.WorkflowProcessVersion;
import com.workflow.backend.model.WorkflowTask;
import com.workflow.backend.repository.WorkflowInstanceRepository;
import com.workflow.backend.repository.WorkflowProcessVersionRepository;
import com.workflow.backend.repository.WorkflowTaskRepository;

@Service
public class WorkflowInstanceService {

    private final WorkflowInstanceRepository instanceRepository;
    private final WorkflowTaskRepository taskRepository;
    private final WorkflowProcessVersionRepository versionRepository;

    public WorkflowInstanceService(
        WorkflowInstanceRepository instanceRepository,
        WorkflowTaskRepository taskRepository,
        WorkflowProcessVersionRepository versionRepository
    ) {
        this.instanceRepository = instanceRepository;
        this.taskRepository = taskRepository;
        this.versionRepository = versionRepository;
    }

    public StartWorkflowInstanceResponse startInstance(
        StartWorkflowInstanceRequest request
    ) {
        validateStartRequest(request);

        WorkflowProcessVersion version = versionRepository
            .findById(request.getProcessVersionId())
            .orElseThrow(() -> new IllegalArgumentException("La versión del proceso no existe."));

        if (!"PUBLISHED".equals(version.getStatus())) {
            throw new IllegalStateException("Solo se pueden iniciar trámites desde una versión publicada.");
        }

        Map<String, Object> graphJson = version.getGraphJson();

        if (graphJson == null || !graphJson.containsKey("cells")) {
            throw new IllegalStateException("La versión publicada no tiene un diagrama válido.");
        }

        WorkflowGraphRuntime runtime = parseGraphRuntime(graphJson);

        Map<String, Object> initialNode = runtime.findInitialNode();

        if (initialNode == null) {
            throw new IllegalStateException("El workflow no tiene nodo inicial.");
        }

        Map<String, Object> firstActionNode = runtime.findNextActionNodeFrom(initialNode);

        if (firstActionNode == null) {
            throw new IllegalStateException("No se encontró una primera actividad ejecutable desde el nodo inicial.");
        }

        LocalDateTime now = LocalDateTime.now();

        WorkflowInstance instance = new WorkflowInstance();
        instance.setTrackingCode(generateTrackingCode());
        instance.setProcessId(request.getProcessId());
        instance.setProcessVersionId(request.getProcessVersionId());
        instance.setTitle(safeText(request.getTitle(), "Trámite sin título"));
        instance.setStatus("IN_PROGRESS");
        instance.setCurrentNodeId(String.valueOf(firstActionNode.get("id")));
        instance.setData(request.getInitialData());
        instance.setCreatedAt(now);
        instance.setUpdatedAt(now);

        WorkflowInstance savedInstance = instanceRepository.save(instance);

        WorkflowTask firstTask = createTaskFromActionNode(
            savedInstance,
            firstActionNode,
            now
        );

        WorkflowTask savedTask = taskRepository.save(firstTask);

        List<WorkflowTask> tasks = new ArrayList<>();
        tasks.add(savedTask);

        return new StartWorkflowInstanceResponse(
            savedInstance.getId(),
            savedInstance.getTrackingCode(),
            savedInstance.getProcessId(),
            savedInstance.getProcessVersionId(),
            savedInstance.getTitle(),
            savedInstance.getStatus(),
            savedInstance.getCurrentNodeId(),
            savedInstance.getCreatedAt(),
            tasks
        );
    }

    private WorkflowTask createTaskFromActionNode(
        WorkflowInstance instance,
        Map<String, Object> node,
        LocalDateTime now
    ) {
        WorkflowTask task = new WorkflowTask();

        task.setInstanceId(instance.getId());
        task.setProcessId(instance.getProcessId());
        task.setProcessVersionId(instance.getProcessVersionId());

        task.setNodeId(String.valueOf(node.get("id")));
        task.setNodeLabel(extractNodeLabel(node));

        Map<String, Object> businessConfig = asMap(node.get("businessConfig"));
        Map<String, Object> assignment = asMap(
            businessConfig != null ? businessConfig.get("assignment") : null
        );
        Map<String, Object> form = asMap(
            businessConfig != null ? businessConfig.get("form") : null
        );

        if (assignment != null) {
            task.setDepartmentId(asString(assignment.get("departmentId")));
            task.setRoleId(asString(assignment.get("roleId")));
        }

        if (form != null) {
            task.setFormId(asString(form.get("formId")));
        }

        task.setStatus("NEW");
        task.setCreatedAt(now);
        task.setUpdatedAt(now);

        return task;
    }

    private void validateStartRequest(StartWorkflowInstanceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula.");
        }

        if (!StringUtils.hasText(request.getProcessId())) {
            throw new IllegalArgumentException("processId es obligatorio.");
        }

        if (!StringUtils.hasText(request.getProcessVersionId())) {
            throw new IllegalArgumentException("processVersionId es obligatorio.");
        }
    }

    private String generateTrackingCode() {
        long count = instanceRepository.count() + 1;
        return String.format("TRAM-%06d", count);
    }

    private String safeText(String value, String fallback) {
        return StringUtils.hasText(value) ? value.trim() : fallback;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> asMap(Object value) {
        if (value instanceof Map<?, ?>) {
            return (Map<String, Object>) value;
        }

        return null;
    }

    private String asString(Object value) {
        return value != null ? String.valueOf(value) : null;
    }

    private String extractNodeLabel(Map<String, Object> node) {
        Map<String, Object> attrs = asMap(node.get("attrs"));

        if (attrs == null) {
            return "Actividad sin nombre";
        }

        Map<String, Object> label = asMap(attrs.get("label"));

        if (label != null && label.get("text") != null) {
            return String.valueOf(label.get("text"));
        }

        Map<String, Object> text = asMap(attrs.get("text"));

        if (text != null && text.get("text") != null) {
            return String.valueOf(text.get("text"));
        }

        return "Actividad sin nombre";
    }

    private WorkflowGraphRuntime parseGraphRuntime(Map<String, Object> graphJson) {
        return new WorkflowGraphRuntime(graphJson);
    }
}