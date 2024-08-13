package com.managment.task.config;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.managment.task.model.Employees;
import com.managment.task.repository.EmployeesRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Component
public class PasswordMigration {
    private final EmployeesRepository employeesRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordMigration(EmployeesRepository employeesRepository) {
        this.employeesRepository = employeesRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostConstruct
    @Transactional
    public void migratedPassword(){
        List<Employees> employees = employeesRepository.findAll();
        for(Employees employee: employees){
            String oldPassword = employee.getPassword();
            if(!oldPassword.startsWith("$2a$")){
                String bCryotPassword = passwordEncoder.encode(oldPassword);
                employee.setPassword(bCryotPassword);
                employeesRepository.save(employee);
            }
        }
    }
}
