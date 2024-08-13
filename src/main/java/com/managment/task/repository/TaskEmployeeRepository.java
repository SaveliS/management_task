package com.managment.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.managment.task.model.TaskEmployee;
import com.managment.task.model.TaskEmployee.TaskEmployeeKey;

public interface TaskEmployeeRepository extends JpaRepository<TaskEmployee,TaskEmployeeKey>{
    List<TaskEmployee> findAllByTasks_TaskId(int id);
    TaskEmployee findByTasks_TaskIdAndEmployees_EmployeeId(int taskId, int employeeId);
    List<TaskEmployee> findByTasks_TaskId(int taskId);
}
