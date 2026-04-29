package com.workflow.backend.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.workflow.backend.DTO.SaveWorkflowFormSubmissionRequest;
import com.workflow.backend.model.WorkflowFormSubmission;
import com.workflow.backend.model.WorkflowTask;
import com.workflow.backend.repository.WorkflowFormSubmissionRepository;
import com.workflow.backend.repository.WorkflowTaskRepository;

@Service
public class WorkflowFormSubmissionService {

    private final WorkflowFormSubmissionRepository submissionRepository;
    private final WorkflowTaskRepository taskRepository;

    public WorkflowFormSubmissionService(
        WorkflowFormSubmissionRepository submissionRepository,
        WorkflowTaskRepository taskRepository
    ) {
        this.submissionRepository = submissionRepository;
        this.taskRepository = taskRepository;
    }

    public WorkflowFormSubmission saveSubmission(
        SaveWorkflowFormSubmissionRequest request
    ) {
        validateRequest(request);

        WorkflowTask task = taskRepository.findById(request.getTaskId())
            .orElseThrow(() -> new IllegalArgumentException("La tarea no existe."));

        if (!"IN_PROGRESS".equals(task.getStatus())) {
            throw new IllegalStateException("Solo se puede registrar formulario en tareas en proceso.");
        }

        if (StringUtils.hasText(task.getAssignedUserId())
            && !task.getAssignedUserId().equals(request.getUserId())) {
            throw new IllegalStateException("La tarea está asignada a otro funcionario.");
        }

        if (!StringUtils.hasText(task.getFormId())) {
            throw new IllegalStateException("La tarea no tiene formulario asociado.");
        }

        LocalDateTime now = LocalDateTime.now();

        WorkflowFormSubmission submission = submissionRepository
            .findByTaskId(task.getId())
            .orElseGet(WorkflowFormSubmission::new);

        if (submission.getSubmittedAt() == null) {
            submission.setSubmittedAt(now);
        }

        submission.setTaskId(task.getId());
        submission.setInstanceId(task.getInstanceId());
        submission.setProcessId(task.getProcessId());
        submission.setProcessVersionId(task.getProcessVersionId());
        submission.setNodeId(task.getNodeId());
        submission.setFormId(task.getFormId());
        submission.setSubmittedByUserId(request.getUserId());
        submission.setValues(request.getValues());
        submission.setUpdatedAt(now);

        return submissionRepository.save(submission);
    }

    public WorkflowFormSubmission findByTaskId(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new IllegalArgumentException("taskId es obligatorio.");
        }

        return submissionRepository.findByTaskId(taskId).orElse(null);
    }

    private void validateRequest(SaveWorkflowFormSubmissionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula.");
        }

        if (!StringUtils.hasText(request.getTaskId())) {
            throw new IllegalArgumentException("taskId es obligatorio.");
        }

        if (!StringUtils.hasText(request.getUserId())) {
            throw new IllegalArgumentException("userId es obligatorio.");
        }

        Map<String, Object> values = request.getValues();

        if (values == null) {
            throw new IllegalArgumentException("values es obligatorio.");
        }
    }
}