package com.managment.task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.managment.task.model.TaskStatus;
import com.managment.task.repository.TaskStatusRepository;

@Service
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    public TaskStatusService(TaskStatusRepository taskStatusRepository){
        this.taskStatusRepository = taskStatusRepository;
    }

    public Iterable<TaskStatus> findAllTaskStatus(){
        return taskStatusRepository.findAll();
    } 
}
