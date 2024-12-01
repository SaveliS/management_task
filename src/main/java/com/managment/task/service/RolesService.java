package com.managment.task.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.managment.task.model.Roles;
import com.managment.task.repository.RolesRepository;

@Service
public class RolesService {
    
    private final RolesRepository rolesRepository;

    public RolesService (RolesRepository rolesRepository){
        this.rolesRepository = rolesRepository;
    }

    /**
     * @return список всех существующих ролей
     */
    public List<Roles> getAllRoles(){
        return rolesRepository.findAll();
    }
}
