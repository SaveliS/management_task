package com.managment.task.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.managment.task.model.AttributeTask;
import com.managment.task.model.Employees;
import com.managment.task.model.TaskEmployee;
import com.managment.task.model.TaskEmployee.TaskEmployeeKey;
import com.managment.task.model.TaskFiles;
import com.managment.task.model.Tasks;
import com.managment.task.repository.AttributeTaskRepository;
import com.managment.task.repository.EmployeeGroupsRepository;
import com.managment.task.repository.EmployeesRepository;
import com.managment.task.repository.GroupsRepository;
import com.managment.task.repository.TaskEmployeeRepository;
import com.managment.task.repository.TaskFilesRepository;
import com.managment.task.repository.TaskStatusRepository;
import com.managment.task.repository.TasksRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TaskService {

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskEmployeeRepository taskEmployeeRepository;

    @Autowired
    private EmployeesRepository employeesRepository;

    @Autowired
    private EmployeeGroupsRepository employeeGroupsRepository;

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private TaskFilesRepository taskFilesRepository;

    @Autowired
    private AttributeTaskRepository attributeTaskRepository;

    public TaskService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public boolean existsById(int id) {
        return tasksRepository.existsById(id);
    }

    public Tasks addNewTask(Tasks task) {
        return tasksRepository.save(task);
    }

    public Iterable<Tasks> findAllTask() {
        return tasksRepository.findAll();
    }

    public Iterable<Tasks> findAllTaskTemplate(){
        return tasksRepository.findAllByStatus(taskStatusRepository.findById(7).orElseThrow(() -> new EntityNotFoundException("Не найден статус")));
    }

    public Tasks findTaskById(int id) {
        return tasksRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Сущность не найдена"));
    }

    @Transactional
    public void prepareModelForSubTask(int id, Model model) {
        model.addAttribute("employee", employeesRepository.findAll());
        model.addAttribute("task_status", taskStatusRepository.findAll());
        Tasks subTasks = new Tasks();
        subTasks.setParentTasks(findTaskById(id));
        model.addAttribute("task", subTasks);
    }

    public void removeSubTaskInTask(int taskId, List<Integer> subTaskIds) {
        // Получаем задачу, к которой принадлежат подзадачи
        Tasks selectedTask = tasksRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        // Находим подзадачи для удаления
        List<Tasks> subTasksToRemove = selectedTask.getSubTasks().stream()
                .filter(subTask -> subTaskIds.contains(subTask.getTaskId()))
                .collect(Collectors.toList());

        if (subTasksToRemove.isEmpty()) {
            throw new EntityNotFoundException("SubTasks not found");
        }

        // Удаляем подзадачи из коллекции
        selectedTask.getSubTasks().removeAll(subTasksToRemove);

        // Удаляем подзадачи из базы данных
        for (Tasks subTask : subTasksToRemove) {
            for(TaskFiles taskFiles : taskFilesRepository.findAllByTaskId(subTask.getTaskId())){
                taskFilesRepository.delete(taskFiles);
            }
            tasksRepository.delete(subTask);
        }

        // Сохраняем изменения в родительской задаче
        tasksRepository.save(selectedTask);
    }

    public Iterable<Tasks> findAllTaskNotSubTasks() {
        return tasksRepository.findAll().stream()
                .filter(task -> task.getParentTasks() == null).toList();
    }

    public boolean updateTaskStatusIfAllComplete(int taskId) {
        List<TaskEmployee> taskEmployees = taskEmployeeRepository.findByTasks_TaskId(taskId);
        boolean allCompleteOrDeclined = taskEmployees.stream()
                .allMatch(te -> te.getComplete() == TaskEmployee.Status.ACCEPTED
                        || te.getComplete() == TaskEmployee.Status.REFUSED);

        if (allCompleteOrDeclined == false)
            return false;

        Tasks task = tasksRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        task.setStatus(taskStatusRepository.findById(3).orElseThrow(() -> new EntityNotFoundException("Status not found"))); 
        if (task.getSubTasks() != null) {
            updateParentTaskTaskStatusIfSubTaskCompleted(taskId);
        }
        task.setIsCompleted(true);
        tasksRepository.save(task);
        return true;
    }

    public boolean updateParentTaskTaskStatusIfSubTaskCompleted(int taskId) {
        Tasks parentTask = tasksRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        // Обвернуть в Optinal, надо было сразу так сделать
        for (Tasks value : parentTask.getSubTasks()) {
            if (!value.getIsCompleted()) {
                return false;
            }
        }
        return true;
    }

    public void updateTask(Tasks task) {
        Tasks taskInDB = tasksRepository.findById(task.getTaskId()).orElseThrow(() -> new EntityNotFoundException("Tasks not found"));
        taskInDB = task;
        tasksRepository.save(taskInDB);
    }

    public Tasks addNewTemplated(Tasks template,List<String> pinnedGroups, List<String> pinnedUser) {
        List<AttributeTask> attributeInTask = template.getAttributeTask();
        template.setAttributeTask(null);
        template = addNewTask(template);
        for (AttributeTask value : attributeInTask) {
            value.getAttributeTaskKey().setTaskId(template.getTaskId());
            attributeTaskRepository.save(value);
        }
        // Проблема: Если состав группы изменится то закрепленные пользователи не изменятся.

        // Если переданы группы, то разбиваем их на пользователей и закрепляем за шаблоном.
        if(pinnedGroups.isEmpty() == false){
            for (int i = 0; i < pinnedGroups.size(); i++) {
                List<Employees> employeeInGroup = employeeGroupsRepository.findAllEmployeesByIdGroups(Integer.parseInt(pinnedGroups.get(i)));
                for (Employees employees : employeeInGroup) {
                    TaskEmployee.TaskEmployeeKey key = new TaskEmployeeKey(template.getTaskId(), employees.getEmployeeId());
                    TaskEmployee newTaskEmployee = new TaskEmployee(key, template, employees, TaskEmployee.Status.DEFAULT, null);
                    taskEmployeeRepository.save(newTaskEmployee);
                }
            }
        }
        // Если переданы пользователи, то закрепляем их за шаблоном.
        if(pinnedUser.isEmpty() == false){
            for (int i = 0; i < pinnedUser.size(); i++) {
                Employees employee = employeesRepository.findById(Integer.parseInt(pinnedUser.get(i))).orElseThrow(() -> new EntityNotFoundException("Employee not found."));
                TaskEmployee.TaskEmployeeKey key = new TaskEmployeeKey(template.getTaskId(), employee.getEmployeeId());
                TaskEmployee newTaskEmployee = new TaskEmployee(key, template, employee, TaskEmployee.Status.DEFAULT, null);
                taskEmployeeRepository.save(newTaskEmployee);
            }
        }


        return template;
    }
}
