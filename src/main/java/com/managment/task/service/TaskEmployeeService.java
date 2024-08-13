package com.managment.task.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.managment.task.model.Employees;
import com.managment.task.model.TaskEmployee;
import com.managment.task.repository.TaskEmployeeRepository;

@Service
public class TaskEmployeeService {
    
    @Autowired
    private TaskEmployeeRepository taskEmployeeRepository;

    public TaskEmployeeService(TaskEmployeeRepository taskEmployeeRepository){
        this.taskEmployeeRepository = taskEmployeeRepository;
    }

    public Iterable<TaskEmployee> findAllTaskEmployee(){
        return taskEmployeeRepository.findAll();
    }

    public TaskEmployee addNewTaskEmployee(TaskEmployee newTaskEmployee){
        return taskEmployeeRepository.save(newTaskEmployee);
    }

    public List<Employees> findAllEmployeeByIdTask(int id){
        return taskEmployeeRepository.findAllByTasks_TaskId(id).stream()
                                            .map(TaskEmployee::getEmployees)
                                            .collect(Collectors.toList());
    }

    public List<TaskEmployee> findAllTaskEmployeesByIdTask(int idTask){
        return taskEmployeeRepository.findAllByTasks_TaskId(idTask).stream()
                                        .collect(Collectors.toList());
    }

   
    public String changeEmployeeStatusTask(int taskId, int employeeId, TaskEmployee.Status valueStatus, String comment){
        TaskEmployee editTaskEmployee = taskEmployeeRepository.findByTasks_TaskIdAndEmployees_EmployeeId(taskId, employeeId);
        editTaskEmployee.setComplete(valueStatus);
        editTaskEmployee.setComment(comment);
        taskEmployeeRepository.save(editTaskEmployee);
        return "Добавлен";
    }
}
