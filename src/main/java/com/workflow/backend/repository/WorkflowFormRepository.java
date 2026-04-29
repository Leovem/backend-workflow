package com.workflow.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.workflow.backend.model.WorkflowForm;

public interface WorkflowFormRepository extends MongoRepository<WorkflowForm, String> {

    List<WorkflowForm> findByProcessVersionId(String processVersionId);

    Optional<WorkflowForm> findByProcessVersionIdAndNodeId(
        String processVersionId,
        String nodeId
    );

    List<WorkflowForm> findAllByProcessVersionIdAndNodeId(
    String processVersionId,
    String nodeId
);


}