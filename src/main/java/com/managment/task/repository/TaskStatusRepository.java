package com.managment.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.managment.task.model.TaskStatus;

public interface TaskStatusRepository extends JpaRepository<TaskStatus,Integer>{
    List<TaskStatus> findAll();
}
