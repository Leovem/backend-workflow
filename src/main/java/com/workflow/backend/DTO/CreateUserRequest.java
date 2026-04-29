package com.workflow.backend.DTO;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String nombre;
    private String email;
    private String password;
    private String[] roles;  
    private Boolean activo;
}