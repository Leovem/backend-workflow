package com.workflow.backend.DTO;

import java.util.List;

public class UpdateWorkflowFormRequest {

    private String title;
    private String description;
    private Boolean generatedByAI;
    private List<WorkflowFormFieldRequest> fields;

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

    public List<WorkflowFormFieldRequest> getFields() {
        return fields;
    }

    public void setFields(List<WorkflowFormFieldRequest> fields) {
        this.fields = fields;
    }
}