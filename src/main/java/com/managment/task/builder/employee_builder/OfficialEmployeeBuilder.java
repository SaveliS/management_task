package com.managment.task.builder.employee_builder;

import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.managment.task.model.Departments;
import com.managment.task.model.Employees;
import com.managment.task.model.Positions;
import com.managment.task.model.Roles;

@Component
public class OfficialEmployeeBuilder implements EmployeeBuilder{
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private Employees officialTemplate = new Employees();

    @Override
    public EmployeeBuilder setFirstName(String name) {
        this.officialTemplate.setFirstName("Служебный");
        return this;
    }

    @Override
    public EmployeeBuilder setLastName(String name) {
        this.officialTemplate.setLastName("Служебный");
        return this;
    }

    @Override
    public EmployeeBuilder setLogin(String login) {
        this.officialTemplate.setLogin(login);
        return this;
    }

    @Override
    public EmployeeBuilder setPassword(String password) {
        this.officialTemplate.setPassword(passwordEncoder.encode(officialTemplate.getLogin()));
        return this;
    }

    @Override
    public EmployeeBuilder setEnable(boolean isEnable) {
        this.officialTemplate.setEnable(true);
        return this;
    }

    @Override
    public EmployeeBuilder setRoles(Set<Roles> roles) {
        this.officialTemplate.setRoles(roles);
        return this;
    }

    @Override
    public EmployeeBuilder setDepartment(Departments departments) {
        this.officialTemplate.setDepartmentId(null);
        return this;
    }

    @Override
    public EmployeeBuilder setPosition(Positions positions) {
        this.officialTemplate.setPositionId(null);
        return this;
    }

    @Override
    public Employees build() {
        if(officialTemplate.getLogin() == null || officialTemplate.getFirstName() == null || officialTemplate.getLastName() == null){
            throw new IllegalStateException("Обязательные поля не заполянены.");
        }
        return officialTemplate;
    }

}
