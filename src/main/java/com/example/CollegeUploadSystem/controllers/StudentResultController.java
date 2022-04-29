package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.StudentResult;
import com.example.CollegeUploadSystem.models.Task;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.services.GroupService;
import com.example.CollegeUploadSystem.services.StudentResultService;
import com.example.CollegeUploadSystem.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class StudentResultController {

    private final StudentResultService studentResultService;
    private final GroupService groupService;
    private final TaskService taskService;

    @Autowired
    public StudentResultController(StudentResultService studentResultService, GroupService groupService, TaskService taskService) {
        this.studentResultService = studentResultService;
        this.groupService = groupService;
        this.taskService = taskService;
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("groups/{groupId}/tasks/{taskId}/student-results")
    public ResponseEntity<Void> upload(@AuthenticationPrincipal User currentUser, @PathVariable("groupId") Long groupId, @PathVariable("taskId") Long taskId, @RequestParam("file") MultipartFile file) throws IOException {
        // find the originals in the database.
        Group groupFromDatabase = this.groupService.findById(groupId);
        Task taskFromDatabase = this.taskService.getById(taskId);

        // upload the file by the criterias of the found Group and Task.
        this.studentResultService.upload(currentUser, groupFromDatabase, taskFromDatabase, file);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @DeleteMapping("/student-results/{studentResultId}")
    public ResponseEntity<Void> delete(@PathVariable("studentResultId") Long studentResultId) {
        // find the original instance in the database.
        StudentResult studentResultFromDatabase = this.studentResultService.getById(studentResultId);

        // delete the file and its data in the database.
        this.studentResultService.delete(studentResultFromDatabase);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
