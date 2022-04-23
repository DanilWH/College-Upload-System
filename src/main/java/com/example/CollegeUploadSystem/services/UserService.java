package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.dto.input.FullNameInput;
import com.example.CollegeUploadSystem.dto.input.ProfileLoginInput;
import com.example.CollegeUploadSystem.dto.input.ProfilePasswordInput;
import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.models.UserRoles;
import com.example.CollegeUploadSystem.repos.GroupRepo;
import com.example.CollegeUploadSystem.repos.UserRepo;
import com.example.CollegeUploadSystem.utils.ApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final GroupRepo groupRepo;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUtils applicationUtils;

    @Autowired
    public UserService(UserRepo userRepo, GroupRepo groupRepo, PasswordEncoder passwordEncoder, ApplicationUtils applicationUtils) {
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
        this.passwordEncoder = passwordEncoder;
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

    @Deprecated
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
        // the csv file format is supposed to be in the following format "LastName,FirstName,FatherName".
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

    public void updateFullName(Long id, FullNameInput fullNameInput) {
        User user = this.userRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        user.setFirstName(fullNameInput.getFirstName());
        user.setLastName(fullNameInput.getLastName());
        user.setFatherName(fullNameInput.getFatherName());

        this.userRepo.save(user);
    }

    public void updateLogin(Long id, ProfileLoginInput profileLoginInput) {
        User user = this.userRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // TODO: ask shashin if I need to check that only the current user can change his login.

        // check if the login exists in the database.
        User duplicateUser = this.userRepo.findByLogin(profileLoginInput.getLogin());
        if (duplicateUser != null && !duplicateUser.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The login is not unique");
        }

        user.setLogin(profileLoginInput.getLogin());

        this.userRepo.save(user);
    }

    public void updatePassword(User currentUser, Long id, ProfilePasswordInput profilePasswordInput) {
        User user = this.userRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // check additional security stuff for students.
        if (!currentUser.getUserRoles().contains(UserRoles.ADMIN)) {
            // a student can only change his own password.
            if (!currentUser.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A student can not change the password of anyone but its own one.");
            }
            // a student, when changing his password, has to enter the old password.
            if (!this.passwordEncoder.matches(profilePasswordInput.getOldPassword(), user.getPassword())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Old password is wrong");
            }
        }


        // encode the new password.
        String encryptedPassword = this.passwordEncoder.encode(profilePasswordInput.getPassword());

        user.setPassword(encryptedPassword);
        user.setPasswordChanger(currentUser);
        user.setPasswordChangeTime(LocalDateTime.now());

        this.userRepo.save(user);
    }

    public void deactivate(Long id) {
        User user = this.userRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        user.setPassword(null);

        this.userRepo.save(user);
    }

    public void deactivateAllByGroup(Long groupId) {
        // find the group by the ID, throw the 404 status code if the group not found.
        Group group = this.groupRepo.findById(groupId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such group"));
        group.getStudents().forEach((student) -> student.setPassword(null));
        this.userRepo.saveAll(group.getStudents());
    }

    public void deleteAllByGroup(Long groupId) {
        // here, we don't need to return the 404 status code if the group not found,
        // it'll be known with the group deletion input (the 2'nd sequential input).
        this.userRepo.deleteByGroupId(groupId);
    }
}
