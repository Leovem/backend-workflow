package com.workflow.backend.model;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "instances")
public class Instance {

    @Id
    private String id;

    private String processId;
    private String currentNodeId;

    private String estado; // en_proceso, completado

    private Date fechaInicio;
    private Date fechaFin;

    private Map<String, Object> data;
}