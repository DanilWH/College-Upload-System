package com.example.CollegeUploadSystem.repos;

import com.example.CollegeUploadSystem.models.StudentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentResultRepo extends JpaRepository<StudentResult, Long> {

    StudentResult findByTaskIdAndUserId(Long taskId, Long userId);

}
