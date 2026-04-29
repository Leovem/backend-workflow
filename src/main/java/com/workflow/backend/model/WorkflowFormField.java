package com.workflow.backend.model;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class WorkflowFormField {

    private String id;

    private String label;
    private String type;
    private Boolean required;
    private String placeholder;
    private List<String> options = new ArrayList<>();
    private Integer order;

    // getters y setters
}