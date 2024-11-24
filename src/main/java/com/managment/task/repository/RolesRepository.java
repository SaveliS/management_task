package com.managment.task.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.managment.task.model.Roles;

public interface RolesRepository extends JpaRepository<Roles,Integer>{
    Optional<Roles> findByNameRole(String name);
}
