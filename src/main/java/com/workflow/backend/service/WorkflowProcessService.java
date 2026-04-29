package com.workflow.backend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.workflow.backend.DTO.CreateWorkflowProcessRequest;
import com.workflow.backend.DTO.SaveWorkflowProcessVersionRequest;
import com.workflow.backend.DTO.WorkflowProcessEditorResponse;
import com.workflow.backend.model.WorkflowProcess;
import com.workflow.backend.model.WorkflowProcessVersion;
import com.workflow.backend.repository.WorkflowProcessRepository;
import com.workflow.backend.repository.WorkflowProcessVersionRepository;

@Service
public class WorkflowProcessService {

    private final WorkflowProcessRepository processRepository;
    private final WorkflowProcessVersionRepository versionRepository;

    public WorkflowProcessService(
        WorkflowProcessRepository processRepository,
        WorkflowProcessVersionRepository versionRepository
    ) {
        this.processRepository = processRepository;
        this.versionRepository = versionRepository;
    }

    public WorkflowProcessEditorResponse createProcess(
        CreateWorkflowProcessRequest request
    ) {
        validateCreateRequest(request);

        LocalDateTime now = LocalDateTime.now();

        WorkflowProcess process = new WorkflowProcess();
        process.setName(request.getName().trim());
        process.setDescription(safeText(request.getDescription(), ""));
        process.setStatus("ACTIVE");
        process.setCreatedAt(now);
        process.setUpdatedAt(now);

        WorkflowProcess savedProcess = processRepository.save(process);

        WorkflowProcessVersion version = new WorkflowProcessVersion();
        version.setProcessId(savedProcess.getId());
        version.setVersionNumber(1);
        version.setStatus("DRAFT");
        version.setGraphJson(null);
        version.setCreatedAt(now);
        version.setUpdatedAt(now);

        WorkflowProcessVersion savedVersion = versionRepository.save(version);

        savedProcess.setCurrentDraftVersionId(savedVersion.getId());
        savedProcess.setUpdatedAt(LocalDateTime.now());

        processRepository.save(savedProcess);

        return new WorkflowProcessEditorResponse(
            savedProcess.getId(),
            savedVersion.getId(),
            savedProcess.getName(),
            savedProcess.getDescription(),
            savedVersion.getVersionNumber(),
            savedVersion.getStatus(),
            savedVersion.getGraphJson()
        );
    }

    public WorkflowProcessVersion saveDraftVersion(
        String processVersionId,
        SaveWorkflowProcessVersionRequest request
    ) {
        if (request == null || request.getGraphJson() == null) {
            throw new IllegalArgumentException("graphJson es obligatorio.");
        }

        WorkflowProcessVersion version = versionRepository.findById(processVersionId)
            .orElseThrow(() -> new IllegalArgumentException("La versión del proceso no existe."));

        if (!"DRAFT".equals(version.getStatus())) {
            throw new IllegalStateException("Solo se pueden editar versiones en estado DRAFT.");
        }

        version.setGraphJson(request.getGraphJson());
        version.setUpdatedAt(LocalDateTime.now());

        return versionRepository.save(version);
    }

    public WorkflowProcessEditorResponse findEditorData(String processId) {
        WorkflowProcess process = processRepository.findById(processId)
            .orElseThrow(() -> new IllegalArgumentException("El proceso no existe."));

        if (!StringUtils.hasText(process.getCurrentDraftVersionId())) {
            throw new IllegalStateException("El proceso no tiene una versión borrador.");
        }

        WorkflowProcessVersion version = versionRepository
            .findById(process.getCurrentDraftVersionId())
            .orElseThrow(() -> new IllegalArgumentException("La versión borrador no existe."));

        return new WorkflowProcessEditorResponse(
            process.getId(),
            version.getId(),
            process.getName(),
            process.getDescription(),
            version.getVersionNumber(),
            version.getStatus(),
            version.getGraphJson()
        );
    }

    private void validateCreateRequest(CreateWorkflowProcessRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula.");
        }

        if (!StringUtils.hasText(request.getName())) {
            throw new IllegalArgumentException("El nombre del proceso es obligatorio.");
        }
    }

    private String safeText(String value, String fallback) {
        return StringUtils.hasText(value) ? value.trim() : fallback;
    }

    public WorkflowProcessVersion publishVersion(String processVersionId) {
    if (!StringUtils.hasText(processVersionId)) {
        throw new IllegalArgumentException("El id de la versión es obligatorio.");
    }

    WorkflowProcessVersion version = versionRepository.findById(processVersionId)
        .orElseThrow(() -> new IllegalArgumentException("La versión del proceso no existe."));

    if (!"DRAFT".equals(version.getStatus())) {
        throw new IllegalStateException("Solo se pueden publicar versiones en estado DRAFT.");
    }

    WorkflowProcess process = processRepository.findById(version.getProcessId())
        .orElseThrow(() -> new IllegalArgumentException("El proceso asociado no existe."));

    LocalDateTime now = LocalDateTime.now();

    version.setStatus("PUBLISHED");
    version.setPublishedAt(now);
    version.setUpdatedAt(now);

    WorkflowProcessVersion publishedVersion = versionRepository.save(version);

    process.setCurrentPublishedVersionId(publishedVersion.getId());
    process.setUpdatedAt(now);

    processRepository.save(process);

    return publishedVersion;
}
}