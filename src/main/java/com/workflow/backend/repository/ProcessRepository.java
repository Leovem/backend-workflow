package com.workflow.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.workflow.backend.model.Process;

public interface ProcessRepository extends MongoRepository<Process, String> {
}