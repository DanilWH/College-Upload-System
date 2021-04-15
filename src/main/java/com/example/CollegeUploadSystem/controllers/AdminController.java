package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.models.Group;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/new_group")
    public String newGroupForm(Model model) {
        model.addAttribute("newGroupForm", new Group());

        return "admin/newGroup";
    }

    @PostMapping("/new_group")
    public String newGroupSaving(@ModelAttribute("newGroupForm") Group group) {
        return "redirect:/";
    }
}
