package com.managment.task.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.managment.task.model.TaskEmployee;
import com.managment.task.model.Tasks;
import com.managment.task.service.EmployeeService;
import com.managment.task.service.GroupService;
import com.managment.task.service.TaskEmployeeService;
import com.managment.task.service.TaskFileService;
import com.managment.task.service.TaskService;
import com.managment.task.service.TaskStatusService;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskFileService taskFileService;

    @Autowired
    private TaskEmployeeService taskEmployeeService;

    @Autowired
    private GroupService groupService;


    @GetMapping("/{id}")
    public String getTask(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails,Model model){
        List<TaskEmployee> allTaskEmployees = taskEmployeeService.findAllTaskEmployeesByIdTask(id);
        model.addAttribute("task_files", taskFileService.findTaskFilesByIdTask(id));
        model.addAttribute("aut_employee", employeeService.isUserPerformer(allTaskEmployees, userDetails.getUsername()));
        model.addAttribute("task_employee", allTaskEmployees);
        model.addAttribute("task",taskService.findTaskById(id));
        model.addAttribute("isUserAction", TaskEmployee.Status.DEFAULT);
        return "task/taskDetails";
    }

    @PostMapping("/{id}")
    public String changeTask(@PathVariable int id, @RequestParam("userAction") TaskEmployee.Status userAction, @RequestParam("comment") String comment,
        @AuthenticationPrincipal UserDetails userDetails, Model model){
        taskEmployeeService.changeEmployeeStatusTask(id, employeeService.findByLogin(userDetails.getUsername()).get().getEmployeeId(), userAction, comment);
        taskService.updateTaskStatusIfAllComplete(id);
        return "redirect:/tasks/" + id;
    }

    @DeleteMapping("/{id}/removeSubTask")
    @ResponseBody
    public ResponseEntity<String> removeSubTask(@PathVariable("id") int taskId, @RequestBody Map<String,List<Integer>> removeSubTask){
        taskService.removeSubTaskInTask(taskId, removeSubTask.get("taskId"));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/remove")
    @ResponseBody
    public ResponseEntity<String> removeFile(@PathVariable("id") int taskId, @RequestBody Map<String, List<Integer>> removeFiles){
        taskFileService.removeAllFilesInTask(taskId, removeFiles.get("taskIds"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/upload")
    @ResponseBody
    public ResponseEntity<String> fileUpdate(@PathVariable("id") int taskId,@RequestParam("files") MultipartFile[] files){
        taskFileService.addNewTaskFiles(taskId, files);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{taskId}/{fileId}")
    public ResponseEntity<Resource> donwloadFile(@PathVariable int taskId, @PathVariable int fileId){
        try {
            Path file = Paths.get(taskFileService.findTaskFilesByIdTaskAndIdFiles(taskId, fileId).getPathFile());
            Resource resource = new UrlResource(file.toUri());

            String encodeFileName = URLEncoder.encode(resource.getFilename(), StandardCharsets.UTF_8.toString());

            if(resource.exists() && resource.isReadable()){
                return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,  "attachment; filename*=UTF-8''" + encodeFileName)
                    .body(resource);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/new")
    public String createSubTask(@PathVariable int id,Model model, RedirectAttributes redirectAttributes){
        if(!taskService.existsById(id)){
            redirectAttributes.addFlashAttribute("message","Родитель не найден");
            return "redirect:/";
        }
        taskService.prepareModelForSubTask(id, model);
        return "task/newTask";
    }
    
    @GetMapping("/new")
    public String createTask(@RequestParam(name = "templateId", required = false) Integer idTemplate, Model model){
        if(idTemplate != null && idTemplate.equals(idTemplate)){
            model.addAttribute("task", taskService.findTaskById(idTemplate));
        }else{
            model.addAttribute("task", new Tasks());
        }
        model.addAttribute("groupsEmpl", groupService.findAllGroup());
        model.addAttribute("employee", employeeService.findAllEmployee());
        model.addAttribute("task_status", taskStatusService.findAllTaskStatus());
        model.addAttribute("templates", taskService.findAllTaskTemplate());
        model.addAttribute("selectedResponsible", new String(""));
        return "task/newTask";
    }
    
    /**
     * @param task - Задача которую необходимо создать 
     * @param file - Файл закрепленный за задачей
     * @param newGroup - Новые пользователи, которые необходимы при создании по шаблону
     * @param newUsers - Новые группы, которые необходимы при создании по шаблону
     * @param result - Валидация формы
     * @param redirectAttributes - Сообщение о состояниее задачи <Добавлена,Не добавлена>
     * @param model - Модель для возрата в случае ошибок
     * @return - Возврат на страницу всех задач
     */
    @PostMapping(value = "/new", params = "!templateId")
    public String createTask(@ModelAttribute("task") Tasks task, @RequestParam("fileUpload") List<MultipartFile> file,@RequestParam("selectedGroups") ArrayList<String> newGroup,@RequestParam("selectedUsers") ArrayList<String> newUsers,BindingResult result, RedirectAttributes redirectAttributes,@AuthenticationPrincipal UserDetails userDetails,Model model){
    
        // Todo: Переделать валидацию, либо удалить и проверять JS
        // if(file.isEmpty() || file.get(0).isEmpty()){
        //     model.addAttribute("messageFile", "Пожалуйста выберите файл");
        //     model.addAttribute("employee", employeeService.findAllEmployee());
        //     model.addAttribute("task_status", taskStatusService.findAllTaskStatus());
        //     model.addAttribute("task", task);
        //     return "task/newTask";
        // }

        // if(result.hasErrors()){
        //     model.addAttribute("employee", employeeService.findAllEmployee());
        //     model.addAttribute("task_status", taskStatusService.findAllTaskStatus());
        //     model.addAttribute("task", task);
        //     return "task/newTask";
        // }

        Tasks newTasks = taskService.addNewTask(task,newGroup,newUsers,userDetails);

        if (taskService.addNewFileInTasks(newTasks, file)) redirectAttributes.addFlashAttribute("messageFile", "Файл успешно сохранен");

        return "redirect:/";
    }

    @PostMapping(value = "/new",params = "templateId" )
    public String createTask(@RequestParam(name = "templateId") int idTemplate){
        return "redirect:/tasks/new?templateId=" + idTemplate;
    }

    @GetMapping("/{id}/edit")
    public String editTask(@PathVariable int id, Model model){
        model.addAttribute("task", taskService.findTaskById(id));
        model.addAttribute("employee", employeeService.findAllEmployee());
        model.addAttribute("task_status", taskStatusService.findAllTaskStatus());
        return "task/editTask";
    }

    @PostMapping("/{id}/edit")
    public String editTask(@ModelAttribute("task") Tasks task,@PathVariable int id, RedirectAttributes redirectAttributes){
        task.setTaskId(id);
        System.out.println(task.toString());
        taskService.updateTask(task);
        redirectAttributes.addFlashAttribute("message","Редатирование успешно завершено.");
        return "redirect:/";
    }
}
