package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.ApplicationUtils;
import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.models.UserRoles;
import com.example.CollegeUploadSystem.services.GroupService;
import com.example.CollegeUploadSystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class CommonController {

    private final String FIELD_ERROR_MESSAGE = "Поле не должно быть пустым";

    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;

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
        model.addAttribute("groupForm", new Group());

        return "groups";
    }

    @GetMapping("/user/{user}/edit_profile")
    public String editUserProfile(
            @AuthenticationPrincipal User currentUser,
            @PathVariable("user") User initialUser,
            Model model
    ) {
        // throw the 403 error if a student tries to open someone else's profile.
        ApplicationUtils.protectAccessToUserProfile(currentUser, initialUser);

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
        ApplicationUtils.protectAccessToUserProfile(currentUser, initialUser);

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
            ApplicationUtils.fieldNotBlank(bindingResult, "firstName", userModel.getFirstName(), FIELD_ERROR_MESSAGE);
            ApplicationUtils.fieldNotBlank(bindingResult, "lastName", userModel.getLastName(), FIELD_ERROR_MESSAGE);
            ApplicationUtils.fieldNotBlank(bindingResult, "fatherName", userModel.getFatherName(), FIELD_ERROR_MESSAGE);
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

        // if the admin is editing a student's profile, redirect
        // the admin back to the same student group page.
        if (
                currentUser.getUserRoles().contains(UserRoles.ADMIN) &&
                !currentUser.getId().equals(initialUser.getId())
        ) {
            return "redirect:/group/" + initialUser.getGroup().getId() + "/students";
        } else {
            return "redirect:/";
        }
    }
}
