package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.repos.GroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        groupForm.setCreationDate(LocalDate.now());
        groupForm.setStudents(new ArrayList<>());
        // the csv format is supposed to be in the following format "LastName,FirstName,FatherName".

        // get the bytes of the file.
        String fileContent = new String(file.getBytes());
        // prepare the file content for reading.
        Scanner csvContent = new Scanner(fileContent);

        while (csvContent.hasNextLine()) {
            // read a single line of the csv file.
            String row = csvContent.nextLine();

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

        csvContent.close();
    }
}
