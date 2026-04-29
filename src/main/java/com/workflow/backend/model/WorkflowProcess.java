package com.workflow.backend.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "workflow_processes")
public class WorkflowProcess {

    @Id
    private String id;

    private String name;
    private String description;

    private String status; // ACTIVE, ARCHIVED

    private String currentDraftVersionId;
    private String currentPublishedVersionId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentDraftVersionId() {
        return currentDraftVersionId;
    }

    public void setCurrentDraftVersionId(String currentDraftVersionId) {
        this.currentDraftVersionId = currentDraftVersionId;
    }

    public String getCurrentPublishedVersionId() {
        return currentPublishedVersionId;
    }

    public void setCurrentPublishedVersionId(String currentPublishedVersionId) {
        this.currentPublishedVersionId = currentPublishedVersionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}