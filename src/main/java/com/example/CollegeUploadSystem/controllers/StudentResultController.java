package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.dto.StudentResultDto;
import com.example.CollegeUploadSystem.mappers.StudentResultMapper;
import com.example.CollegeUploadSystem.models.StudentResult;
import com.example.CollegeUploadSystem.models.Task;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.models.Views;
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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/groups/{groupId}/student-results")
    public List<StudentResultDto> list(@PathVariable("groupId") Long groupId) {
        return this.studentResultService.getAllByGroupId(groupId)
                .stream()
                .map(this.studentResultMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/student-results/{studentResultId}/file")
    public ResponseEntity<Resource> download(@PathVariable("studentResultId") Long studentResultId) throws IOException {
        StudentResult studentResultFromDb = this.studentResultService.getById(studentResultId);

        // get the file as a Resource.
        Resource resource = this.studentResultService.getStudentResultFileAsResource(studentResultFromDb);

        // figure out the media type of the downloading file.
        String mediaType = this.applicationUtils.recognizeMediaType(resource.getFilename());

        // TODO: check the "inline" option.
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(mediaType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("/tasks/{taskId}/student-results")
    @JsonView(Views.IdName.class)
    public StudentResultDto upload(@AuthenticationPrincipal User currentUser, @PathVariable("taskId") Long taskId, @RequestParam("file") MultipartFile file) throws IOException {
        // find the originals in the database.
        Task taskFromDatabase = this.taskService.getById(taskId);

        // upload the file by the criterias of the current user's Group and the found Task.
        StudentResult newStudentResult = this.studentResultService.upload(currentUser, taskFromDatabase, file);

        return this.studentResultMapper.toDto(newStudentResult);
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @DeleteMapping("/student-results/{studentResultId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User currentUser, @PathVariable("studentResultId") Long studentResultId) {
        // find the original instance in the database.
        StudentResult studentResultFromDatabase = this.studentResultService.getById(studentResultId);

        // delete the file and its data in the database.
        this.studentResultService.delete(currentUser, studentResultFromDatabase);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
