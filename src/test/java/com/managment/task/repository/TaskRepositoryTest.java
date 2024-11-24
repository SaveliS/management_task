package com.managment.task.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.managment.task.model.AttributeTask;
import com.managment.task.model.AttributeTask.AttributeTaskKey;
import com.managment.task.model.TaskEmployee.TaskEmployeeKey;
import com.managment.task.model.Employees;
import com.managment.task.model.TaskEmployee;
import com.managment.task.model.TaskStatus;
import com.managment.task.model.Tasks;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class TaskRepositoryTest {
    
    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private EmployeesRepository employeesRepository;

    @Test
    public void testSaveTaskFinal(){
        TaskStatus status = taskStatusRepository.findById(8).get();

        Employees creator = employeesRepository.findById(2).get();
        Employees responsible = employeesRepository.findById(1).get();

        Tasks task = new Tasks();
        task.setTaskName("Test Task");
        task.setStatus(status);
        task.setStartDate(Date.valueOf(LocalDate.now()));
        task.setEndDate(Date.valueOf(LocalDate.now().plusDays(5)));
        task.setComment("This is a test task.");
        task.setCreatedBy(creator);
        task.setResponsible(responsible);
        task.setIsCompleted(false);
        task.setIsTemplated(false);

        TaskEmployeeKey key1 = new TaskEmployeeKey(task.getTaskId(), creator.getEmployeeId());
        TaskEmployee task_employee_1 = new TaskEmployee(key1,task,creator,TaskEmployee.Status.DEFAULT,"TEST");

        TaskEmployeeKey key2 = new TaskEmployeeKey(task.getTaskId(), creator.getEmployeeId());
        TaskEmployee task_employee_2 = new TaskEmployee(key2,task,responsible,TaskEmployee.Status.DEFAULT,"TEST TWO");

        task.getEmployeeTask().add(task_employee_1);
        task.getEmployeeTask().add(task_employee_2);
    }

    @Test
    public void testSaveTaskInEmployee(){
        TaskStatus status = taskStatusRepository.findById(8).get();

        Employees creator = employeesRepository.findById(2).get();
        Employees responsible = employeesRepository.findById(1).get();

        Tasks task = new Tasks();
        task.setTaskName("Test Task");
        task.setStatus(status);
        task.setStartDate(Date.valueOf(LocalDate.now()));
        task.setEndDate(Date.valueOf(LocalDate.now().plusDays(5)));
        task.setComment("This is a test task.");
        task.setCreatedBy(creator);
        task.setResponsible(responsible);
        task.setIsCompleted(false);
        task.setIsTemplated(false);

        TaskEmployeeKey key1 = new TaskEmployeeKey(task.getTaskId(), creator.getEmployeeId());
        TaskEmployee task_employee_1 = new TaskEmployee(key1,task,creator,TaskEmployee.Status.DEFAULT,"TEST");

        TaskEmployeeKey key2 = new TaskEmployeeKey(task.getTaskId(), creator.getEmployeeId());
        TaskEmployee task_employee_2 = new TaskEmployee(key2,task,responsible,TaskEmployee.Status.DEFAULT,"TEST TWO");

        task.getEmployeeTask().add(task_employee_1);
        task.getEmployeeTask().add(task_employee_2);

        AttributeTaskKey key1AttributeTaskKey = new AttributeTaskKey(task.getTaskId(), "Priority");
        AttributeTask attribute1 = new AttributeTask(key1AttributeTaskKey, "High", task);

        AttributeTaskKey key2AttributeTaskKey = new AttributeTaskKey(task.getTaskId(), "Category");
        AttributeTask attribute2 = new AttributeTask(key2AttributeTaskKey, "Development", task);

        task.getAttributeTask().add(attribute1);
        task.getAttributeTask().add(attribute2);

        Tasks savedTasks = tasksRepository.save(task);

        assertNotNull(savedTasks);
        assertTrue(savedTasks.getTaskId() > 0);
        assertTrue(savedTasks.getTaskId() != null);
        assertEquals(2, savedTasks.getEmployeeTask().size());
        assertEquals(2, savedTasks.getAttributeTask().size());
    }

    @Test
    public void testSaveTaskInAttribute(){

        TaskStatus status = taskStatusRepository.findById(8).get();

        Employees creator = employeesRepository.findById(2).get();
        Employees responsible = employeesRepository.findById(1).get();

        Tasks task = new Tasks();
        task.setTaskName("Test Task");
        task.setStatus(status);
        task.setStartDate(Date.valueOf(LocalDate.now()));
        task.setEndDate(Date.valueOf(LocalDate.now().plusDays(5)));
        task.setComment("This is a test task.");
        task.setCreatedBy(creator);
        task.setResponsible(responsible);
        task.setIsCompleted(false);
        task.setIsTemplated(false);



        AttributeTaskKey key1 = new AttributeTaskKey(task.getTaskId(), "Priority");
        AttributeTask attribute1 = new AttributeTask(key1, "High", task);
        attribute1.setTasks(task);
        task.getAttributeTask().add(attribute1);
        //task.addAttribute(attribute1);

        AttributeTaskKey key2 = new AttributeTaskKey(task.getTaskId(), "Category");
        AttributeTask attribute2 = new AttributeTask(key2, "Development", task);
        attribute2.setTasks(task);
        task.getAttributeTask().add(attribute2);
        //task.addAttribute(attribute2);

        Tasks savedTask = tasksRepository.save(task);

        assertNotNull(savedTask);
        assertTrue(savedTask.getTaskId() > 0);
        assertEquals(2, savedTask.getAttributeTask().size());
    }

    @Test
    public void testSaveTask(){
        TaskStatus status = taskStatusRepository.findById(8).get();

        Employees creator = employeesRepository.findById(2).get();
        Employees responsible = employeesRepository.findById(1).get();

        Tasks task = new Tasks();
        task.setTaskName("Test Task");
        task.setStatus(status);
        task.setStartDate(Date.valueOf(LocalDate.now()));
        task.setEndDate(Date.valueOf(LocalDate.now().plusDays(5)));
        task.setComment("This is a test task.");
        task.setCreatedBy(creator);
        task.setResponsible(responsible);
        task.setIsCompleted(false);
        task.setIsTemplated(false);

        Tasks savedTask = tasksRepository.save(task);

        assertNotNull(savedTask);
        assertTrue(savedTask.getTaskId() > 0);
        assertEquals("Test Task", savedTask.getTaskName());
        assertEquals(status, savedTask.getStatus());
        assertEquals(creator, savedTask.getCreatedBy());
        assertEquals(responsible, savedTask.getResponsible());
    }
}
