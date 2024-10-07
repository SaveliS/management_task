package com.managment.task.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.managment.task.model.AttributeTask;
import com.managment.task.model.Employees;
import com.managment.task.model.Groups;
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
    private TaskFilesRepository taskFilesRepository;

    @Autowired
    private AttributeTaskRepository attributeTaskRepository;

    @Autowired
    private GroupsRepository groupsRepository;

    public TaskService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public boolean existsById(int id) {
        return tasksRepository.existsById(id);
    }

    public static void removeDuplicateEmployees(List<Employees> newUserInTask){
        Set<Integer> uniqueEmployeeIds = new HashSet<>();
        Iterator<Employees> iterator = newUserInTask.iterator();

        while(iterator.hasNext()){
            Employees employee = iterator.next();
            int employeeId = employee.getEmployeeId();

            if (!uniqueEmployeeIds.add(employeeId)) {
                // Если employeeId уже в Set, удаляем текущий элемент
                iterator.remove();
            }
        }
    }

    /**
     * @param task Новая задача, будет добавлена, если это НЕ шаблон
     * @return Созданная заадача
     */
    public Tasks addNewTask(Tasks task) {
        return tasksRepository.save(task);
    }

    public Tasks addNewTask(Tasks task, List<String> newGroup, List<String> newUser, UserDetails userDetails){
        // Закрепляем создателя задачи
        task.setCreatedBy(employeesRepository.findByLogin(userDetails.getUsername()).orElseThrow(() -> new EntityNotFoundException("User not found")));
        // Если задача, создана по шаблону то будет ID != 0
        if(task.getTaskId() == 0){
            addNewTask(task);
        }

        task.setTaskId(0);
        // Сущности групп 
        List<Groups> newGroupsInTask = new ArrayList<>(newGroup.size());
        for (int i = 0; i < newGroup.size(); i++) {
            newGroupsInTask.add(groupsRepository.findById(Integer.parseInt(newGroup.get(i))).orElseThrow(() -> new EntityNotFoundException("Group not found")));
        } 
        // Сущности сотрудников
        List<Employees> newUsersInTask = new ArrayList<>();
        for (int i = 0; i < newUser.size(); i++) {
            newUsersInTask.add(employeesRepository.findById(Integer.parseInt(newUser.get(i))).orElseThrow(() -> new EntityNotFoundException("Employee not found")));
        }
        // Добавляем сотрудников закрепленных за группой к списку ответсвенных (испольнителей)
        for (Groups value : newGroupsInTask) {
            newUsersInTask.addAll(employeeGroupsRepository.findAllEmployeesByIdGroups(value.getGroupId()));
        }
        // Добавляем сотрудников из сущности
        for (int i = 0; i < task.getEmployeeTask().size(); i++) {
           newUsersInTask.add(employeesRepository.findById(task.getEmployeeTask().get(i).getTaskEmployeeKey().getEmployeeId()).orElseThrow(() -> new EntityNotFoundException("Employee not found")));
        }
        // Возможен дубль пользователя из группы и отдельно привязанного
        removeDuplicateEmployees(newUsersInTask);

        // Закрепляем 0 ID, чтобы был создан уникальный ключ
        task.setTaskId(0);
        // Не можем закрепить пользотвателя за задачей, пока задача не создана
        task.setEmployeeTask(null);
        List<AttributeTask> attributeTask = task.getAttributeTask();
        // Не можем закрепить атрибуты за задачей, пока задача не создана
        task.setAttributeTask(null);
        task = addNewTask(task);

        //Закрепляем атрибуты за задачей
        for (AttributeTask value : attributeTask) {
            value.getAttributeTaskKey().setTaskId(task.getTaskId());
            attributeTaskRepository.save(value);
        }

        //Закрепляем пользователей за задачей
        for (int i = 0; i < newUsersInTask.size(); i++) {
            TaskEmployee.TaskEmployeeKey key = new TaskEmployeeKey(task.getTaskId(), newUsersInTask.get(i).getEmployeeId());
            TaskEmployee newTaskEmployee = new TaskEmployee(key, task, newUsersInTask.get(i), TaskEmployee.Status.DEFAULT, null);
            taskEmployeeRepository.save(newTaskEmployee);
        }

        return task;
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

    public boolean addNewFileInTasks(Tasks newTasks,List<MultipartFile> files){
        try {
            for(MultipartFile value: files){
                byte [] byteFile = value.getBytes();
                Path path = Paths.get("D:\\Project\\file_task\\", value.getOriginalFilename());

                Files.write(path, byteFile);
                taskFilesRepository.save(new TaskFiles(newTasks.getTaskId(), path.toString()));
            }
            return true;
        } catch (IOException  e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
}
