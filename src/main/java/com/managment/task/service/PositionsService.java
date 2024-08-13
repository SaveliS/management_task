package com.managment.task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.managment.task.model.Positions;
import com.managment.task.repository.PositionsRepository;

@Service
public class PositionsService {
    
    @Autowired
    private PositionsRepository positionsRepository;

    public PositionsService (PositionsRepository positionsRepository){
        this.positionsRepository = positionsRepository;
    }

    public Iterable<Positions> findAllPosition(){
        return positionsRepository.findAll();
    }
}
