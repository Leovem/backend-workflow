package com.workflow.backend.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "workflow_projects")
public class WorkflowProject {

    @Id
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

    private List<String> memberUserIds = new ArrayList<>();

    private Map<String, Object> diagramSnapshot;

    public WorkflowProject() {
    }

    public WorkflowProject(String name, String description, String createdBy) {
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.status = ProjectStatus.DRAFT;
        this.visibility = ProjectVisibility.PRIVATE;
        this.memberUserIds = new ArrayList<>();
        this.memberUserIds.add(createdBy);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public String getCollaborationRoomId() {
        return collaborationRoomId;
    }

    public void setCollaborationRoomId(String collaborationRoomId) {
        this.collaborationRoomId = collaborationRoomId;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public ProjectVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(ProjectVisibility visibility) {
        this.visibility = visibility;
    }

    public List<String> getMemberUserIds() {
        return memberUserIds;
    }

    public void setMemberUserIds(List<String> memberUserIds) {
        this.memberUserIds = memberUserIds;
    }

    public Map<String, Object> getDiagramSnapshot() {
        return diagramSnapshot;
    }

    public void setDiagramSnapshot(Map<String, Object> diagramSnapshot) {
        this.diagramSnapshot = diagramSnapshot;
    }
}