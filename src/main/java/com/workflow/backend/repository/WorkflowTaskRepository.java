package com.workflow.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.workflow.backend.model.WorkflowTask;

public interface WorkflowTaskRepository extends MongoRepository<WorkflowTask, String> {

    List<WorkflowTask> findByInstanceId(String instanceId);

    List<WorkflowTask> findByDepartmentIdAndStatus(
        String departmentId,
        String status
    );

    List<WorkflowTask> findByAssignedUserIdAndStatus(
        String assignedUserId,
        String status
    );

    List<WorkflowTask> findByProcessVersionId(String processVersionId);
}