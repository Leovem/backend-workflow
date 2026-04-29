package com.workflow.backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.workflow.backend.DTO.BulkWorkflowFormRequest;
import com.workflow.backend.DTO.UpdateWorkflowFormRequest;
import com.workflow.backend.DTO.WorkflowFormFieldRequest;
import com.workflow.backend.DTO.WorkflowFormRequest;
import com.workflow.backend.model.WorkflowForm;
import com.workflow.backend.model.WorkflowFormField;
import com.workflow.backend.repository.WorkflowFormRepository;

@Service
public class WorkflowFormService {

    private final WorkflowFormRepository workflowFormRepository;

    public WorkflowFormService(WorkflowFormRepository workflowFormRepository) {
        this.workflowFormRepository = workflowFormRepository;
    }

    public List<WorkflowForm> saveBulk(BulkWorkflowFormRequest request) {
        validateBulkRequest(request);

        List<WorkflowForm> savedForms = new ArrayList<>();

        for (WorkflowFormRequest formRequest : request.getForms()) {
            validateFormRequest(formRequest);

            WorkflowForm form = workflowFormRepository
                .findByProcessVersionIdAndNodeId(
                    request.getProcessVersionId(),
                    formRequest.getNodeId()
                )
                .orElseGet(() -> createNewForm(request, formRequest));

            form.setProcessId(request.getProcessId());
            form.setProcessVersionId(request.getProcessVersionId());
            form.setNodeId(formRequest.getNodeId());

            form.setTitle(safeText(formRequest.getTitle(), "Formulario de actividad"));
            form.setDescription(safeText(formRequest.getDescription(), ""));
            form.setGeneratedByAI(Boolean.TRUE.equals(formRequest.getGeneratedByAI()));

            form.setFields(mapFields(formRequest.getFields()));
            form.setUpdatedAt(LocalDateTime.now());

            savedForms.add(workflowFormRepository.save(form));
        }

        return savedForms;
    }

    private WorkflowForm createNewForm(
        BulkWorkflowFormRequest request,
        WorkflowFormRequest formRequest
    ) {
        WorkflowForm form = new WorkflowForm();

        form.setProcessId(request.getProcessId());
        form.setProcessVersionId(request.getProcessVersionId());
        form.setNodeId(formRequest.getNodeId());
        form.setCreatedAt(LocalDateTime.now());

        return form;
    }

    private void validateBulkRequest(BulkWorkflowFormRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula.");
        }

        if (!StringUtils.hasText(request.getProcessId())) {
            throw new IllegalArgumentException("processId es obligatorio.");
        }

        if (!StringUtils.hasText(request.getProcessVersionId())) {
            throw new IllegalArgumentException("processVersionId es obligatorio.");
        }

        if (request.getForms() == null || request.getForms().isEmpty()) {
            throw new IllegalArgumentException("Debe enviar al menos un formulario.");
        }
    }

    private void validateFormRequest(WorkflowFormRequest formRequest) {
        if (formRequest == null) {
            throw new IllegalArgumentException("El formulario no puede ser nulo.");
        }

        if (!StringUtils.hasText(formRequest.getNodeId())) {
            throw new IllegalArgumentException("nodeId es obligatorio en cada formulario.");
        }
    }

    private List<WorkflowFormField> mapFields(List<WorkflowFormFieldRequest> requests) {
    if (requests == null || requests.isEmpty()) {
        return new ArrayList<>();
    }

    List<WorkflowFormField> fields = new ArrayList<>();
    int index = 1;

    for (WorkflowFormFieldRequest request : requests) {
        if (request == null) {
            continue;
        }

        WorkflowFormField field = new WorkflowFormField();

        field.setId(StringUtils.hasText(request.getId())? request.getId(): UUID.randomUUID().toString());
        field.setLabel(safeText(request.getLabel(), "Campo sin nombre"));
        field.setType(normalizeFieldType(request.getType()));
        field.setRequired(Boolean.TRUE.equals(request.getRequired()));
        field.setPlaceholder(safeText(request.getPlaceholder(), ""));
        field.setOptions(
            request.getOptions() != null ? request.getOptions() : new ArrayList<>()
        );

        Integer order = request.getOrder();
        field.setOrder(order != null ? order : index);

        fields.add(field);
        index++;
    }

    return fields;
}

    private String normalizeFieldType(String type) {
        if (!StringUtils.hasText(type)) {
            return "text";
        }

        String normalized = type.trim().toLowerCase();

        return switch (normalized) {
            case "text", "textarea", "number", "date", "select", "checkbox", "file", "image" -> normalized;
            default -> "text";
        };
    }

    private String safeText(String value, String fallback) {
        return StringUtils.hasText(value) ? value.trim() : fallback;
    }

    public List<WorkflowForm> findByProcessVersion(String processVersionId) {
        return workflowFormRepository.findByProcessVersionId(processVersionId);
    }

    public Optional<WorkflowForm> findById(String id) {
        return workflowFormRepository.findById(id);
    }

    public List<WorkflowForm> findByProcessVersionAndNode(
    String processVersionId,
    String nodeId
) {
    return workflowFormRepository.findAllByProcessVersionIdAndNodeId(
        processVersionId,
        nodeId
    );
}

public void deleteById(String id) {
    workflowFormRepository.deleteById(id);
}

public WorkflowForm updateForm(String id, UpdateWorkflowFormRequest request) {
    if (!StringUtils.hasText(id)) {
        throw new IllegalArgumentException("El id del formulario es obligatorio.");
    }

    if (request == null) {
        throw new IllegalArgumentException("La solicitud no puede ser nula.");
    }

    WorkflowForm form = workflowFormRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("El formulario no existe."));

    form.setTitle(safeText(request.getTitle(), form.getTitle()));
    form.setDescription(safeText(request.getDescription(), ""));
    form.setGeneratedByAI(Boolean.TRUE.equals(request.getGeneratedByAI()));

    form.setFields(mapFields(request.getFields()));
    form.setUpdatedAt(LocalDateTime.now());

    return workflowFormRepository.save(form);
}
}