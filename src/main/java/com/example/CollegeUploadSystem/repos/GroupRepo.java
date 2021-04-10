package com.example.CollegeUploadSystem.repos;

import com.example.CollegeUploadSystem.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepo extends JpaRepository<Group, Long> {

    List<Group> findAll();

}
