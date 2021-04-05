package com.example.CollegeUploadSystem.repos;

import com.example.CollegeUploadSystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByLogin(String login);
}
