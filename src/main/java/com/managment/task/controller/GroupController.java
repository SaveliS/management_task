package com.managment.task.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.managment.task.service.EmployeeGroupsService;
import com.managment.task.service.EmployeeService;
import com.managment.task.service.GroupService;



@Controller
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private EmployeeGroupsService employeeGroupsService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private GroupService groupService;

    @GetMapping()
    public String homeGroup(Model model){
        model.addAttribute("groups", employeeGroupsService.findEmployeeSortGroups());
        return "groups/groupManagement";
    }

    @GetMapping("/new")
    public String createNewGroup(Model model){
        model.addAttribute("employees", employeeService.findAllEmployee());
        return "groups/groupCreation";
    }

    @PostMapping("/new")
    public String createNewGroup(@RequestParam("groupName") String groupName, 
        @RequestParam("selectedEmployees") List<Integer> selectedEmployeesIDs, RedirectAttributes redirectAttributes){
        String message = groupService.addNewEmployee(groupName, selectedEmployeesIDs);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/group";
    }
}
