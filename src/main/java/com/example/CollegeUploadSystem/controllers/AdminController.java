package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.Task;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.services.GroupService;
import com.example.CollegeUploadSystem.services.TaskService;
import com.example.CollegeUploadSystem.services.UserService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

/*
    @GetMapping("/new_group")
    public String newGroupForm(Model model) {
        model.addAttribute("groupForm", new Group());

        return "admin/newGroup";
    }
*/

    @PostMapping("/new_group")
    public String newGroupSaving(
            @AuthenticationPrincipal User admin,
            @RequestParam MultipartFile file,
            @Valid @ModelAttribute("groupForm") Group groupForm,
            BindingResult bindingResult,
            Model model
    ) throws IOException {
        // check the correctness of the file extension.
        String fileExt = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!fileExt.equals("csv")) {
            model.addAttribute("fileError", "Не csv файл");
        }

        if (!fileExt.equals("csv") || bindingResult.hasErrors()) {
            List<Group> groups = this.groupService.getAll();

            model.addAttribute("isErrorPresent", true);
            model.addAttribute("groups", groups);
            model.addAttribute("groupForm", groupForm);

            return "groups";
        }

        this.groupService.saveGroup(file, groupForm, admin);

        return "redirect:/";
    }

    @PostMapping("/group/{group}/add_task")
    public String addTask(
            @PathVariable Group group,
            @Valid @ModelAttribute("taskForm") Task taskForm,
            BindingResult bindingResult,
            Model model
    ) {
        // return to the same page if any error appeared to exist there.
        if (!bindingResult.hasErrors()) {
            this.taskService.addTask(taskForm, group);
        } else {
            model.addAttribute("addNewTaskError", true);
        }

        List<User> students = this.userService.getByGroupIdOrderByLastName(group.getId());
        List<Task> tasks = this.taskService.getByGroupId(group.getId());

        model.addAttribute("group", group);
        model.addAttribute("students", students);
        model.addAttribute("tasks", tasks);

        return "students";
    }
}
