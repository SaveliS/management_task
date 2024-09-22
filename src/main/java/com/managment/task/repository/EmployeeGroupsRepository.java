package com.managment.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.managment.task.model.EmployeeGroups;
import com.managment.task.model.Employees;
import com.managment.task.model.EmployeeGroups.EmployeeGroupsKey;
import com.managment.task.model.Groups;

public interface EmployeeGroupsRepository extends JpaRepository<EmployeeGroups, EmployeeGroupsKey>{
    @Query("SELECT DISTINCT eg.groupIn FROM EmployeeGroups eg")
    List<Groups> findAllUniqueGroups();
    @Query("SELECT eg.employeeIn FROM EmployeeGroups eg WHERE eg.groupIn.groupId = :groupId")
    List<Employees> findAllEmployeesByIdGroups(@Param("groupId") int groupId);
}
