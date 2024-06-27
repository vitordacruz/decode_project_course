package com.ead.course.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CourseUserDTO {
    private UUID courseId;
    private UUID userId;
}
