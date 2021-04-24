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
import org.springframework.validation.ObjectError;
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
        if (currentUser.getGroup().getId() != groupId) {
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
            @RequestParam("confirmPassword") String confirmPassword,
            BindingResult bindingResult
    ) {
        // check if the password match.
        if (confirmPassword != null && !userModel.getPassword().equals(confirmPassword)) {
            ObjectError objectError = new ObjectError("confirmPassword", "Пароли не совподают.");
            bindingResult.addError(objectError);
        }

        // check if there are errors in the user form.
        if (!userModel.getPassword().equals(confirmPassword) || bindingResult.hasErrors()) {
            return "edit_user_profile";
        }

        this.userService.updateUserProfile(currentUser, userModel);

        return "redirect:/";
    }
}
