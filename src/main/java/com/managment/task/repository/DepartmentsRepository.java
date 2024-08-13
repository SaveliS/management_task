package com.managment.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.managment.task.model.Departments;

public interface DepartmentsRepository extends JpaRepository<Departments, Integer>{
    List<Departments> findAll();
}
