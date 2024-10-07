package com.managment.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.managment.task.model.AttributeTask;
import com.managment.task.model.AttributeTask.AttributeTaskKey;

public interface AttributeTaskRepository extends JpaRepository <AttributeTask,AttributeTaskKey>{
    @Query("FROM AttributeTask at WHERE at.attributeTaskKey.taskId =:attributeId")
    List<AttributeTask> findAllByIdTask(@Param("attributeId")int id);
}
