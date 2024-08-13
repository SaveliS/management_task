package com.managment.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.managment.task.service.TaskService;

@Controller
public class MainController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/")
    public String homePage(Model model){
        model.addAttribute("tasks", taskService.findAllTaskNotSubTasks());
        return "main";
    }
}
