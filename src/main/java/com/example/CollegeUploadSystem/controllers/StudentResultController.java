package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.dto.StudentResultDto;
import com.example.CollegeUploadSystem.mappers.StudentResultMapper;
import com.example.CollegeUploadSystem.models.*;
import com.example.CollegeUploadSystem.services.GroupService;
import com.example.CollegeUploadSystem.services.StudentResultService;
import com.example.CollegeUploadSystem.services.TaskService;
import com.example.CollegeUploadSystem.utils.ApplicationUtils;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/api")
public class StudentResultController {

    private final StudentResultService studentResultService;
    private final GroupService groupService;
    private final TaskService taskService;
    private final StudentResultMapper studentResultMapper;
    private final ApplicationUtils applicationUtils;

    @Autowired
    public StudentResultController(StudentResultService studentResultService, GroupService groupService, TaskService taskService, StudentResultMapper studentResultMapper, ApplicationUtils applicationUtils) {
        this.studentResultService = studentResultService;
        this.groupService = groupService;
        this.taskService = taskService;
        this.studentResultMapper = studentResultMapper;
        this.applicationUtils = applicationUtils;
    }

    // TODO: DON'T FORGET TO IMPLEMENT THE GET REQUEST TO FETCH A LIST OF STUDENTS' RESULTS.

    @GetMapping("/student-results/{studentResultId}/files")
    public ResponseEntity<Resource> download(@PathVariable("studentResultId") Long studentResultId, HttpServletRequest request) throws MalformedURLException {
        // TODO: permit the endpoint for all the users.
        // get the file as a Resource.
        Resource resource = this.studentResultService.loadFileAsResource(studentResultId);

        // figure out the media type of the downloading file.
        String mediaType = this.applicationUtils.recognizeMediaType(resource.getFilename());

        // TODO: check the "inline" option.
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(mediaType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("groups/{groupId}/tasks/{taskId}/student-results")
    @JsonView(Views.IdName.class)
    public StudentResultDto upload(@AuthenticationPrincipal User currentUser, @PathVariable("groupId") Long groupId, @PathVariable("taskId") Long taskId, @RequestParam("file") MultipartFile file) throws IOException {
        // find the originals in the database.
        Group groupFromDatabase = this.groupService.findById(groupId);
        Task taskFromDatabase = this.taskService.getById(taskId);

        // upload the file by the criterias of the found Group and Task.
        StudentResult newStudentResult = this.studentResultService.upload(currentUser, groupFromDatabase, taskFromDatabase, file);

        return this.studentResultMapper.toDto(newStudentResult);
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
