package com.managment.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.managment.task.model.AttributeTask;
import com.managment.task.model.AttributeTask.AttributeTaskKey;

public interface AttributeTaskRepository extends JpaRepository <AttributeTask,AttributeTaskKey>{
    
}
