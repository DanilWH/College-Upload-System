package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.models.*;
import com.example.CollegeUploadSystem.services.GroupService;
import com.example.CollegeUploadSystem.services.StudentResultService;
import com.example.CollegeUploadSystem.services.TaskService;
import com.example.CollegeUploadSystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private StudentResultService studentResultService;

    @GetMapping("/group/{groupId}/students")
    public String students(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long groupId,
            Model model
    ) {
        Group group = this.groupService.getById(groupId);
        List<User> students = this.userService.getByGroupIdOrderByLastName(groupId);
        List<Task> tasks = this.taskService.getByGroupId(groupId);

        model.addAttribute("group", group);
        model.addAttribute("students", students);
        model.addAttribute("tasks", tasks);

        // put a new task form into the model in case the current user is admin.
        if (currentUser != null && currentUser.getUserRoles().contains(UserRoles.ADMIN)) {
            model.addAttribute("taskForm", new Task());
        }

        return "students";
    }

    @PostMapping("/upload")
    public String upload(
            @AuthenticationPrincipal User currentUser,
            @RequestParam Long groupId,
            @RequestParam Long taskId,
            @RequestParam MultipartFile file,
            Model model
    ) throws IOException {
        // check if the current user belongs to the appropriate group.
        if (!currentUser.getGroup().getId().equals(groupId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        StudentResult studentResult = this.studentResultService.getByTaskIdAndUserId(taskId, currentUser.getId());

        if (file != null && !file.isEmpty()) {
            // load the group and the task only if the student has a file to upload.
            Group group = this.groupService.getById(groupId);
            Task task = this.taskService.getById(taskId);

            // upload the file
            this.studentResultService.uploadStudentResult(currentUser, studentResult, group, task, file);
        } else {
            model.addAttribute("uploadError", "Пожалуйста выберите файл.");
        }

        return students(currentUser, groupId, model);
    }

    @GetMapping("/edit_user_profile")
    public String editUserProfile(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        model.addAttribute("user", user);

        return "edit_user_profile";
    }

    @PostMapping("/update_user_profile")
    public String updateUserProfile(
            @AuthenticationPrincipal User currentUser,
            @Valid @ModelAttribute("user") User userModel,
            BindingResult bindingResult
    ) {
        // the "The login ... already exists" rule is checked in the controller because of the inability to
        // put the current user session in the constraint validator and compare if the user has changed its
        // login or not so we know if we should check the user's new login existence in the database.
        if (!currentUser.getLogin().equals(userModel.getLogin())) {
            if (this.userService.getByLogin(userModel.getLogin()) != null) {
                FieldError error = new FieldError(bindingResult.getObjectName(),
                        "login",
                        "Логин " + userModel.getLogin() + " занят другим пользователем"
                );
                bindingResult.addError(error);
            }
        }

        // check if there are errors in the user form.
        if (bindingResult.hasErrors()) {
            return "edit_user_profile";
        }

        this.userService.updateUserProfile(currentUser, userModel);

        return "redirect:/";
    }
}
