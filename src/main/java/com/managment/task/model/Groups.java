package com.managment.task.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "groups")
public class Groups {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private int groupId;
    @Column(name = "group_name")
    private String groupName;

    @OneToMany(mappedBy = "groupIn")
    private List<EmployeeGroups> employeeGroups = new ArrayList<>();

    public Groups(){}

    public Groups(String groupName){
        this.groupName = groupName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<EmployeeGroups> getEmployeeGroups() {
        return employeeGroups;
    }

    public void setEmployeeGroups(List<EmployeeGroups> employeeGroups) {
        this.employeeGroups = employeeGroups;
    }
}
