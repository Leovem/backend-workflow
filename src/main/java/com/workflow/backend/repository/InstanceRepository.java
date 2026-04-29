package com.workflow.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.workflow.backend.model.Instance;

public interface InstanceRepository extends MongoRepository<Instance, String> {
}
