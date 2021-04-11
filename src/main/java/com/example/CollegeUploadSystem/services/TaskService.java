package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.Task;
import com.example.CollegeUploadSystem.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepo taskRepo;

    public List<Task> getByGroupId(Long groupId) {
        return this.taskRepo.findByGroupId(groupId);
    }

    public Task getById(Long taskId) {
        return this.taskRepo.findById(taskId).orElseThrow(NoResultException::new);
    }
}
