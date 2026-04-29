package com.workflow.backend.DTO;

import java.util.List;

import lombok.Data;

@Data
public class BulkWorkflowFormRequest {
    private String processId;
    private String processVersionId;
    private List<WorkflowFormRequest> forms;

    // getters y setters
}