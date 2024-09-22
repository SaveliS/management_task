package com.managment.task.builder;

import java.sql.Date;
import java.util.List;

import com.managment.task.model.Employees;
import com.managment.task.model.TaskStatus;
import com.managment.task.model.Tasks;

public interface TasksBuilder {
    TasksBuilder setName(String name);
    TasksBuilder setStatus(TaskStatus status);
    TasksBuilder setStartDate(Date startDate);
    TasksBuilder setEndDate(Date endDate);
    TasksBuilder setCreatedBy(Employees createByemployees);
    TasksBuilder setResponsible(Employees responsibleByemployees);
    TasksBuilder setCompleted(boolean isCompleted);
    TasksBuilder setTemplated(boolean isTemplated);
    TasksBuilder setParentTasks(Tasks parentTasks);
    TasksBuilder setSubTasks(List<Tasks> subTasks);
    TasksBuilder setComment(String comment);
    Tasks build();
}
