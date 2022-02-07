package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.models.UserRoles;
import com.example.CollegeUploadSystem.repos.UserRepo;
import com.example.CollegeUploadSystem.utils.ApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Service
public class UserService implements UserDetailsService {
    public final PasswordEncoder passwordEncoder;
    public final UserRepo userRepo;
    public final ApplicationUtils applicationUtils;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepo userRepo, ApplicationUtils applicationUtils) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.applicationUtils = applicationUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = this.userRepo.findByLogin(login);

        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }

        return user;
    }

    public List<User> getByGroupIdOrderByLastName(Long groupId) {
        return this.userRepo.findByGroupIdOrderByLastName(groupId);
    }

    public User getByLogin(String login) {
        return this.userRepo.findByLogin(login);
    }

    public void updateUserProfile(User userToUpdate, User currentUser, User userModel) {
        // the admin has the right to change a student's first name, last name, and father's name.
        // but the student doesn't itself.
        if (currentUser.getUserRoles().contains(UserRoles.ADMIN)) {
            userToUpdate.setFirstName(userModel.getFirstName());
            userToUpdate.setLastName(userModel.getLastName());
            userToUpdate.setFatherName(userModel.getFatherName());
        }

        userToUpdate.setLogin(userModel.getLogin());

        // we don't want to change the user's password if the password is null.
        if (userModel.getPassword() != null) {
            userToUpdate.setPassword(passwordEncoder.encode(userModel.getPassword()));
            userToUpdate.setPasswordChanger(currentUser);
            userToUpdate.setPasswordChangeTime(LocalDateTime.now());
        }

        this.userRepo.save(userToUpdate);

        // dynamically update a logged user's session.
        this.applicationUtils.refreshCurrentUserSession();
    }

    public List<User> createNewUsers(MultipartFile csvFile, Group group, User admin) throws IOException {
        List<User> extractedUsers = new ArrayList<>();
        // get the bytes of the file.
        String fileContent = new String(csvFile.getBytes());
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

                    extractedUsers.add(newStudent);
                }
            }
        }

        // Sort the new student by their last names.
        extractedUsers.sort((user1, user2) -> user1.getLastName().compareTo(user2.getLastName()));
        // close the file buffer.
        csvContent.close();

        // save the students that belongs to the group in the database and return them.
        return this.generateAndSaveAll(extractedUsers, group, admin);
    }

    private List<User> generateAndSaveAll(List<User> students, Group group, User admin) {
        List<User> processedStudents = new ArrayList<>();

        // prepare the students of the new group for storing in the database.
        students.forEach((student) -> {
            // set the student login.
            student.setLogin(String.format("%s_%s%s%s",
                    student.getLastName(),
                    student.getFirstName().charAt(0),
                    student.getFatherName().charAt(0),
                    students.indexOf(student) + 1)
            );
            // encrypt the password and set it to the current student.
            // every student's login is the password by default.
            student.setPassword(passwordEncoder.encode(student.getLogin()));
            // set the creation time.
            student.setCreationTime(LocalDateTime.now());
            // set admin as the creator of the current student.
            student.setUserCreator(admin);
            // set the time of the student creation as the time which the password was changed at.
            student.setPasswordChangeTime(student.getCreationTime());
            // set the student creator as the student password changer.
            student.setPasswordChanger(student.getUserCreator());
            // set the role to the student.
            student.setUserRoles(Collections.singleton(UserRoles.STUDENT));
            // make the student belong to the group.
            student.setGroup(group);

            // add the student to the processedStudents list.
            processedStudents.add(student);
        });

        // save the new students of the new group.
        return this.userRepo.saveAll(processedStudents);
    }

    public void deactivate(User user) {
        user.setPassword(null);
        this.userRepo.save(user);
    }

    public void deactivateAllByGroup(Group group) {
        group.getStudents().forEach((student) -> student.setPassword(null));
        this.userRepo.saveAll(group.getStudents());
    }
}
