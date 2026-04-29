package com.workflow.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.workflow.backend.model.Task;

public interface TaskRepository extends MongoRepository<Task, String> {
}
