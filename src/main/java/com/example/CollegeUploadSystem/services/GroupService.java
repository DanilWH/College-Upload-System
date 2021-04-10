package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.repos.GroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepo groupRepo;

    public List<Group> getAll() {
        return this.groupRepo.findAll();
    }

    public Group getById(Long groupId) {
        return this.groupRepo.findById(groupId).orElseThrow(NoResultException::new);
    }
}
