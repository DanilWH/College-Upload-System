package com.example.CollegeUploadSystem;

import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.models.UserRoles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ApplicationUtils {

    @Value("${upload.path}")
    private String uploadPath;

    /**
     * Throws the 403 (Forbidden) error if a student tries to edit someone else's profile.
     *
     * @return void
     */
    public void protectAccessToUserProfile(User currentUser, User initialUser) {
        if (currentUser.getUserRoles().contains(UserRoles.STUDENT) && !currentUser.getId().equals(initialUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Checks if a field value of the binding object is blank
     * and adds an error into the binding result if true.
     *
     * @return void
     */
    public void fieldNotBlank(
            BindingResult bindingResult,
            String fieldName,
            String fieldValue,
            String defaultMessage
    ) {
        if (fieldValue.isBlank()) {
            FieldError error = new FieldError(bindingResult.getObjectName(), fieldName, defaultMessage);
            bindingResult.addError(error);
        }
    }

    /**
     * Dynamically updates a logged user's session.
     *
     * @return void
     */
    public void refreshCurrentUserSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> authorities = new ArrayList<>(auth.getAuthorities());
        // authorities.add(...); //add your role here [e.g., new SimpleGrantedAuthority("ROLE_NEW_ROLE")]

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), authorities);

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    /**
     * Uploads MultipartFile onto the server.
     *
     * @return void
     */
    public void uploadMultipartFile(MultipartFile file, String directory, String filepath, String filename) throws IOException {
        System.out.println(this.uploadPath);
        // create a new directory if doesn't exist.
        File fileObj = new File(this.uploadPath + "/" + directory + "/" + filepath);
        if (!fileObj.exists()) {
            fileObj.mkdirs();
        }

        // save the file in the directory.
        file.transferTo(new File(this.uploadPath + "/" + directory + "/" + filepath + filename));
    }

}
