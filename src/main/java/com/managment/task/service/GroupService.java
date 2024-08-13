package com.managment.task.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.managment.task.model.EmployeeGroups;
import com.managment.task.model.Employees;
import com.managment.task.model.Groups;
import com.managment.task.repository.GroupsRepository;

@Service
public class GroupService {

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private EmployeeGroupsService employeeGroupsService;

    @Autowired
    private EmployeeService employeeService;

    public GroupService(GroupsRepository groupsRepository){
        this.groupsRepository = groupsRepository;
    }

    public Iterable<Groups> findAllGroup(){
        return groupsRepository.findAll();
    }

    @Transactional
    public String addNewEmployee(String groupsName, List<Integer> selectedEmployeesIDs){
        try {
            Groups group = groupsRepository.save(new Groups(groupsName));

            for(int id: selectedEmployeesIDs){
                Employees employeesInNewGroup = employeeService.findByIdEmployee(id);
                EmployeeGroups.EmployeeGroupsKey key = new EmployeeGroups.EmployeeGroupsKey(employeesInNewGroup.getEmployeeId(),group.getGroupId());
                EmployeeGroups newEmployeeGroups = new EmployeeGroups(key, employeesInNewGroup, group);
                employeeGroupsService.saveEmployeeGroups(newEmployeeGroups);
            }

            return "Группа "+ group.getGroupName() +" успешно добавлена.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Ошибка: Группа не добавлена.";
        }
    }
}
