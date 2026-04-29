package com.workflow.backend.DTO;
import java.util.List;

import lombok.Data;

@Data
public class WorkflowFormFieldRequest {
    private String id;
    private String label;
    private String type;
    private Boolean required;
    private String placeholder;
    private List<String> options;
    private Integer order;

    // getters y setters
}