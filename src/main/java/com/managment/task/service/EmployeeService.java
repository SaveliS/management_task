package com.managment.task.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.managment.task.model.Employees;
import com.managment.task.model.TaskEmployee;
import com.managment.task.repository.EmployeesRepository;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeesRepository employeesRepository;

    public EmployeeService(EmployeesRepository employeesRepository){
        this.employeesRepository = employeesRepository;
    }

    public Iterable<Employees> findAllEmployee(){
        return employeesRepository.findAll();
    }

    public Employees findByIdEmployee(int id){
        return employeesRepository.findById(id).get();
    }

    public void addNewEmployee(Employees employees){
        employeesRepository.save(employees);
    }

    public Optional<Employees> findByLogin(String login){
        return employeesRepository.findByLogin(login);
    }

    // Проверяет, является ли пользователем исполнителем в задаче
    public boolean isUserPerformer(List<TaskEmployee> employees, String login){
        return employees.stream()
        .anyMatch(empl -> empl.getEmployees().getLogin().equals(login) 
        && isStatusUserInTaskEmployee(empl));
    }

    public boolean isStatusUserInTaskEmployee(TaskEmployee isStatus){
        return Optional.ofNullable(isStatus)
            .map(status -> status.getComplete() == TaskEmployee.Status.DEFAULT)
            .orElse(false);
    }
}
