package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.ApplicationUtils;
import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.Task;
import com.example.CollegeUploadSystem.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Value("${admin.directory}")
    private String adminDirectory;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private ApplicationUtils applicationUtils;

    public List<Task> getByGroupId(Long groupId) {
        return this.taskRepo.findByGroupIdOrderByCreationDateTimeDesc(groupId);
    }

    public Task getById(Long taskId) {
        return this.taskRepo.findById(taskId).orElseThrow(NoResultException::new);
    }

    public void addTask(Task task, Group group, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String filepath = String.format("%s_%s/%s/", group.getName(), group.getCreationDate().getYear(), task.getName());
            String filename = String.format("%s_%s", UUID.randomUUID(), file.getOriginalFilename());
            this.applicationUtils.uploadMultipartFile(file, this.adminDirectory, filepath, filename);

            task.setTaskDescriptionFile(filepath + filename);
        }

        task.setGroup(group);
        task.setCreationDateTime(LocalDateTime.now());

        this.taskRepo.save(task);
    }
}
