package com.workflow.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.workflow.backend.model.WorkflowProcessVersion;

public interface WorkflowProcessVersionRepository extends MongoRepository<WorkflowProcessVersion, String> {

    List<WorkflowProcessVersion> findByProcessId(String processId);

    Optional<WorkflowProcessVersion> findByProcessIdAndStatus(
        String processId,
        String status
    );

    long countByProcessId(String processId);
}