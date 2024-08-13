package com.managment.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.managment.task.model.Employees;
import com.managment.task.service.DepartmentsService;
import com.managment.task.service.EmployeeService;
import com.managment.task.service.PositionsService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private PositionsService positionsService;

    @Autowired
    private DepartmentsService departmentsService;

    @Autowired
    private EmployeeService employeeService;


    @GetMapping()
    public String homeUser(Model model){
        model.addAttribute("employee", employeeService.findAllEmployee());
        return "employees/userManagement";
    }

    @GetMapping("/new")
    public String createEmployee(Model model){
        model.addAttribute("departments", departmentsService.findAllDepartment());
        model.addAttribute("positions", positionsService.findAllPosition());
        model.addAttribute("employee", new Employees());
        return "employees/newEmployee";
    }

    @PostMapping("/new")
    public String createEmployee(@ModelAttribute("departments") Employees employees){
        employeeService.addNewEmployee(employees);
        return "redirect:/";
    }
    
}
