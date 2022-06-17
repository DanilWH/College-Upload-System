package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.StudentResult;
import com.example.CollegeUploadSystem.models.Task;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.repos.StudentResultRepo;
import com.example.CollegeUploadSystem.utils.ApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class StudentResultService {

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

    public StudentResult getByUserIdAndTaskId(Long userId, Long taskId) {
        return this.studentResultRepo.findByUserIdAndTaskId(userId, taskId);
    }

    public List<StudentResult> getAllByGroupId(Long groupId) {
        return this.studentResultRepo.findByGroupId(groupId);
    }

    public Resource getStudentResultFileAsResource(StudentResult studentResultFromDb) throws MalformedURLException {
        return this.applicationUtils.loadFileAsResource(this.userDirectory, studentResultFromDb.getFilepath() + studentResultFromDb.getFilename());
    }

    public StudentResult upload(User currentUser, Task task, MultipartFile file) throws IOException {
        // check if the current user attaches the file to the task that belongs to the group which the current user (student) is in.
        if (!currentUser.getGroup().getId().equals(task.getGroup().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A student can not attach the file to the task that does not belong to his group");
        }

        // check if there is a result of the specific user, in the specific group, under the specific task in the database.
        if (this.getByUserIdAndTaskId(currentUser.getId(), task.getId()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The result already exists. Delete the old one if you want to upload another result.");
        }

        // uploadPath is the root directory where all the upload are stored.
        // filepath is the directory specified by the group name and the task name.
        // filename is the unique file name.
        String filepath = String.format("%s_%s/%s/",
                currentUser.getGroup().getName().replaceAll(ApplicationUtils.REGEXR_STRING, ApplicationUtils.REPLACEMENT_STRING),
                currentUser.getGroup().getCreationDate().getYear(),
                task.getName().replaceAll(ApplicationUtils.REGEXR_STRING, ApplicationUtils.REPLACEMENT_STRING)
        );

        // create the file name.
        String filename = String.format("%s%s_%s_%s",
                currentUser.getLastName(),
                currentUser.getFirstName(),
                UUID.randomUUID(),
                file.getOriginalFilename().replaceAll(ApplicationUtils.REGEXR_STRING, ApplicationUtils.REPLACEMENT_STRING)
        );

        // upload the file.
        this.applicationUtils.uploadMultipartFile(file, this.userDirectory, filepath + filename);

        // create the new StudentResult.
        StudentResult studentResult = new StudentResult();

        // fill the fields of the new student result entity.
        studentResult.setFilename(filename);
        studentResult.setFilepath(filepath);
        studentResult.setDateTime(ZonedDateTime.now());
        studentResult.setUser(currentUser);
        studentResult.setTask(task);

        // and finally save the new result in the database.
        return this.studentResultRepo.save(studentResult);
    }

    public void delete(User currentUser, StudentResult studentResult) {
        // a student can delete only his onw results, so we check it.
        if (!currentUser.getId().equals(studentResult.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "It is forbidden to delete someone else's result.");
        }

        // delete the student result data in the database.
        this.studentResultRepo.delete(studentResult);

        // delete the file on the server.
        this.applicationUtils.deleteFile(this.userDirectory, "/" + studentResult.getFilepath() + studentResult.getFilename());
    }

}
