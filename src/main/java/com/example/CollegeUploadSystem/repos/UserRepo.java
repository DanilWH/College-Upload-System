package com.example.CollegeUploadSystem.repos;

import com.example.CollegeUploadSystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByLogin(String login);
    List<User> findByGroupIdOrderByLastName(Long groupId);

}
