package com.managment.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.managment.task.model.TaskStatus;
import com.managment.task.model.Tasks;

public interface TasksRepository extends JpaRepository<Tasks,Integer>{
    List<Tasks> findAllByStatus(TaskStatus taskStatus);
}
