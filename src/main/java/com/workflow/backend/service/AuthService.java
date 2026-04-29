package com.workflow.backend.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.workflow.backend.DTO.LoginRequest;
import com.workflow.backend.model.User;
import com.workflow.backend.repository.UserRepository;
import com.workflow.backend.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepo,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(LoginRequest request) {

        User user = userRepo.findByEmail(request.getEmail());

        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        System.out.println("[DEBUG LOGIN] ✅ Usuario encontrado en DB: " + user.getNombre());
        
        // --- AQUÍ ESTÁ EL PUNTO CLAVE ---
        System.out.println("[DEBUG LOGIN] Password recibido (Postman): " + request.getPassword());
        System.out.println("[DEBUG LOGIN] Hash en DB (passwordHash): " + user.getPasswordHash());

        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        System.out.println("[DEBUG LOGIN] ¿Coinciden según BCrypt?: " + matches);

        if (!matches) {
            System.out.println("[DEBUG LOGIN] ❌ Error: La contraseña no coincide con el Hash guardado.");
            throw new RuntimeException("Contraseña incorrecta");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtService.generateToken(user);
    }
}
