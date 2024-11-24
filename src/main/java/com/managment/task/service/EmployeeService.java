package com.managment.task.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.managment.task.config.CustomEmployeeDetailsService;
import com.managment.task.model.Employees;
import com.managment.task.model.TaskEmployee;
import com.managment.task.repository.EmployeeGroupsRepository;
import com.managment.task.repository.EmployeesRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmployeeService {
    
    private final EmployeesRepository employeesRepository;

    private final EmployeeGroupsRepository employeeGroupsRepository;

    public static void removeDuplicateEmployees(List<Employees> newUserInTask){
        Set<Integer> uniqueEmployeeIds = new HashSet<>();
        Iterator<Employees> iterator = newUserInTask.iterator();

        while(iterator.hasNext()){
            Employees employee = iterator.next();
            int employeeId = employee.getEmployeeId();

            if (!uniqueEmployeeIds.add(employeeId)) {
                // Если employeeId уже в Set, удаляем текущий элемент
                iterator.remove();
            }
        }
    }

    @Autowired
    public EmployeeService(EmployeesRepository employeesRepository, EmployeeGroupsRepository employeeGroupsRepository){
        this.employeeGroupsRepository = employeeGroupsRepository;
        this.employeesRepository = employeesRepository;
    }

    public Iterable<Employees> findAllEmployee(){
        return employeesRepository.findAll();
    }

    public Employees findByIdEmployee(int id){
        return employeesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public void addNewEmployee(Employees employees){
        employeesRepository.save(employees);
    }

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    public Employees save(Employees employees){
       return employeesRepository.save(employees); 
    }

    public Optional<Employees> findByLogin(String login){
        return employeesRepository.findByLogin(login);
    }

    /**
     * Получение пользователя по логину пользователя
     *
     * @return пользователь
     */
    public Employees getByLogin(String login){
        return employeesRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Сотрудник с данным логином не найден"));
    }

    /**
     * @param employeeId - Номер сотрудника
     * @return - Сущность сотрудника
     */
    public Optional<Employees> findEmployeeById(String employeeId){
        try {
            int employeeIdInt = Integer.parseInt(employeeId);
            return employeesRepository.findById(employeeIdInt);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * @param groupId - Номер группы
     * @return - Список сотрудников данной группы
     */
    public List<Employees> findEmployeeGroupsById(String groupId){
       try {
            int groupIdInt = Integer.parseInt(groupId);
            return Optional.ofNullable(employeeGroupsRepository.findAllEmployeesByIdGroups(groupIdInt))
                           .orElse(Collections.emptyList());
       } catch (NumberFormatException e) {
         return Collections.emptyList();
       }
    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return new CustomEmployeeDetailsService(employeesRepository);
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public Employees getCurrentUser(){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByLogin(login);
    }

    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public Employees create(Employees employees){
        if(employeesRepository.existsByLogin(employees.getLogin())){
            // Заменить на исключения
        }

        return save(employees);
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
