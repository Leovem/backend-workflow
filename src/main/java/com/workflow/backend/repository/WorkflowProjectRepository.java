package com.workflow.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.workflow.backend.model.ProjectStatus;
import com.workflow.backend.model.WorkflowProject;

public interface WorkflowProjectRepository extends MongoRepository<WorkflowProject, String> {

    List<WorkflowProject> findAllByOrderByCreatedAtDesc();

    List<WorkflowProject> findByStatusOrderByCreatedAtDesc(ProjectStatus status);

    List<WorkflowProject> findByMemberUserIdsContainingOrderByCreatedAtDesc(String userId);

    Optional<WorkflowProject> findByAccessCode(String accessCode);

    boolean existsByAccessCode(String accessCode);
}