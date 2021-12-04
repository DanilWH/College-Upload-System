package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.repos.GroupRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupRepo groupRepo;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public GroupService(GroupRepo groupRepo, UserService userService, PasswordEncoder passwordEncoder) {
        this.groupRepo = groupRepo;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Group> findAllActive() {
        // we list the active groups only.
        return this.groupRepo.findAll()
                .stream()
                .filter(Group::getActive)
                .collect(Collectors.toList());
    }

    public Group create(MultipartFile file, Group group, User admin) throws IOException {
        group.setCreationDate(LocalDate.now());
        // the csv format is supposed to be in the following format "LastName,FirstName,FatherName".

        // get the bytes of the file.
        String fileContent = new String(file.getBytes());
        // prepare the file content for reading.
        Scanner csvContent = new Scanner(fileContent);

        while (csvContent.hasNextLine()) {
            // read a single line of the csv file.
            String row = csvContent.nextLine();

            if (!row.isBlank()) {
                String[] data = row.split("([,;])");

                // do add a new student if the row contains the three part of the student's name.
                if (data.length == 3)  {
                    User newStudent = new User();
                    newStudent.setFirstName(data[1]);
                    newStudent.setLastName(data[0]);
                    newStudent.setFatherName(data[2]);

                    group.getStudents().add(newStudent);
                }
            }
        }

        // Sort the new student by their last names.
        group.getStudents().sort((user1, user2) -> user1.getLastName().compareTo(user2.getLastName()));

        // save the group in the database.
        Group savedGroup = this.groupRepo.save(group);
        // save the students that belongs to the group in the database.
        this.userService.addAllStudents(group.getStudents(), group, admin);

        csvContent.close();

        return savedGroup;
    }

    public Group update(Group groupFromDb, Group group) {
        groupFromDb.setName(group.getName());
        return this.groupRepo.save(groupFromDb);
    }

    public void delete(Group group) {
        group.setActive(false);
        this.groupRepo.save(group);
    }

    public Group convertToJson(String rawGroup) throws JsonProcessingException {
        return new ObjectMapper().readValue(rawGroup, Group.class);
    }

    /* Under a big question */
    public Group getById(Long groupId) {
        return this.groupRepo.findById(groupId).orElseThrow(NoResultException::new);
    }
}
