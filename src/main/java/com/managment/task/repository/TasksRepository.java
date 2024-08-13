package com.managment.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.managment.task.model.Tasks;

public interface TasksRepository extends JpaRepository<Tasks,Integer>{

}
