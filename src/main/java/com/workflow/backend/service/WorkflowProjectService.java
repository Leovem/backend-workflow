package com.workflow.backend.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.workflow.backend.DTO.CreateWorkflowProjectRequest;
import com.workflow.backend.DTO.JoinWorkflowProjectRequest;
import com.workflow.backend.DTO.UpdateDiagramSnapshotRequest;
import com.workflow.backend.DTO.WorkflowProjectResponse;
import com.workflow.backend.model.ProjectStatus;
import com.workflow.backend.model.WorkflowProject;
import com.workflow.backend.repository.WorkflowProjectRepository;

@Service
public class WorkflowProjectService {

    private final WorkflowProjectRepository repository;

    public WorkflowProjectService(WorkflowProjectRepository repository) {
        this.repository = repository;
    }

    public WorkflowProjectResponse create(
            CreateWorkflowProjectRequest request,
            String userId
    ) {
        WorkflowProject project = new WorkflowProject(
                request.getName(),
                request.getDescription(),
                userId
        );

        project.setAccessCode(generateUniqueAccessCode());
        project.setDiagramSnapshot(emptyDiagramSnapshot());

        WorkflowProject saved = repository.save(project);

        saved.setCollaborationRoomId(saved.getId());
        saved = repository.save(saved);

        return toResponse(saved);
    }

    public List<WorkflowProjectResponse> findAllForAdmin() {
        return repository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<WorkflowProjectResponse> findMyProjects(String userId) {
        return repository.findByMemberUserIdsContainingOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public WorkflowProjectResponse findById(String id, String userId, boolean isAdmin) {
        WorkflowProject project = getProjectOrThrow(id);

        if (!isAdmin && !project.getMemberUserIds().contains(userId)) {
            throw new RuntimeException("No tienes permiso para abrir este proyecto");
        }

        return toResponse(project);
    }

    public WorkflowProjectResponse joinByCode(
            JoinWorkflowProjectRequest request,
            String userId
    ) {
        WorkflowProject project = repository.findByAccessCode(request.getAccessCode())
                .orElseThrow(() -> new RuntimeException("Código de proyecto inválido"));

        if (!project.getMemberUserIds().contains(userId)) {
            project.getMemberUserIds().add(userId);
            project.setUpdatedAt(Instant.now());
        }

        WorkflowProject saved = repository.save(project);

        return toResponse(saved);
    }

    public WorkflowProjectResponse updateSnapshot(
            String id,
            UpdateDiagramSnapshotRequest request,
            String userId,
            boolean isAdmin
    ) {
        WorkflowProject project = getProjectOrThrow(id);

        if (!isAdmin && !project.getMemberUserIds().contains(userId)) {
            throw new RuntimeException("No tienes permiso para guardar este proyecto");
        }

        project.setDiagramSnapshot(request.getDiagramSnapshot());
        project.setUpdatedAt(Instant.now());

        WorkflowProject saved = repository.save(project);

        return toResponse(saved);
    }

    public WorkflowProjectResponse archive(String id, String userId, boolean isAdmin) {
        WorkflowProject project = getProjectOrThrow(id);

        if (!isAdmin && !project.getCreatedBy().equals(userId)) {
            throw new RuntimeException("Solo el admin o creador puede archivar este proyecto");
        }

        project.setStatus(ProjectStatus.ARCHIVED);
        project.setUpdatedAt(Instant.now());

        WorkflowProject saved = repository.save(project);

        return toResponse(saved);
    }

    private WorkflowProject getProjectOrThrow(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
    }

    private Map<String, Object> emptyDiagramSnapshot() {
        return Map.of(
                "lanes", List.of(),
                "nodes", List.of(),
                "links", List.of()
        );
    }

    private String generateUniqueAccessCode() {
        String code;

        do {
            code = generateAccessCode();
        } while (repository.existsByAccessCode(code));

        return code;
    }

    private String generateAccessCode() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();

        return "WF-" +
                letters.charAt(random.nextInt(letters.length())) +
                letters.charAt(random.nextInt(letters.length())) +
                "-" +
                (1000 + random.nextInt(9000));
    }

    private WorkflowProjectResponse toResponse(WorkflowProject project) {
        return new WorkflowProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedBy(),
                project.getCreatedAt(),
                project.getUpdatedAt(),
                project.getStatus(),
                project.getCollaborationRoomId(),
                project.getAccessCode(),
                project.getVisibility(),
                project.getMemberUserIds(),
                project.getDiagramSnapshot()
        );
    }
}