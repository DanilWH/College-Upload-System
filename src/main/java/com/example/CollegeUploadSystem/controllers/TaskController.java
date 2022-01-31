package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.Task;
import com.example.CollegeUploadSystem.models.Views;
import com.example.CollegeUploadSystem.services.TaskService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/groups/{groupId}/tasks")
    @JsonView(Views.IdName.class)
    public List<Task> getByGroupId(@PathVariable("groupId") Long groupId) {
        return this.taskService.getByGroupId(groupId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/groups/{groupId}/tasks")
    @JsonView(Views.IdName.class)
    public Task create(@PathVariable("groupId") Group groupFromDb, @RequestBody Task task) throws IOException {
        return this.taskService.create(task, groupFromDb);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/tasks/{taskId}")
    @JsonView(Views.IdName.class)
    public Task update(@PathVariable("taskId") Task taskFromDb, @RequestBody Task task) {
        return this.taskService.update(taskFromDb, task);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity delete(@PathVariable("taskId") Long taskId) {
        this.taskService.delete(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*** DESCRIPTION FILES PROCESSING. ***/

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/tasks/files")
    public ResponseEntity fileUpload(@RequestParam("file") MultipartFile file, @RequestParam("fileLocation") String fileLocation) throws IOException {
        this.taskService.uploadDescriptionFile(file, fileLocation);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/tasks/files")
    public ResponseEntity fileDeletion(@RequestParam("fileLocation") String fileLocation) throws Exception {
        this.taskService.deleteDescriptionFile(fileLocation);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
