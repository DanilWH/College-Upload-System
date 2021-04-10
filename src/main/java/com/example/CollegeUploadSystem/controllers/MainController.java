package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.services.GroupService;
import com.example.CollegeUploadSystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MainController {

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

        return "groups";
    }

    @GetMapping("/group/{groupId}/students")
    public String students(
            @PathVariable() Long groupId,
            Model model
    ) {
        Group group = this.groupService.getById(groupId);
        List<User> students = this.userService.getByGroupIdOrderByLastName(groupId);

        model.addAttribute("group", group);
        model.addAttribute("students", students);

        return "students";
    }
}
