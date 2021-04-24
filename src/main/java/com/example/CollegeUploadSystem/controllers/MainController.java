package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private GroupService groupService;

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

}
