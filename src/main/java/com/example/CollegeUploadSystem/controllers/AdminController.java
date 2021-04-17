package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.dto.StudentsCreationDto;
import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.Task;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.services.GroupService;
import com.example.CollegeUploadSystem.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    @Value("${number.of.students}")
    private int numberOfStudents;

    @Autowired
    private GroupService groupService;
    @Autowired
    private TaskService taskService;

    @GetMapping("/new_group")
    public String newGroupForm(Model model) {
        Group groupForm = new Group();
        List<User> students = new ArrayList<>();

        for (int i = 0; i < this.numberOfStudents; i++) {
            students.add(new User());
        }
        groupForm.setStudents(students);

        model.addAttribute("groupForm", groupForm);

        return "admin/newGroup";
    }

    @PostMapping("/new_group")
    public String newGroupSaving(
            @Valid @ModelAttribute("groupForm") Group groupForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "admin/newGroup";
        }

        this.groupService.saveGroup(groupForm);

        return "redirect:/";
    }

    @PostMapping("/group/{group}/add_task")
    public String addTask(
            @PathVariable Group group,
            @ModelAttribute("taskForm") Task taskForm
    ) {
        this.taskService.addTask(taskForm, group);

        return "redirect:/group/" + group.getId() + "/students";
    }
}
