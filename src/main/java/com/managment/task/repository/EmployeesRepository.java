package com.managment.task.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.managment.task.model.Employees;

public interface EmployeesRepository extends JpaRepository<Employees,Integer>{
    List<Employees> findAll();
    Optional<Employees> findByLogin(String login);
}
