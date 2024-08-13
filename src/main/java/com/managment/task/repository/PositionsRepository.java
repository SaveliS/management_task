package com.managment.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.managment.task.model.Positions;

public interface PositionsRepository extends JpaRepository<Positions,Integer>{
    List<Positions> findAll();
}
