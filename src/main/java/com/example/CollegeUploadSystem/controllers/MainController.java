package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.StudentResult;
import com.example.CollegeUploadSystem.models.Task;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.repos.StudentResultRepo;
import com.example.CollegeUploadSystem.services.GroupService;
import com.example.CollegeUploadSystem.services.TaskService;
import com.example.CollegeUploadSystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

    @Value("${upload.path}")
    private String uploadPath;
    @Autowired
    private StudentResultRepo studentResultRepo;

    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;

    @GetMapping("/")
    public String main() {
        return "redirect:/groups";
    }

    @GetMapping("/groups")
    public String groups(
            Model model
    ) {
        List<Group> groups = this.groupService.getAll();

        model.addAttribute("groups", groups);

        return "groups";
    }

    @GetMapping("/group/{groupId}/students")
    public String students(
            @PathVariable() Long groupId,
            Model model
    ) {
        Group group = this.groupService.getById(groupId);
        List<User> students = this.userService.getByGroupIdOrderByLastName(groupId);
        List<Task> tasks = this.taskService.getByGroupId(groupId);

        model.addAttribute("group", group);
        model.addAttribute("students", students);
        model.addAttribute("tasks", tasks);

        return "students";
    }

    @PostMapping("/upload")
    public String upload(
            @AuthenticationPrincipal User currentUser,
            @RequestParam Long groupId,
            @RequestParam MultipartFile file,
            @RequestParam Long taskId,
            Model model
    ) throws IOException {
        StudentResult studentResult = this.studentResultRepo.findByTaskIdAndUserId(taskId, currentUser.getId());

        if (file != null && !file.isEmpty()) {
            // load the group and the task only if the student has a file to upload.
            Group group = this.groupService.getById(groupId);
            Task task = this.taskService.getById(taskId);

            // check if the student has already uploaded files that belongs to the same task.
            if (studentResult != null) {
                // if so, delete the old file.
                File fileObj = new File(this.uploadPath + studentResult.getFilepath() + studentResult.getFilename());
                fileObj.delete();

                // we don't delete the entity in the database because we'll just update the old one.
            }
            else {
                // create a new student result if the students hasn't uploaded a file yet.
                studentResult = new StudentResult();
            }

            // fill the fields of the new student result entity.
            studentResult.setDateTime(LocalDateTime.now());
            studentResult.setTask(task);
            studentResult.setUser(currentUser);

            // uploadPath is the root directory where all the upload are stored.
            // filepath is the directory specified by the group name and the task name.
            // filename is the unique file name.
            String filepath = String.format("/%s/%s/", group.getName(), task.getName());

            // create a new directory if doesn't exist.
            File fileObj = new File(this.uploadPath + filepath);
            if (!fileObj.exists()) {
                fileObj.mkdirs();
            }

            // create the file name.
            String filename = String.format("%s%s_%s_%s",
                    currentUser.getLastName(),
                    currentUser.getFirstName(),
                    UUID.randomUUID(),
                    file.getOriginalFilename()
            );

            // save the file in the directory.
            file.transferTo(new File(this.uploadPath + filepath + filename));

            studentResult.setFilename(filename);
            studentResult.setFilepath(filepath);

            this.studentResultRepo.save(studentResult);
        } else {
            model.addAttribute("uploadError", "Пожалуйста выберите файл.");
        }

        return students(groupId, model);
    }
}
