package com.managment.task.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "task_employee")
public class TaskEmployee {

    @EmbeddedId
    private TaskEmployeeKey TaskEmployeeKey;

    @ManyToOne()
    @MapsId("taskId")
    @JoinColumn(name = "tasks_id")
    private Tasks tasks;
    
    @ManyToOne()
    @MapsId("employeeId")
    @JoinColumn(name = "employees_id")
    private Employees employees;

    @Column(name = "complete")
    @Enumerated(EnumType.STRING)
    private Status complete;

    public enum Status {
        ACCEPTED,REFUSED,DEFAULT
    }

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    public TaskEmployee(){}

    public TaskEmployee(com.managment.task.model.TaskEmployee.TaskEmployeeKey taskEmployeeKey, Tasks tasks,
            Employees employees, Status complete, String comment) {
        TaskEmployeeKey = taskEmployeeKey;
        this.tasks = tasks;
        this.employees = employees;
        this.complete = complete;
        this.comment = comment;
    }

    @Embeddable
    public static class TaskEmployeeKey implements Serializable{

        @Column(name = "task_id")
        private int taskId;

        @Column(name = "employee_id")
        private int employeeId;

        public TaskEmployeeKey(){}

        public TaskEmployeeKey(int task_id,int employee_id){
            this.taskId = task_id;
            this.employeeId = employee_id;
        }

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }

        public int getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(int employeeId) {
            this.employeeId = employeeId;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + taskId;
            result = prime * result + employeeId;
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
            TaskEmployeeKey other = (TaskEmployeeKey) obj;
            if (taskId != other.taskId)
                return false;
            if (employeeId != other.employeeId)
                return false;
            return true;
        }

        
    }

    public TaskEmployeeKey getTaskEmployeeKey() {
        return TaskEmployeeKey;
    }

    public void setTaskEmployeeKey(TaskEmployeeKey taskEmployeeKey) {
        TaskEmployeeKey = taskEmployeeKey;
    }

    public Tasks getTasks() {
        return tasks;
    }

    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }

    public Employees getEmployees() {
        return employees;
    }

    public void setEmployees(Employees employees) {
        this.employees = employees;
    }

    public Status getComplete() {
        return complete;
    }

    public void setComplete(Status complete) {
        this.complete = complete;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @PrePersist
    public void prePersist() {
        if (this.complete == null) {
            this.complete = Status.DEFAULT;
        }
    }
}
