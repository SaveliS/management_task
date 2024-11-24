package com.managment.task.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.managment.task.model.TaskStatus;
import com.managment.task.repository.TaskStatusRepository;

import jakarta.persistence.EntityNotFoundException;

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

    public TaskStatus findById(int idTaskStatus){
        return taskStatusRepository.findByStatusId(idTaskStatus).orElseThrow(() -> new EntityNotFoundException("null"));
    }

    public Optional<TaskStatus> findByStatusId(String statusId){
        try {
            int taskStatusIdInt = Integer.parseInt(statusId);
            return taskStatusRepository.findByStatusId(taskStatusIdInt);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
