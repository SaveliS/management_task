package com.managment.task.builder;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.managment.task.model.Employees;
import com.managment.task.model.Tasks;
import com.managment.task.service.TaskStatusService;

@Component
public class TasksDirector {

    @Autowired
    private TaskStatusService taskStatusService;

    private TasksBuilder tasksBuilder;

    public TasksDirector(TasksBuilder tasksBuilder) {
        this.tasksBuilder = tasksBuilder;
    }

    public Tasks consturctTasksTemplate(Employees responsible, Employees creator){
        return tasksBuilder
               .setName("Шаблон")
               .setCompleted(true)
               .setTemplated(true)
               .setStatus(taskStatusService.findById(7))
               .setStartDate(Date.valueOf(LocalDate.now()))
               .setEndDate(Date.valueOf(LocalDate.now()))
               .setCreatedBy(creator)
               .setResponsible(responsible)
               .setParentTasks(null)
               .setSubTasks(null)
               .setComment(null)
               .build();
    }
}
