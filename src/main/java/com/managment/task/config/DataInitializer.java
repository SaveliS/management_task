package com.managment.task.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.managment.task.builder.employee_builder.EmployeeDirector;
import com.managment.task.builder.employee_builder.OfficialEmployeeBuilder;
import com.managment.task.model.Employees;
import com.managment.task.model.Roles;
import com.managment.task.repository.EmployeesRepository;
import com.managment.task.repository.RolesRepository;

import jakarta.persistence.EntityNotFoundException;

@Configuration
public class DataInitializer {
    private final EmployeesRepository employeesRepository;
    private final RolesRepository rolesRepository;

    public DataInitializer(RolesRepository rolesRepository, EmployeesRepository employeesRepository){
        this.employeesRepository = employeesRepository;
        this.rolesRepository = rolesRepository;
    }
    
    @Bean
    @Transactional
    public CommandLineRunner initRoles(){
        return args -> {
            if(rolesRepository.findByNameRole("USER").isEmpty()){
                rolesRepository.save(new Roles("USER"));
            }

            if(rolesRepository.findByNameRole("ADMIN").isEmpty()){
                rolesRepository.save(new Roles("ADMIN"));
            }

            if(rolesRepository.findByNameRole("MANAGER").isEmpty()){
                rolesRepository.save(new Roles("MANAGER"));
            }
        };
    }

    @Bean
    @Transactional
    public CommandLineRunner initAdmin(){
        return args -> {
            if(employeesRepository.findByLogin("ADMIN").isEmpty()){
                EmployeeDirector officalEmployee = new EmployeeDirector(new OfficialEmployeeBuilder());
                Employees employees = officalEmployee.constructOfficalEmployees("ADMIN", Set.of(
                    rolesRepository.findByNameRole("ADMIN")
                        .orElseThrow(
                            () -> new EntityNotFoundException("Роль не найдена")
                        )
                    )
                );
                employeesRepository.save(employees);
            }
        };
    }
}
