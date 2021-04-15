package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.dto.StudentsCreationDto;
import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {
    @Value("${number.of.students}")
    private int numberOfStudents;

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
    public String newGroupSaving(@ModelAttribute("newGroupForm") Group group) {
        return "redirect:/";
    }
}
