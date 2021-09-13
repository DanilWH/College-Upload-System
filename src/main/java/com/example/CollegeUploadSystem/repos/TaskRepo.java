package com.example.CollegeUploadSystem.repos;

import com.example.CollegeUploadSystem.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    List<Task> findByGroupIdOrderByCreationDateTimeDesc(Long groupId);
}
