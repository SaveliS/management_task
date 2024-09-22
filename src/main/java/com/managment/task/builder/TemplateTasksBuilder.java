package com.managment.task.builder;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.managment.task.model.Employees;
import com.managment.task.model.TaskStatus;
import com.managment.task.model.Tasks;

@Component
public class TemplateTasksBuilder implements TasksBuilder{

    private Tasks templateTask = new Tasks();

    @Override
    public TasksBuilder setName(String name) {
        this.templateTask.setTaskName(name);
        return this;
    }

    @Override
    public TasksBuilder setStatus(TaskStatus status) {
        this.templateTask.setStatus(status);
        return this;
    }

    @Override
    public TasksBuilder setStartDate(Date startDate) {
        this.templateTask.setStartDate(startDate);
        return this;
    }

    @Override
    public TasksBuilder setEndDate(Date endDate) {
        this.templateTask.setEndDate(endDate);
        return this;
    }

    @Override
    public TasksBuilder setCreatedBy(Employees createByemployees) {
        this.templateTask.setCreatedBy(createByemployees);
        return this;
    }

    @Override
    public TasksBuilder setResponsible(Employees responsibleByemployees) {
        this.templateTask.setResponsible(responsibleByemployees);
        return this;
    }

    @Override
    public TasksBuilder setCompleted(boolean isCompleted) {
        this.templateTask.setIsCompleted(isCompleted);
        return this;
    }

    @Override
    public TasksBuilder setTemplated(boolean isTemplated) {
        this.templateTask.setIsTemplated(isTemplated);
        return this;
    }

    @Override
    public TasksBuilder setParentTasks(Tasks parentTasks) {
        this.templateTask.setParentTasks(parentTasks);
        return this;
    }

    @Override
    public TasksBuilder setSubTasks(List<Tasks> subTasks) {
        this.templateTask.setSubTasks(subTasks);
        return this;
    }

    @Override
    public TasksBuilder setComment(String comment) {
        this.templateTask.setComment(comment);
        return this;
    }

    @Override
    public Tasks build() {
        if(templateTask.getTaskName() == null || templateTask.getResponsible() == null){
            throw new IllegalStateException("");
        }
        return templateTask;
    }  
}
