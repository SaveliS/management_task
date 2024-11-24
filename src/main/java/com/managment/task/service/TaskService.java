package com.managment.task.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.managment.task.model.Employees;
import com.managment.task.model.Groups;
import com.managment.task.model.TaskEmployee;
import com.managment.task.model.TaskFiles;
import com.managment.task.model.Tasks;
import com.managment.task.model.TaskEmployee.TaskEmployeeKey;
import com.managment.task.repository.EmployeesRepository;
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
    private EmployeeService employeeService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskEmployeeRepository taskEmployeeRepository;

    @Autowired
    private EmployeesRepository employeesRepository;

    @Autowired
    private TaskFilesRepository taskFilesRepository;

    public TaskService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public boolean existsById(int id) {
        return tasksRepository.existsById(id);
    }

    /**
     * @param task Новая задача, будет добавлена, если это НЕ шаблон
     * @return Созданная задача
     */
    public Tasks addNewTask(Tasks task) {
        return tasksRepository.save(task);
    }

    public Tasks addNewTask(Tasks task, List<String> newGroup, List<String> newUser, UserDetails userDetails){
        // Закрепляем создателя задачи
        task.setCreatedBy(
            employeeService.findByLogin(userDetails.getUsername())
            .orElseThrow(
                () -> new EntityNotFoundException("User not found")
            )
        );

        // Если задача, создана по шаблону то будет ID != 0
        if(task.getTaskId() == 0){
            addNewTask(task);
        }
        // Если статус = Шаблон, то переопределяем на 'Создана'
        if(task.getStatus().getStatusId() == 7){
            task.setStatus(taskStatusService.findById(1));
        }

        task.setTaskId(0);
        

        // Сущности групп 
        List<Groups> newGroupsInTask = newGroup.stream()
                .map(id -> groupService.findGroupById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Entity not found")))
                .collect(Collectors.toList());
                
        // Сущности сотрудников
        List<Employees> newUsersInTask = newUser.stream()
                .map(id -> employeeService.findEmployeeById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Employee not found")))
                .collect(Collectors.toList());

        // Добавляем сотрудников закрепленных за группой к списку ответсвенных (испольнителей)
        newGroupsInTask.forEach(group -> 
            newUsersInTask.addAll(
                    employeeService.findEmployeeGroupsById(
                        String.valueOf(group.getGroupId())
                    )
                )
            );
       
        // Добавляем сотрудников из задачи
        task.getEmployeeTask().forEach(employee -> 
            newUsersInTask.add(
                employeeService.findEmployeeById(
                        String.valueOf(employee.getTaskEmployeeKey().getEmployeeId())
                    )
                .orElseThrow(
                    () -> new EntityNotFoundException("Employee not found")
                )
            )
        );

        task.getAttributeTask().forEach(attribute -> 
            attribute.setTasks(task)
        );

        // Возможен дубль пользователя из группы и отдельно привязанного
        EmployeeService.removeDuplicateEmployees(newUsersInTask);

        Tasks createdTask = addNewTask(task);

        return createdTask;
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
        
        template.getAttributeTask().forEach(attribute -> 
            attribute.setTasks(template)
        );

        // Если переданы группы, то разбиваем их на пользователей и закрепляем за шаблоном.
        List<Employees> employeesInTempalate = pinnedGroups.stream()
                .flatMap(id -> employeeService.findEmployeeGroupsById(id.toString()).stream())
                .collect(Collectors.toList());

        // Если переданы пользователи, то закрепляем их за шаблоном.
        pinnedUser.forEach(userId -> {
            Employees employees = employeeService.findByIdEmployee(Integer.parseInt(userId));
            employeesInTempalate.add(employees);
        });

        EmployeeService.removeDuplicateEmployees(employeesInTempalate);

        employeesInTempalate.forEach(user -> {
            TaskEmployeeKey key = new TaskEmployeeKey(template.getTaskId(), user.getEmployeeId());
            TaskEmployee newTaskEmployee = new TaskEmployee(key, template, user, TaskEmployee.Status.DEFAULT, null);
            template.getEmployeeTask().add(newTaskEmployee);
        });

        tasksRepository.save(template);

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
