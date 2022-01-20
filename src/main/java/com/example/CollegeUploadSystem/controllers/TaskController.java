package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.Task;
import com.example.CollegeUploadSystem.models.Views;
import com.example.CollegeUploadSystem.services.TaskService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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
    public List<Task> getByGroupId(@PathVariable("groupId") Long groupId) {
        return this.taskService.getByGroupId(groupId);
    }

    @PostMapping("/groups/{groupId}/tasks")
    @JsonView(Views.IdName.class)
    public ResponseEntity<Task> create(@PathVariable("groupId") Group groupFromDb, @RequestBody Task task) throws IOException {
        Task createdTask = this.taskService.create(task, groupFromDb);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PostMapping("/tasks/files")
    public ResponseEntity fileUpload(@RequestParam("file") MultipartFile file, @RequestParam("fileLocation") String fileLocation) throws IOException {
        this.taskService.uploadDescriptionFile(file, fileLocation);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

/*
    @PutMapping("/group/{groupId}/tasks/{taskId}")
    public Task update(
            @PathVariable("groupId") Group groupFromDb,
            @PathVariable("taskId") Task taskFromDb,
            @RequestPart("task") Task task,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        return this.taskService.updateTask(groupFromDb, taskFromDb, task, file, false);
    }
*/

    @PostMapping("/group/{group}/add_or_update_task")
    public String addOrUpdateTask(
            @RequestParam MultipartFile file,
            @RequestParam(name = "fileDeletion", required = false) boolean fileDeletion,
            @PathVariable Group group,
            @Valid @ModelAttribute("taskForm") Task taskForm,
            BindingResult bindingResult,
            Model model
    ) throws Exception {
        // return to the same page if any error appeared to exist there.
        if (!bindingResult.hasErrors()) {
            if (taskForm.getId() == null) {
//                this.taskService.addTask(taskForm, group, file);
            } else {
//                this.taskService.updateTask(taskForm, group, file, fileDeletion);
            }
            return "redirect:/group/" + group.getId() + "/students";
        }
        return "";
    }
}
