package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.StudentResult;
import com.example.CollegeUploadSystem.models.Task;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.repos.StudentResultRepo;
import com.example.CollegeUploadSystem.repos.TaskRepo;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentResultService {

    @Value("${upload.path}")
    private String uploadPath;
    @Value("${user.directory}")
    private String userDirectory;

    private final StudentResultRepo studentResultRepo;
    private final TaskRepo taskRepo;
    private final ApplicationUtils applicationUtils;

    @Autowired
    public StudentResultService(StudentResultRepo studentResultRepo, TaskRepo taskRepo, ApplicationUtils applicationUtils) {
        this.studentResultRepo = studentResultRepo;
        this.taskRepo = taskRepo;
        this.applicationUtils = applicationUtils;
    }

    public StudentResult getById(Long studentResultId) {
        return this.studentResultRepo.findById(studentResultId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The student result with the ID of " + studentResultId + " was not found."));
    }

    public StudentResult getByTaskIdAndUserId(Long taskId, Long userId) {
        // TODO: change the query to "findByUserIdAndTaskId"
        return this.studentResultRepo.findByTaskIdAndUserId(taskId, userId);
    }

    public List<StudentResult> getAllByGroupId(Long groupId) {
        List<Long> taskIds = this.taskRepo.findByGroupIdOrderByCreationDateTimeDesc(groupId).stream().map(Task::getId).collect(Collectors.toList());
        return this.studentResultRepo.findAllByTaskId(taskIds);
    }

    public Resource getStudentResultFileAsResource(StudentResult studentResultFromDb) throws MalformedURLException {
        return this.applicationUtils.loadFileAsResource(this.userDirectory, studentResultFromDb.getFilepath() + studentResultFromDb.getFilename());
    }

    public StudentResult upload(User currentUser, Group group, Task task, MultipartFile file) throws IOException {
        // TODO: decide if i need to check that the current user uploads the file under his cell.
        // check if the current user belongs to the appropriate group.
        // if (!currentUser.getGroup().getId().equals(groupId)) {
        //     throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        // }

        // check if there is a result of the specific user, in the specific group, under the specific task in the database.
        if (this.getByTaskIdAndUserId(task.getId(), currentUser.getId()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The result already exists. Delete the old one if you want to upload another result.");
        }

        // create the new StudentResult.
        StudentResult studentResult = new StudentResult();

        // uploadPath is the root directory where all the upload are stored.
        // filepath is the directory specified by the group name and the task name.
        // filename is the unique file name.
        String filepath = String.format("%s_%s/%s/", group.getName(), group.getCreationDate().getYear(), task.getName());

        // TODO: check the filename for special characters (use the same approach as in Task description file).
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
        return this.studentResultRepo.save(studentResult);
    }

    public void delete(StudentResult studentResult) {
        // delete the student result data in the database.
        this.studentResultRepo.delete(studentResult);

        // delete the file on the server.
        this.applicationUtils.deleteFile(this.userDirectory, "/" + studentResult.getFilepath() + studentResult.getFilename());
    }

}
