package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    private CourseUserRepository courseUserRepository;

    @Autowired
    private AuthUserClient authUserClient;

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        boolean deleteCourseUserInAuthUser = false;
        List<ModuleModel> moduleModelList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseID());
        if (!moduleModelList.isEmpty()) {
            moduleModelList.forEach(item -> {
                var moduleID = item.getModuleID();
                List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(moduleID);
                if (!lessonModelList.isEmpty()) {
                    lessonRepository.deleteAll(lessonModelList);
                }
            });
            moduleRepository.deleteAll(moduleModelList);
        }
        List<CourseUserModel> courseUserModelList = courseUserRepository.findAllCourseUserIntoCourse(courseModel.getCourseID().toString());
        if (!courseUserModelList.isEmpty()) {
            courseUserRepository.deleteAll(courseUserModelList);
            deleteCourseUserInAuthUser = true;
        }
        courseRepository.delete(courseModel);
        if (deleteCourseUserInAuthUser) {
            authUserClient.deleteCourseUserInAuthUser(courseModel.getCourseID());
        }
    }

    @Transactional
    @Override
    public CourseModel save(CourseModel courseModel) {
        return courseRepository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }
}
