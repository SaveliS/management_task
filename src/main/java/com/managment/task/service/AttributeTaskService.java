package com.managment.task.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.managment.task.model.AttributeTask;
import com.managment.task.repository.AttributeTaskRepository;

@Service
public class AttributeTaskService {
    
    @Autowired
    private AttributeTaskRepository attributeTaskRepository;

    public List<AttributeTask> getAllAttributeTaskById(int id){
        return attributeTaskRepository.findAllByIdTask(id);
    }
}
