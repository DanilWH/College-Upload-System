package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.Task;
import com.example.CollegeUploadSystem.repos.TaskRepo;
import com.example.CollegeUploadSystem.utils.ApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
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

    public Task getById(Long taskId) {
        return this.taskRepo.findById(taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The task with an ID of " + taskId + " was not found."));
    }

    public List<Task> getByGroupId(Long groupId) {
        return this.taskRepo.findByGroupIdOrderByCreationDateTimeDesc(groupId);
    }

    public Task create(Task task, Group groupFromDb) {
        task.setGroup(groupFromDb);
        task.setCreationDateTime(LocalDateTime.now());
        return this.taskRepo.save(task);
    }

    public Task update(Task taskFromDb, Task task) {
        taskFromDb.setName(task.getName());
        return this.taskRepo.save(taskFromDb);
    }

    public void delete(Long taskId) {
        Task taskFromDb = this.getById(taskId);

        // delete the task from the database first.
        this.taskRepo.delete(taskFromDb);

        // then, delete the task description file if it exists.
        if (taskFromDb.getDescriptionFileLocation() != null) {
            this.applicationUtils.deleteFile(this.adminDirectory, taskFromDb.getDescriptionFileLocation());
        }

    }

    public Resource getDescriptionFileAsResource(Task taskFromDb) throws MalformedURLException {
        if (taskFromDb.getDescriptionFileLocation() == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "There is no file attached to the task with an ID of" + taskFromDb.getId() + ".");
        }

        return this.applicationUtils.loadFileAsResource(this.adminDirectory, taskFromDb.getDescriptionFileLocation());
    }

    public Task attachFileToTask(Task taskFromDb, MultipartFile file) throws IOException {
        // check if the task already has a description file.
        if (taskFromDb.getDescriptionFileLocation() != null) {
            // TODO: find out (ask Shashin) if I need to just delete the old file instead of throwing an error.
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Another description file has already been uploaded to the task. Delete the old descriptoin file if you want to upload a new one.");
        }

        // calculate the path for the uploading description file.
        String fileLocation = this.calculatePathToDescriptionFile(taskFromDb, taskFromDb.getGroup(), file.getOriginalFilename());

        // upload the description file on the server.
        this.applicationUtils.uploadMultipartFile(file, this.adminDirectory, fileLocation);

        // set the path with the file location.
        taskFromDb.setDescriptionFileLocation(fileLocation);

        // save the task with the path to the description file in the database and return it.
        return this.taskRepo.save(taskFromDb);
    }

    public Task deleteFileFromTask(Task taskFromDb) {
        // store the task description file location in a variable to use it later when deleting the file.
        String descriptionFileLocation = taskFromDb.getDescriptionFileLocation();

        // set the "descriptionFileLocation" to null (detach the file from the task).
        taskFromDb.setDescriptionFileLocation(null);

        // save the version of the task without the description file in the database first.
        Task taskWithoutFile = this.taskRepo.save(taskFromDb);

        // then, delete the description file from the server.
        this.applicationUtils.deleteFile(this.adminDirectory, descriptionFileLocation);

        return taskWithoutFile;
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
