package com.workflow.backend.DTO;
import java.util.List;

import lombok.Data;
@Data
public class WorkflowFormRequest {
    private String nodeId;
    private String title;
    private String description;
    private Boolean generatedByAI;
    private List<WorkflowFormFieldRequest> fields;

    // getters y setters
}
