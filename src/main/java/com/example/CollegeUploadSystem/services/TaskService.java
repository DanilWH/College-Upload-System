package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.Task;
import com.example.CollegeUploadSystem.repos.TaskRepo;
import com.example.CollegeUploadSystem.utils.ApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private final String REGEXR_STRING = "([#%&{} /<>*? $!\\'\":@+`|=])";

    @Value("${admin.directory}")
    private String adminDirectory;

    private final TaskRepo taskRepo;
    private final ApplicationUtils applicationUtils;

    @Autowired
    public TaskService(TaskRepo taskRepo, ApplicationUtils applicationUtils) {
        this.taskRepo = taskRepo;
        this.applicationUtils = applicationUtils;
    }

    public List<Task> getByGroupId(Long groupId) {
        return this.taskRepo.findByGroupIdOrderByCreationDateTimeDesc(groupId);
    }

    public Task getById(Long taskId) {
        return this.taskRepo.findById(taskId).orElseThrow(NoResultException::new);
    }

    public Task create(Task task, Group groupFromDb) throws IOException {
        task.setGroup(groupFromDb);
        task.setCreationDateTime(LocalDateTime.now());
        // we get the input body with the taskDescriptionFile filled with the original file name. That's why we pass
        // it for calculating the file's path.
        task.setDescriptionFile(calculatePathToDescriptionFile(task, groupFromDb, task.getDescriptionFile()));

        return this.taskRepo.save(task);
    }

    public Task update(Task taskFromDb, Task task) {
        taskFromDb.setName(task.getName());
        taskFromDb.setDescriptionFile(task.getDescriptionFile());

        return this.taskRepo.save(taskFromDb);
    }

    public void delete(Long taskId) {
        Task taskFromDb = this.taskRepo.findById(taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        this.taskRepo.delete(taskFromDb);
    }

    public void uploadDescriptionFile(MultipartFile file, String fileLocation) throws IOException {
        if (file != null && !file.isEmpty()) {
            this.applicationUtils.uploadMultipartFile(file, this.adminDirectory, fileLocation);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No file content.");
        }
    }

    public void deleteDescriptionFile(String fileLocation) throws Exception {
        this.applicationUtils.deleteFile(this.adminDirectory, fileLocation);
    }

    private String calculatePathToDescriptionFile(Task taskForm, Group group, String originalFilename) {
        // replace all the prohibited symbols with the "-" symbol.
        String taskNameForFilename = taskForm.getName().replaceAll(REGEXR_STRING, "-");
        String originalFilenameForFilename = originalFilename.replaceAll(REGEXR_STRING, "-");

        // define the file path and the file name.
        String filepath = String.format("%s_%s/", group.getName(), group.getCreationDate().getYear());
        String filename = String.format("%s_%s_%s", taskNameForFilename, UUID.randomUUID(), originalFilenameForFilename);

        return filepath + filename;
    }

}
