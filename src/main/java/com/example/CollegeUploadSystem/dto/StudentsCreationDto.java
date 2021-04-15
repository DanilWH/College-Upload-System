package com.example.CollegeUploadSystem.dto;

import com.example.CollegeUploadSystem.models.User;

import java.util.List;

public class StudentsCreationDto {
    List<User> students;

    public void addStudent(User student) {
        this.students.add(student);
    }

    public List<User> getStudents() {
        return this.students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }
}
