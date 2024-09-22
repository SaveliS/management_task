package com.managment.task.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskid")
    private int taskId;
    @ManyToOne
    @JoinColumn(name = "statusid", referencedColumnName = "statusid")
    private TaskStatus status;
    @Column(name = "taskname")
    private String taskName;
    @Column(name = "startdate")
    private Date startDate;
    @Column(name = "enddate")
    private Date endDate;
    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "createdby" , referencedColumnName = "employeeid")
    private Employees createdBy; // Создатель задачи

    @ManyToOne
    @JoinColumn(name = "responsible", referencedColumnName = "employeeid")
    private Employees responsible; // Ответсвенный испольнитель

    @OneToMany(mappedBy = "tasks", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttributeTask> attributeTask = new ArrayList<>();

    // @OneToMany
    // @JoinColumn(name = "taskfiles", referencedColumnName = "task_id")
    // private TaskFiles taskFiles;

    @Column(name = "iscompleted", nullable = false)
    private boolean isCompleted;
    @Column(name = "istemplated", nullable = false)
    private boolean isTemplated;

    @ManyToOne
    @JoinColumn(name = "parent_task_id")
    private Tasks parentTasks;

    @OneToMany(mappedBy = "parentTasks", cascade = CascadeType.ALL)
    private List<Tasks> subTasks = new ArrayList<>();

    public Tasks() {}

    public Tasks(TaskStatus status, String taskName, Date startDate, Date endDate, String comment, Employees createdBy,
            Employees responsible, boolean isCompleted, boolean isTemplated, Tasks parentTasks, List<Tasks> subTasks) {
        this.status = status;
        this.taskName = taskName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.comment = comment;
        this.createdBy = createdBy;
        this.responsible = responsible;
        this.isCompleted = isCompleted;
        this.isTemplated = isTemplated;
        this.parentTasks = parentTasks;
        this.subTasks = subTasks;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public TaskStatus getStatusId() {
        return status;
    }

    public void setStatusId(TaskStatus status) {
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }
    
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if(startDate == null) this.startDate = Date.valueOf(LocalDate.now());
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if(endDate == null) this.endDate = Date.valueOf(LocalDate.now());
        this.endDate = endDate;
    }

    public Employees getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Employees createdBy) {
        this.createdBy = createdBy;
    }

    public Employees getResponsible() {
        return responsible;
    }

    public void setResponsible(Employees responsible) {
        this.responsible = responsible;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Tasks getParentTasks() {
        return parentTasks;
    }

    public void setParentTasks(Tasks parentTasks) {
        this.parentTasks = parentTasks;
    }

    public List<Tasks> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<Tasks> subTasks) {
        this.subTasks = subTasks;
    }

    public boolean getIsTemplated() {
        return isTemplated;
    }

    public void setIsTemplated(boolean isTemplated) {
        this.isTemplated = isTemplated;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<AttributeTask> getAttributeTask() {
        return attributeTask;
    }

    public void setAttributeTask(List<AttributeTask> attributeTask) {
        this.attributeTask = attributeTask;
    }

    @Override
    public String toString() {
        return "Tasks [taskId=" + taskId + ", status=" + status + ", taskName=" + taskName + ", startDate=" + startDate
                + ", endDate=" + endDate + ", comment=" + comment + ", createdBy=" + createdBy + ", responsible="
                + responsible + ", attributeTask=" + attributeTask + ", isCompleted=" + isCompleted + ", isTemplated="
                + isTemplated + ", parentTasks=" + parentTasks + ", subTasks=" + subTasks + "]";
    }

    
    
    // public TaskFiles getTaskFiles() {
    //     return taskFiles;
    // }

    // public void setTaskFiles(TaskFiles taskFiles) {
    //     this.taskFiles = taskFiles;
    // }
}
