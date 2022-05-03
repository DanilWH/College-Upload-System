package com.example.CollegeUploadSystem.repos;

import com.example.CollegeUploadSystem.models.StudentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentResultRepo extends JpaRepository<StudentResult, Long> {

    StudentResult findByTaskIdAndUserId(Long taskId, Long userId);

    @Query("select sr from StudentResult sr where task_id in :taskIds")
    List<StudentResult> findAllByTaskId(@Param("taskIds") List<Long> taskIds);
}
