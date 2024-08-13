package com.managment.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.managment.task.model.EmployeeGroups;
import com.managment.task.model.EmployeeGroups.EmployeeGroupsKey;

public interface EmployeeGroupsRepository extends JpaRepository<EmployeeGroups, EmployeeGroupsKey>{
    
}
