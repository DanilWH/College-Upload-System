package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.repos.GroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Group> getAll() {
        return this.groupRepo.findAll();
    }

    public Group getById(Long groupId) {
        return this.groupRepo.findById(groupId).orElseThrow(NoResultException::new);
    }

    public void saveGroup(MultipartFile file, Group groupForm, User admin) throws IOException {
        groupForm.setStudents(new ArrayList<>());
        // the csv format is supposed to be in the following format "LastName,FirstName,FatherName".

        InputStream in = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String row;
        while ((row = br.readLine()) != null) {
            if (!row.isBlank()) {
                String[] data = row.split(",");

                User newStudent = new User();
                newStudent.setFirstName(data[1]);
                newStudent.setLastName(data[0]);
                newStudent.setFatherName(data[2]);

                groupForm.getStudents().add(newStudent);
            }
        }

        // save the group in the database.
        this.groupRepo.save(groupForm);
        // save the students that belongs to the group in the database.
        this.userService.addAllStudents(groupForm.getStudents(), groupForm, admin);

        // close the input stream.
        in.close();
    }
}
