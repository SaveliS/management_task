package com.managment.task.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_groups")
public class EmployeeGroups {

    @EmbeddedId
    private EmployeeGroupsKey employee_groups;

    @ManyToOne()
    @MapsId("employeeId")
    @JoinColumn(name = "employeein")
    private Employees employeeIn;

    @ManyToOne()
    @MapsId("groupId")
    @JoinColumn(name = "groupin")
    private Groups groupIn;

    @Embeddable
    public static class EmployeeGroupsKey implements Serializable{
        @Column(name = "employee_id")
        private int employeeId;

        @Column(name = "group_id")
        private int groupId;

        public EmployeeGroupsKey() {}

        public EmployeeGroupsKey(int employeeId, int groupId) {
            this.employeeId = employeeId;
            this.groupId = groupId;
        }
    
        public int getEmployeeId() {
            return employeeId;
        }
    
        public void setEmployeeId(int employeeId) {
            this.employeeId = employeeId;
        }
    
        public int getGroupId() {
            return groupId;
        }
    
        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + employeeId;
            result = prime * result + groupId;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            EmployeeGroupsKey other = (EmployeeGroupsKey) obj;
            if (employeeId != other.employeeId)
                return false;
            if (groupId != other.groupId)
                return false;
            return true;
        }
    }

    public EmployeeGroups() {}

    public EmployeeGroups(EmployeeGroupsKey employee_groups, Employees employeeIn, Groups groupIn) {
        this.employee_groups = employee_groups;
        this.employeeIn = employeeIn;
        this.groupIn = groupIn;
    }

    public EmployeeGroupsKey getEmployee_groups() {
        return employee_groups;
    }

    public void setEmployee_groups(EmployeeGroupsKey employee_groups) {
        this.employee_groups = employee_groups;
    }

    public Employees getEmployeeIn() {
        return employeeIn;
    }

    public void setEmployeeIn(Employees employeeIn) {
        this.employeeIn = employeeIn;
    }

    public Groups getGroupIn() {
        return groupIn;
    }

    public void setGroupIn(Groups groupIn) {
        this.groupIn = groupIn;
    }

    
}
