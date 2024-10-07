package com.managment.task.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.managment.task.builder.TasksDirector;
import com.managment.task.model.Employees;
import com.managment.task.model.Tasks;
import com.managment.task.service.EmployeeService;
import com.managment.task.service.GroupService;
import com.managment.task.service.TaskService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.ui.Model;



@Controller
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private TasksDirector tasksDirector;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public String getTemplate(Model model){
        model.addAttribute("tasks", taskService.findAllTaskTemplate());
        return "template/templateManagement";
    }

    @GetMapping("/new")
    public String createNewTemplate(Model model, @AuthenticationPrincipal UserDetails userDetails){
        Employees autUser = employeeService.findByLogin(userDetails.getUsername()).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден."));
        // Выбор ответсвенного исполнителя (категории)
        model.addAttribute("selectedResponsible", new String(""));
        model.addAttribute("groupsEmpl", groupService.findAllGroup());
        model.addAttribute("template", tasksDirector.consturctTasksTemplate(autUser, autUser));
        model.addAttribute("allEmployee", employeeService.findAllEmployee());
        return "template/templateNew";
    }

    @PostMapping("/new")
    public String createNewTemplate(@ModelAttribute(name = "template") Tasks template, @RequestParam(name = "selectedGroups") List<String> pinnedGroups, @RequestParam (name = "selectedUsers") List<String> pinnedEmployees){
        taskService.addNewTemplated(template,pinnedGroups,pinnedEmployees);
        return "redirect:/template";
    }
}
