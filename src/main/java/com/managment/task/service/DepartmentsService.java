package com.managment.task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.managment.task.model.Departments;
import com.managment.task.repository.DepartmentsRepository;

@Service
public class DepartmentsService {
    @Autowired
    private DepartmentsRepository departmentsRepository;

    public DepartmentsService (DepartmentsRepository departmentsRepository){
        this.departmentsRepository = departmentsRepository;
    }

    public Iterable<Departments> findAllDepartment(){
        return departmentsRepository.findAll();
    }
}
