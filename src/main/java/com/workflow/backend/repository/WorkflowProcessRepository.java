package com.workflow.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.workflow.backend.model.WorkflowProcess;

public interface WorkflowProcessRepository extends MongoRepository<WorkflowProcess, String> {
}