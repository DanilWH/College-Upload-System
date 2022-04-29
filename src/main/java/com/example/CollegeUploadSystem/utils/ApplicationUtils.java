package com.example.CollegeUploadSystem.utils;

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

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ApplicationUtils {

    private final Validator validator;

    @Value("${upload.path}")
    private String uploadPath;

    public ApplicationUtils(Validator validator) {
        this.validator = validator;
    }

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
    public void uploadMultipartFile(MultipartFile file, String directory, String fileLocation) throws IOException {
        // check if the uploading file is empty.
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The file is empty");
        }

        // create a new directory if doesn't exist.
        File fileObj = new File(this.uploadPath + "/" + directory + "/" + fileLocation);

        // make the necessary directories if they don't exist on the server.
        if (!fileObj.exists()) {
            fileObj.mkdirs();
        }

        // save the file in the directory.
        file.transferTo(new File(this.uploadPath + "/" + directory + "/" + fileLocation));
    }

    public void deleteFile(String directory, String filepath) throws Exception {
        File fileObj = new File(this.uploadPath + "/" + directory + "/" + filepath);

        if (!fileObj.delete()) {
            // TODO: throw ResponseStatusException
            throw new Exception("Unable to delete the task old description file.");
        }
    }

    @Deprecated(since = "(In a controller) Use the @Valid annotation instead.")
    public <T> Set<ConstraintViolation<T>> validateBean(T bean) {
        Set<ConstraintViolation<T>> violations = this.validator.validate(bean);
        if (!violations.isEmpty()) {
            return violations;
        }

        return null;
    }

}
