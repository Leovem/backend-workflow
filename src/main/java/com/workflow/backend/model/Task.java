package com.workflow.backend.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "tasks")
public class Task {

    @Id
    private String id;

    private String instanceId;
    private String processId;

    private String nodeId;
    private String nombre;

    private String rol;
    private String usuarioId;

    private String estado; // pendiente, en_proceso, completado

    private Date fechaAsignacion;
    private Date fechaInicio;
    private Date fechaFin;

    private Long duracionSegundos;
}