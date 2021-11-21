package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.utils.ApplicationUtils;
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

    private final String REGEXR_STRING = "([#%&{} /<>*? $!\\'\":@+`|=])";

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

    public void addTask(Task taskForm, Group group, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String filepathFilename = uploadDescriptionFile(taskForm, group, file);
            taskForm.setTaskDescriptionFile(filepathFilename);
        }

        taskForm.setGroup(group);
        taskForm.setCreationDateTime(LocalDateTime.now());

        this.taskRepo.save(taskForm);
    }

    public void updateTask(Task taskForm, Group group, MultipartFile file, boolean fileDeletion) throws Exception {
        // we find the old version of the task and replace the necessary properties with new values.
        // Then just store the new task version in the database.

        Task oldTaskObj = this.taskRepo.findById(taskForm.getId()).orElseThrow(NoResultException::new);

        oldTaskObj.setName(taskForm.getName());

        // detach the junk description file if the checkbox is checked.
        if (fileDeletion) {
            this.applicationUtils.deleteFile(this.adminDirectory, oldTaskObj.getTaskDescriptionFile());
            oldTaskObj.setTaskDescriptionFile(null);
        }

        // check if a new description file is loaded.
        if (file != null && !file.isEmpty()) {
            // delete the junk description file if a new description file is loaded and the checkbox isn't checked.
            if (oldTaskObj.getTaskDescriptionFile() != null) {
                this.applicationUtils.deleteFile(this.adminDirectory, oldTaskObj.getTaskDescriptionFile());
            }

            // attach the new descirption file.
            String filepathFilename = uploadDescriptionFile(taskForm, group, file);
            oldTaskObj.setTaskDescriptionFile(filepathFilename);
        }

        this.taskRepo.save(oldTaskObj);
    }

    private String uploadDescriptionFile(Task taskForm, Group group, MultipartFile file) throws IOException {
        // replace all the prohibited symbols with the "-" symbol.
        String taskNameForFilename = taskForm.getName().replaceAll(REGEXR_STRING, "-");
        String originalFilenameForFilename = file.getOriginalFilename().replaceAll(REGEXR_STRING, "-");

        // define the file path and the file name.
        String filepath = String.format("%s_%s/", group.getName(), group.getCreationDate().getYear());
        String filename = String.format("%s_%s_%s", taskNameForFilename, UUID.randomUUID(), originalFilenameForFilename);
        this.applicationUtils.uploadMultipartFile(file, this.adminDirectory, filepath, filename);

        return filepath + filename;
    }
}
