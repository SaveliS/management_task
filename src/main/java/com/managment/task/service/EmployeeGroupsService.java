package com.managment.task.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.managment.task.model.EmployeeGroups;
import com.managment.task.repository.EmployeeGroupsRepository;

@Service
public class EmployeeGroupsService {
    
    @Autowired
    private EmployeeGroupsRepository employeeGroupsRepository;

    public EmployeeGroupsService(EmployeeGroupsRepository employeeGroupsRepository) {
        this.employeeGroupsRepository = employeeGroupsRepository;
    }

    public Iterable<EmployeeGroups> findAllEmployeeGroups(){
        return employeeGroupsRepository.findAll();
    }

    public Map<String,List<EmployeeGroups>> findEmployeeSortGroups(){
        List<EmployeeGroups> employeeGroups = employeeGroupsRepository.findAll();

        Map<String,List<EmployeeGroups>> groupsMap = employeeGroups.stream()
            .collect(Collectors.groupingBy(eg -> eg.getGroupIn().getGroupName()));
        
        return groupsMap;
    }

    public EmployeeGroups saveEmployeeGroups(EmployeeGroups employeeGroups){
        return employeeGroupsRepository.save(employeeGroups);
    }
}
