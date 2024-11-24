package com.managment.task.builder.employee_builder;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.managment.task.model.Employees;
import com.managment.task.model.Roles;

@Component
public class EmployeeDirector {
    private final EmployeeBuilder employeeBuilder;

    public EmployeeDirector(EmployeeBuilder employeeBuilder){
        this.employeeBuilder = employeeBuilder;
    }


    /**
     * Создание служебных пользователей
     * 
     * @param login логин служебного пользователя
     * @param roles роли служебного пользователя
     * @return объект служебного пользователя
     */
    public Employees constructOfficalEmployees(String login,Set<Roles> roles){
        return employeeBuilder
            .setLogin(login)
            .setRoles(roles)
            .setFirstName(null)
            .setLastName(null)
            .setPassword(null)
            .setEnable(true)
            .build();
    }
}
