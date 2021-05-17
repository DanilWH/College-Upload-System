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

    private final String FIELD_ERROR_MESSAGE = "Поле не должно быть пустым";

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

    @GetMapping("/user/{user}/edit_profile")
    public String editUserProfile(
            @AuthenticationPrincipal User currentUser,
            @PathVariable("user") User initialUser,
            Model model
    ) {
        // throw the 403 error if a student tries to open someone else's profile.
        protectAccessToUserProfile(currentUser, initialUser);

        model.addAttribute("user", initialUser);

        return "edit_user_profile";
    }

    @PostMapping("/user/{userId}/update_profile")
    public String updateUserProfile(
            @AuthenticationPrincipal User currentUser,
            @PathVariable("userId") User initialUser, // TODO make the user.id a hidden filed and call this with @RequestParam
            @Valid @ModelAttribute("user") User userModel,
            BindingResult bindingResult
    ) {
        // throw the 403 error if a student tries to edit someone else's profile.
        protectAccessToUserProfile(currentUser, initialUser);

        // the "The login ... already exists" rule is checked in the controller because of the inability to
        // put the current user session in the constraint validator and compare if the user has changed its
        // login or not so we know if we should check the user's new login existence in the database.
        if (!initialUser.getLogin().equals(userModel.getLogin())) {
            if (this.userService.getByLogin(userModel.getLogin()) != null) {
                FieldError error = new FieldError(bindingResult.getObjectName(),
                        "login",
                        "Логин " + userModel.getLogin() + " занят другим пользователем"
                );
                bindingResult.addError(error);
            }
        }

        // because the admin can edit a user's full name
        // we check for @NotBlank the user's full name fields if the current user is admin.
        if (currentUser.getUserRoles().contains(UserRoles.ADMIN)) {
            fieldNotBlank(bindingResult, "firstName", userModel.getFirstName(), FIELD_ERROR_MESSAGE);
            fieldNotBlank(bindingResult, "lastName", userModel.getLastName(), FIELD_ERROR_MESSAGE);
            fieldNotBlank(bindingResult, "fatherName", userModel.getFatherName(), FIELD_ERROR_MESSAGE);
        }

        // check if there are errors in the user form.
        if (bindingResult.hasErrors()) {
            // because disabled fields don't return their values, we put the user full name in the context
            // in order to show the in the fields again.
            userModel.setId(initialUser.getId());
            userModel.setFirstName(initialUser.getFirstName());
            userModel.setLastName(initialUser.getLastName());
            userModel.setFatherName(initialUser.getFatherName());

            return "edit_user_profile";
        }

        // if the user is editing its own profile.
        if (currentUser.getId().equals(initialUser.getId())) {
            this.userService.updateUserProfile(currentUser, currentUser, userModel);
        } else { // if the admin is editing someone's profile.
            this.userService.updateUserProfile(initialUser, currentUser, userModel);
        }

        return "redirect:/";
    }

    /**
     * Throws the 403 (Forbidden) error if a student tries to edit someone else's profile.
     *
     * @return void
     */
    private void protectAccessToUserProfile(User currentUser, User initialUser) {
        if (currentUser.getUserRoles().contains(UserRoles.STUDENT) && !currentUser.getId().equals(initialUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Checks if a field value of the binding object is blank
     * and adds an error into the binding result if true.
     *
     * @return void
     */
    private void fieldNotBlank(
            BindingResult bindingResult,
            String fieldName,
            String fieldValue,
            String defaultMessage
    ) {
        if (fieldValue.isBlank()) {
            FieldError error = new FieldError(bindingResult.getObjectName(), fieldName, defaultMessage);
            bindingResult.addError(error);
        }
    }
}
