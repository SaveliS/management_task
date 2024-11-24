package com.managment.task.builder.employee_builder;

import java.util.Set;

import com.managment.task.model.Departments;
import com.managment.task.model.Employees;
import com.managment.task.model.Positions;
import com.managment.task.model.Roles;

public interface EmployeeBuilder {
    EmployeeBuilder setFirstName(String name);
    EmployeeBuilder setLastName(String name);
    EmployeeBuilder setLogin(String login);
    EmployeeBuilder setPassword(String password);
    EmployeeBuilder setEnable(boolean isEnable);
    EmployeeBuilder setRoles(Set<Roles> roles);
    EmployeeBuilder setDepartment(Departments departments);
    EmployeeBuilder setPosition(Positions positions);
    Employees build();
}
