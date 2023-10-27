package com.ead.course.repositories;

import com.ead.course.models.LessonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<LessonModel, UUID>, JpaSpecificationExecutor<LessonModel> {
    @Query(value = "From LessonModel l where l.module.moduleID = :moduleId")
    List<LessonModel> findAllLessonsIntoModule(@Param("moduleId") UUID moduleId);

    @Query(value = "From LessonModel l where l.module.moduleID = :moduleId and l.lessonID = :lessonId")
    Optional<LessonModel> findLessonIntoModule(@Param("moduleId") UUID moduleId, @Param("lessonId") UUID lessonId);
}
