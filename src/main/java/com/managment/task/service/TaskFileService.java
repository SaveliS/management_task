package com.managment.task.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.managment.task.model.TaskFiles;
import com.managment.task.repository.TaskFilesRepository;

import jakarta.transaction.Transactional;

@Service
public class TaskFileService {
    
    @Autowired
    private TaskFilesRepository taskFilesRepository;

    public TaskFileService (TaskFilesRepository taskFilesRepository){
        this.taskFilesRepository = taskFilesRepository;
    }

    public void addNewTaskFiles(TaskFiles taskFiles){
        taskFilesRepository.save(taskFiles);
    }

    public List<TaskFiles> findTaskFilesByIdTask(int idTask){
        return taskFilesRepository.findAllByTaskId(idTask);
    }

    public TaskFiles findTaskFilesByIdTaskAndIdFiles(int idTask, int idFile){
        return taskFilesRepository.findByTaskIdAndFileId(idTask, idFile);
    }

    @Transactional
    public void addNewTaskFiles(int taskId,MultipartFile [] files){
        try {
            for(MultipartFile value: files){
                byte [] byteFile = value.getBytes();
                Path path = Paths.get("D:\\Project\\file_task\\", value.getOriginalFilename());
    
                Files.write(path, byteFile);
                addNewTaskFiles(new TaskFiles(taskId, path.toString()));
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Transactional
    public boolean removeAllFilesInTask(int idTask, List<Integer> removeIdFiles){
        List<TaskFiles> allTaskFilesByIdTask = findTaskFilesByIdTask(idTask);
        List<TaskFiles> tasksFilesRemove = new ArrayList<>();

        for (Integer value : removeIdFiles) {
            boolean isRow = false;

            for (TaskFiles taskFiles : allTaskFilesByIdTask) {
                if(isRow) break;
                isRow = value.equals(taskFiles.getFileId());

                if(isRow) tasksFilesRemove.add(taskFiles);
            }

            if (isRow == false) return false;
        }

        for (TaskFiles taskFiles : tasksFilesRemove) {
            taskFilesRepository.delete(taskFiles);
        }

        return true;
    }
}
