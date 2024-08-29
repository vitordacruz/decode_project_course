package com.ead.course.validation;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.enums.UserType;
import com.ead.course.models.UserModel;
import com.ead.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    public static final String USER_INSTRUCTOR = "userInstructor";
    public static final String USER_INSTRUCTOR_ERROR = "UserInstructorError";
    public static final String INSTRUCTOR_NOT_FOUND = "Instructor not found";
    public static final String USER_MUST_BE_INSTRUCTOR_OR_ADMIN = "User must be INSTRUCTOR or ADMIN";
    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @Autowired
    private UserService userService;

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

        Optional<UserModel> userModelOptional = userService.findById(userInstructorId);

        if (userModelOptional.isEmpty()) {
            errors.rejectValue(USER_INSTRUCTOR, USER_INSTRUCTOR_ERROR, INSTRUCTOR_NOT_FOUND);
        } else {
            if (userModelOptional.get().getUserType().equals(UserType.STUDENT.toString())) {
                errors.rejectValue(USER_INSTRUCTOR, USER_INSTRUCTOR_ERROR, USER_MUST_BE_INSTRUCTOR_OR_ADMIN);
            }
        }

    }
}
