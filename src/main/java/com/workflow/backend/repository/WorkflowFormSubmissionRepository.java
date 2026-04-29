package com.workflow.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.workflow.backend.model.WorkflowFormSubmission;

public interface WorkflowFormSubmissionRepository extends MongoRepository<WorkflowFormSubmission, String> {

    Optional<WorkflowFormSubmission> findByTaskId(String taskId);

    List<WorkflowFormSubmission> findByInstanceId(String instanceId);

    List<WorkflowFormSubmission> findBySubmittedByUserId(String submittedByUserId);
}