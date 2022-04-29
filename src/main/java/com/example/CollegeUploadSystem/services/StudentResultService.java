package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.StudentResult;
import com.example.CollegeUploadSystem.models.Task;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.repos.StudentResultRepo;
import com.example.CollegeUploadSystem.utils.ApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class StudentResultService {

    @Value("${upload.path}")
    private String uploadPath;
    @Value("${user.directory}")
    private String userDirectory;

    private final StudentResultRepo studentResultRepo;
    private final ApplicationUtils applicationUtils;

    @Autowired
    public StudentResultService(StudentResultRepo studentResultRepo, ApplicationUtils applicationUtils) {
        this.studentResultRepo = studentResultRepo;
        this.applicationUtils = applicationUtils;
    }

    public StudentResult getById(Long studentResultId) {
        return this.studentResultRepo.findById(studentResultId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The student result with the ID of " + studentResultId + " was not found."));
    }

    public StudentResult getByTaskIdAndUserId(Long taskId, Long userId) {
        // TODO: change the query to "findByUserIdAndTaskId"
        return this.studentResultRepo.findByTaskIdAndUserId(taskId, userId);
    }

    public void upload(User currentUser, Group group, Task task, MultipartFile file) throws IOException {
        // TODO: decide if i need to check that the current user uploads the file under his cell.
        // check if the current user belongs to the appropriate group.
        // if (!currentUser.getGroup().getId().equals(groupId)) {
        //     throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        // }

        // check if there is a result of the specific user, in the specific group, under the specific task in the database.
        if (this.getByTaskIdAndUserId(task.getId(), group.getId()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The result already exists. Delete the old one if you want to upload another result.");
        }

        // create the new StudentResult.
        StudentResult studentResult = new StudentResult();

        // uploadPath is the root directory where all the upload are stored.
        // filepath is the directory specified by the group name and the task name.
        // filename is the unique file name.
        String filepath = String.format("%s_%s/%s/", group.getName(), group.getCreationDate().getYear(), task.getName());

        // create the file name.
        String filename = String.format("%s%s_%s_%s",
                currentUser.getLastName(),
                currentUser.getFirstName(),
                UUID.randomUUID(),
                file.getOriginalFilename()
        );

        // upload the file.
        this.applicationUtils.uploadMultipartFile(file, this.userDirectory, filepath + filename);

        // fill the fields of the new student result entity.
        studentResult.setFilename(filename);
        studentResult.setFilepath(filepath);
        studentResult.setDateTime(LocalDateTime.now());
        studentResult.setUser(currentUser);
        studentResult.setTask(task);

        // and finally save the new result in the database.
        this.studentResultRepo.save(studentResult);
    }

    public void delete(StudentResult studentResult) {
        // TODO: refactor - use applicationUtils to delete the file.

        // delete the file from the directory.
        File fileObj = new File(this.uploadPath + "/" + this.userDirectory + "/" + studentResult.getFilepath() + studentResult.getFilename());
        fileObj.delete();

        // delete the student result data in the database.
        this.studentResultRepo.delete(studentResult);
    }

}
