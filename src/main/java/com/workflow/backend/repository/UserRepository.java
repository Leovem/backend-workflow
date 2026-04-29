package com.workflow.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.workflow.backend.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}