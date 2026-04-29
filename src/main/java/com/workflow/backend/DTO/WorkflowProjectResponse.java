package com.workflow.backend.DTO;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.workflow.backend.model.ProjectStatus;
import com.workflow.backend.model.ProjectVisibility;

import lombok.Data;

@Data
public class WorkflowProjectResponse {

    private String id;
    private String name;
    private String description;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
    private ProjectStatus status;
    private String collaborationRoomId;
    private String accessCode;
    private ProjectVisibility visibility;
    private List<String> memberUserIds;
    private Map<String, Object> diagramSnapshot;

    public WorkflowProjectResponse(
            String id,
            String name,
            String description,
            String createdBy,
            Instant createdAt,
            Instant updatedAt,
            ProjectStatus status,
            String collaborationRoomId,
            String accessCode,
            ProjectVisibility visibility,
            List<String> memberUserIds,
            Map<String, Object> diagramSnapshot
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.collaborationRoomId = collaborationRoomId;
        this.accessCode = accessCode;
        this.visibility = visibility;
        this.memberUserIds = memberUserIds;
        this.diagramSnapshot = diagramSnapshot;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCreatedBy() { return createdBy; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public ProjectStatus getStatus() { return status; }
    public String getCollaborationRoomId() { return collaborationRoomId; }
    public String getAccessCode() { return accessCode; }
    public ProjectVisibility getVisibility() { return visibility; }
    public List<String> getMemberUserIds() { return memberUserIds; }
    public Map<String, Object> getDiagramSnapshot() { return diagramSnapshot; }
}