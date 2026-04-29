package com.workflow.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.workflow.backend.model.WorkflowInstance;

public interface WorkflowInstanceRepository extends MongoRepository<WorkflowInstance, String> {

    List<WorkflowInstance> findByProcessVersionId(String processVersionId);

    List<WorkflowInstance> findByStatus(String status);
}