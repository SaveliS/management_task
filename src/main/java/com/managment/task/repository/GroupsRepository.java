package com.managment.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.managment.task.model.Groups;

public interface GroupsRepository extends JpaRepository<Groups,Integer> {
    
}
