package com.ead.course.validation;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.CourseDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @Autowired
    private AuthUserClient authUserClient;

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        CourseDTO courseDTO = (CourseDTO) o;
        validator.validate(courseDTO, errors);
        if(!errors.hasErrors()) {
            validateUserInstructor(courseDTO.getUserInstructor(), errors);
        }
    }

    private void validateUserInstructor(UUID userInstructorId, Errors errors) {
        ResponseEntity<UserDTO> responseUserInstructor;

        try {
            responseUserInstructor = authUserClient.getOneUserById(userInstructorId);
            if (responseUserInstructor.getBody().getUserType().equals(UserType.STUDENT)) {
                errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN");
            }
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                errors.rejectValue("userInstructor", "UserInstructorError", "Instructor not found");
            }
        }
    }
}
