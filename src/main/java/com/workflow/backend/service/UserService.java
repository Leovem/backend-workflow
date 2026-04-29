package com.workflow.backend.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.workflow.backend.DTO.CreateUserRequest;
import com.workflow.backend.model.User;
import com.workflow.backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(CreateUserRequest request) {

        User user = new User();
        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());
        user.setRoles(request.getRoles());
        user.setActivo(request.getActivo());


        // 🔐 aquí se encripta (CORRECTO)
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        user.setActivo(true);

        return repo.save(user);
    }

    public List<User> getAll() {
        return repo.findAll();
    }
}