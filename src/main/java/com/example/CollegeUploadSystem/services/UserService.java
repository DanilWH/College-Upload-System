package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.models.UserRoles;
import com.example.CollegeUploadSystem.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo userRepo;

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

    public User getById(Long userId) {
        return this.userRepo.findById(userId).orElseThrow(NoResultException::new);
    }

    public User getByLogin(String login) {
        return this.userRepo.findByLogin(login);
    }

    public void updateUserProfile(User userToUpdate, User currentUser, User userModel) {
        // the admin has the right to change a student's first name, last name, and father's name.
        // but the student doesn't itself.
        if (userToUpdate.getUserRoles().contains(UserRoles.ADMIN)) {
            userToUpdate.setFirstName(userModel.getFirstName());
            userToUpdate.setLastName(userModel.getLastName());
            userToUpdate.setFatherName(userToUpdate.getFatherName());
        }

        userToUpdate.setLogin(userModel.getLogin());

        // we don't want to change the user's password if the password is null.
        if (userModel.getPassword() != null) {
            userToUpdate.setPassword(passwordEncoder.encode(userModel.getPassword()));
            userToUpdate.setPasswordChanger(currentUser);
            userToUpdate.setPasswordChangeTime(LocalDateTime.now());
        }

        this.userRepo.save(userToUpdate);
    }

    public void addAllStudents(List<User> students, Group group, User admin) {
        List<User> processedStudents = new ArrayList<>();

        // prepare the students of the new group for storing in the database.
        students.forEach((student) -> {
            if (student != null && !(student.getFirstName() + student.getLastName() + student.getFatherName()).isEmpty()) {
                // set the student login.
                student.setLogin(String.format("%s_%s%s%s",
                        student.getLastName(),
                        student.getFirstName().charAt(0),
                        student.getFatherName().charAt(0),
                        students.indexOf(student))
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
            }
        });

        // save the new students of the new group.
        this.userRepo.saveAll(processedStudents);
    }
}
