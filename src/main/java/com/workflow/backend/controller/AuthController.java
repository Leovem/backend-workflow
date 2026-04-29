package com.workflow.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.backend.DTO.LoginRequest;
import com.workflow.backend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    //@PostMapping("/login")
    //public String login(@RequestBody LoginRequest request) {
    //    return service.login(request);
    //}

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        String token = service.login(request);
        
        // Creamos un mapa para que Spring lo convierta a JSON automáticamente: {"token": "eyJ..."}
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        
        return response;
    }
}
