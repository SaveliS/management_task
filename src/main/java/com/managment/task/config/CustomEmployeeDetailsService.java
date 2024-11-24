package com.managment.task.config;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.managment.task.model.Employees;
import com.managment.task.repository.EmployeesRepository;

@Service
public class CustomEmployeeDetailsService implements UserDetailsService{
    
    private final EmployeesRepository employeesRepository;

    public CustomEmployeeDetailsService(EmployeesRepository employeesRepository){
        this.employeesRepository = employeesRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employees employee = employeesRepository.findByLogin(username)
            .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден."));

        return new User(
            employee.getLogin(),
            employee.getPassword(),
            employee.isEnable(),
            true,
            true,
            true,
            employee.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getNameRole()))
                .collect(Collectors.toList()));
    }
}
