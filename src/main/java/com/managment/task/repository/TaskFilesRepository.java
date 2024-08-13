package com.managment.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.managment.task.model.TaskFiles;

public interface TaskFilesRepository extends JpaRepository<TaskFiles,Integer>{
    List<TaskFiles> findAllByTaskId(int taskId);
    TaskFiles findByTaskIdAndFileId(int taskId,int fileId);
}
