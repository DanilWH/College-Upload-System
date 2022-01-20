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

    public Task create(Task task, Group groupFromDb) throws IOException {
//        if (file != null && !file.isEmpty()) {
//            String filepathFilename = calculatePathToDescriptionFile(task, groupFromDb, file);
//            task.setTaskDescriptionFile(filepathFilename);
//        }

        task.setGroup(groupFromDb);
        task.setCreationDateTime(LocalDateTime.now());
        // we get the request body with the taskDescriptionFile filled with the original file name. That's why we pass
        // it for calculating the file's path.
        task.setDescriptionFile(calculatePathToDescriptionFile(task, groupFromDb, task.getDescriptionFile()));

        return this.taskRepo.save(task);
    }

//    public Task updateTask(Group groupFromDb, Task taskFromDb, Task task, MultipartFile file, boolean fileDeletion) throws Exception {
//        // we find the old version of the task and replace the necessary properties with new values.
//        // Then just store the new task version in the database.
//
//        Task oldTaskObj = this.taskRepo.findById(task.getId()).orElseThrow(NoResultException::new);
//
//        oldTaskObj.setName(task.getName());
//
//        // detach the junk description file if the checkbox is checked.
//        if (fileDeletion) {
//            this.applicationUtils.deleteFile(this.adminDirectory, oldTaskObj.getTaskDescriptionFile());
//            oldTaskObj.setTaskDescriptionFile(null);
//        }
//
//        // check if a new description file is loaded.
//        if (file != null && !file.isEmpty()) {
//            // delete the junk description file if a new description file is loaded and the checkbox isn't checked.
//            if (oldTaskObj.getTaskDescriptionFile() != null) {
//                this.applicationUtils.deleteFile(this.adminDirectory, oldTaskObj.getTaskDescriptionFile());
//            }
//
//            // attach the new descirption file.
//            String filepathFilename = calculatePathToDescriptionFile(task, groupFromDb, file);
//            oldTaskObj.setTaskDescriptionFile(filepathFilename);
//        }
//
//        return this.taskRepo.save(oldTaskObj);
//    }

    public void uploadDescriptionFile(MultipartFile file, String fileLocation) throws IOException {
        if (file != null && !file.isEmpty()) {
            this.applicationUtils.uploadMultipartFile(file, this.adminDirectory, fileLocation);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No file content.");
        }
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
