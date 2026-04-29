package com.workflow.backend.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "workflow_forms")
public class WorkflowForm {

    @Id
    private String id;

    private String processId;
    private String processVersionId;
    private String nodeId;

    private String title;
    private String description;

    private Boolean generatedByAI;

    private List<WorkflowFormField> fields = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // getters y setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getProcessId() {
        return processId;
    }
    public void setProcessId(String processId) {
        this.processId = processId;
    }
    public String getProcessVersionId() {
        return processVersionId;
    }
    public void setProcessVersionId(String processVersionId) {
        this.processVersionId = processVersionId;
    }
    public String getNodeId() {
        return nodeId;
    }
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Boolean getGeneratedByAI() {
        return generatedByAI;
    }
    public void setGeneratedByAI(Boolean generatedByAI) {
        this.generatedByAI = generatedByAI;
    }
    public List<WorkflowFormField> getFields() {
        return fields;
    }
    public void setFields(List<WorkflowFormField> fields) {
        this.fields = fields;
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
