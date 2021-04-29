package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
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

    public void updateUserProfile(User currentUser, User userModel) {
        currentUser.setLogin(userModel.getLogin());
        currentUser.setPassword(passwordEncoder.encode(userModel.getPassword()));

        this.userRepo.save(currentUser);
    }
}
