package com.workflow.backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.workflow.backend.model.WorkflowInstance;
import com.workflow.backend.model.WorkflowProcessVersion;
import com.workflow.backend.model.WorkflowTask;
import com.workflow.backend.repository.WorkflowInstanceRepository;
import com.workflow.backend.repository.WorkflowProcessVersionRepository;
import com.workflow.backend.repository.WorkflowTaskRepository;

@Service
public class WorkflowTaskService {

    private final WorkflowTaskRepository workflowTaskRepository;
    private final WorkflowInstanceRepository workflowInstanceRepository;
    private final WorkflowProcessVersionRepository workflowProcessVersionRepository;

    public WorkflowTaskService(
        WorkflowTaskRepository workflowTaskRepository,
        WorkflowInstanceRepository workflowInstanceRepository,
        WorkflowProcessVersionRepository workflowProcessVersionRepository
    ) {
        this.workflowTaskRepository = workflowTaskRepository;
        this.workflowInstanceRepository = workflowInstanceRepository;
        this.workflowProcessVersionRepository = workflowProcessVersionRepository;
    }

    public List<WorkflowTask> findActiveTasksByDepartment(String departmentId) {
        if (!StringUtils.hasText(departmentId)) {
            throw new IllegalArgumentException("departmentId es obligatorio.");
        }

        List<WorkflowTask> result = new ArrayList<>();

        result.addAll(
            workflowTaskRepository.findByDepartmentIdAndStatus(
                departmentId,
                "NEW"
            )
        );

        result.addAll(
            workflowTaskRepository.findByDepartmentIdAndStatus(
                departmentId,
                "IN_PROGRESS"
            )
        );

        return result;
    }

    public List<WorkflowTask> findActiveTasksByAssignedUser(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("userId es obligatorio.");
        }

        List<WorkflowTask> result = new ArrayList<>();

        result.addAll(
            workflowTaskRepository.findByAssignedUserIdAndStatus(
                userId,
                "IN_PROGRESS"
            )
        );

        result.addAll(
            workflowTaskRepository.findByAssignedUserIdAndStatus(
                userId,
                "NEW"
            )
        );

        return result;
    }

    public WorkflowTask claimTask(String taskId, String userId) {
        if (!StringUtils.hasText(taskId)) {
            throw new IllegalArgumentException("taskId es obligatorio.");
        }

        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("userId es obligatorio.");
        }

        WorkflowTask task = workflowTaskRepository.findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("La tarea no existe."));

        if (!"NEW".equals(task.getStatus())) {
            throw new IllegalStateException("Solo se pueden tomar tareas en estado NEW.");
        }

        if (StringUtils.hasText(task.getAssignedUserId())) {
            throw new IllegalStateException("La tarea ya está asignada a un funcionario.");
        }

        LocalDateTime now = LocalDateTime.now();

        task.setAssignedUserId(userId);
        task.setStatus("IN_PROGRESS");
        task.setClaimedAt(now);
        task.setUpdatedAt(now);

        return workflowTaskRepository.save(task);
    }

    public WorkflowTask completeTask(String taskId, String userId) {
        if (!StringUtils.hasText(taskId)) {
            throw new IllegalArgumentException("taskId es obligatorio.");
        }

        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("userId es obligatorio.");
        }

        WorkflowTask task = workflowTaskRepository.findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("La tarea no existe."));

        if (!"IN_PROGRESS".equals(task.getStatus())) {
            throw new IllegalStateException("Solo se pueden completar tareas en estado IN_PROGRESS.");
        }

        if (
            StringUtils.hasText(task.getAssignedUserId())
                && !task.getAssignedUserId().equals(userId)
        ) {
            throw new IllegalStateException("La tarea está asignada a otro funcionario.");
        }

        WorkflowInstance instance = workflowInstanceRepository.findById(task.getInstanceId())
            .orElseThrow(() -> new IllegalArgumentException("La instancia del trámite no existe."));

        WorkflowProcessVersion version = workflowProcessVersionRepository
            .findById(task.getProcessVersionId())
            .orElseThrow(() -> new IllegalArgumentException("La versión del proceso no existe."));

        Map<String, Object> graphJson = version.getGraphJson();

        if (graphJson == null || !graphJson.containsKey("cells")) {
            throw new IllegalStateException("La versión del proceso no tiene un diagrama válido.");
        }

        LocalDateTime now = LocalDateTime.now();

        task.setStatus("COMPLETED");
        task.setCompletedAt(now);
        task.setUpdatedAt(now);

        WorkflowTask completedTask = workflowTaskRepository.save(task);

        WorkflowGraphRuntime runtime = new WorkflowGraphRuntime(graphJson);

        Map<String, Object> nextActionNode = runtime.findNextActionNodeAfter(task.getNodeId());

        if (nextActionNode == null) {
            instance.setStatus("COMPLETED");
            instance.setCurrentNodeId(null);
            instance.setCompletedAt(now);
            instance.setUpdatedAt(now);

            workflowInstanceRepository.save(instance);

            return completedTask;
        }

        WorkflowTask nextTask = createTaskFromActionNode(
            instance,
            nextActionNode,
            now
        );

        workflowTaskRepository.save(nextTask);

        instance.setStatus("IN_PROGRESS");
        instance.setCurrentNodeId(String.valueOf(nextActionNode.get("id")));
        instance.setUpdatedAt(now);

        workflowInstanceRepository.save(instance);

        return completedTask;
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
            task.setDepartmentName(asString(assignment.get("departmentName")));
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
}