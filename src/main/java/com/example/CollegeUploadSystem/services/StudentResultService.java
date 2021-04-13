package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.StudentResult;
import com.example.CollegeUploadSystem.models.Task;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.repos.StudentResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class StudentResultService {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private StudentResultRepo studentResultRepo;

    public void uploadStudentResult(
            User currentUser,
            StudentResult studentResult,
            Group group,
            Task task,
            MultipartFile file
    ) throws IOException {
            // check if the student has already uploaded files that belongs to the same task.
            if (studentResult != null) {
                // if so, delete the old file.
                File fileObj = new File(this.uploadPath + studentResult.getFilepath() + studentResult.getFilename());
                fileObj.delete();

                // we don't delete the entity in the database because we'll just update the old one.
            }
            else {
                // create a new student result if the students hasn't uploaded a file yet.
                studentResult = new StudentResult();
            }

            // fill the fields of the new student result entity.
            studentResult.setDateTime(LocalDateTime.now());
            studentResult.setTask(task);
            studentResult.setUser(currentUser);

            // uploadPath is the root directory where all the upload are stored.
            // filepath is the directory specified by the group name and the task name.
            // filename is the unique file name.
            String filepath = String.format("%s/%s/", group.getName(), task.getName());

            // create a new directory if doesn't exist.
            File fileObj = new File(this.uploadPath + filepath);
            if (!fileObj.exists()) {
                fileObj.mkdirs();
            }

            // create the file name.
            String filename = String.format("%s%s_%s_%s",
                    currentUser.getLastName(),
                    currentUser.getFirstName(),
                    UUID.randomUUID(),
                    file.getOriginalFilename()
            );

            // save the file in the directory.
            file.transferTo(new File(this.uploadPath + filepath + filename));

            studentResult.setFilename(filename);
            studentResult.setFilepath(filepath);

            this.studentResultRepo.save(studentResult);
    }
}
