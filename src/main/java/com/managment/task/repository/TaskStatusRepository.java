package com.managment.task.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.managment.task.model.TaskStatus;

public interface TaskStatusRepository extends JpaRepository<TaskStatus,Integer>{
    List<TaskStatus> findAll();
    Optional<TaskStatus> findByStatusId(int idTaskStatus);
}
