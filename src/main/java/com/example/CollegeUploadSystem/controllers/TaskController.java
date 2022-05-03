package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.dto.TaskDto;
import com.example.CollegeUploadSystem.mappers.TaskMapper;
import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.Task;
import com.example.CollegeUploadSystem.models.Views;
import com.example.CollegeUploadSystem.services.GroupService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;
    private final GroupService groupService;
    private final TaskMapper taskMapper;
    private final ApplicationUtils applicationUtils;

    @Autowired
    public TaskController(TaskService taskService, GroupService groupService, TaskMapper taskMapper, ApplicationUtils applicationUtils) {
        this.taskService = taskService;
        this.groupService = groupService;
        this.taskMapper = taskMapper;
        this.applicationUtils = applicationUtils;
    }

    @GetMapping("/groups/{groupId}/tasks")
    @JsonView(Views.IdName.class)
    public List<TaskDto> getByGroupId(@PathVariable("groupId") Long groupId) {
        return this.taskService.getByGroupId(groupId)
                .stream()
                .map(this.taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/groups/{groupId}/tasks")
    @JsonView(Views.IdName.class)
    public TaskDto create(@PathVariable("groupId") Long groupId, @Valid @RequestBody TaskDto taskDto) {
        // get the original group copy from the database.
        Group groupFromDb = this.groupService.findById(groupId);

        // convert the TaskDto the its entity.
        Task task = this.taskMapper.toEntity(taskDto);

        // save the new task in the database.
        Task createdTask = this.taskService.create(task, groupFromDb);

        // convert the created task to the DTO and return it.
        return this.taskMapper.toDto(createdTask);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/tasks/{taskId}")
    @JsonView(Views.IdName.class)
    public synchronized TaskDto update(@PathVariable("taskId") Long taskId, @Valid @RequestBody TaskDto taskDto) {
        // find the original version of the task in the database.
        Task taskFromDb = this.taskService.getById(taskId);

        // convert the DTO version to the entity.
        Task task = this.taskMapper.toEntity(taskDto);

        // save the changed in the database.
        Task updatedTask = this.taskService.update(taskFromDb, task);

        // convert the entity to the dto and return it.
        return this.taskMapper.toDto(updatedTask);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> delete(@PathVariable("taskId") Long taskId) {
        this.taskService.delete(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*** DESCRIPTION FILES PROCESSING. ***/


    @GetMapping("/tasks/{taskId}/file")
    public ResponseEntity<Resource> fileDownload(@PathVariable("taskId") Long taskId) throws MalformedURLException {
        Task taskFromDb = this.taskService.getById(taskId);

        Resource resource = this.taskService.getDescriptionFileAsResource(taskFromDb);
        String mediaType = this.applicationUtils.recognizeMediaType(taskFromDb.getDescriptionFile());

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(mediaType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/tasks/{taskId}/file")
    @JsonView(Views.IdName.class)
    public synchronized TaskDto fileUpload(@PathVariable("taskId") Long taskId, @RequestParam("file") MultipartFile file) throws IOException {
        // find the original version of the task in the database.
        Task taskFromDb = this.taskService.getById(taskId);

        // attach the additional file to the task, set the file location path to the "descriptionFile" field,
        // save the updated version of the task in the database.
        Task taskWithAttachedFile = this.taskService.attachFileToTask(taskFromDb, file);

        // convert the task to the DTO and return it.
        return this.taskMapper.toDto(taskWithAttachedFile);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/tasks/{taskId}/file")
    @JsonView(Views.IdName.class)
    public synchronized TaskDto fileDeletion(@PathVariable("taskId") Long taskId) throws Exception {
        // find the original version of the task in the database.
        Task taskFromDb = this.taskService.getById(taskId);

        // detach the description file from the task and delete it from the server.
        Task taskWithoutFile = this.taskService.deleteFileFromTask(taskFromDb);

        // convert the task to the DTO and return it.
        return this.taskMapper.toDto(taskWithoutFile);
    }
}
