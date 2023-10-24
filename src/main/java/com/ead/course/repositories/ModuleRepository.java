package com.ead.course.repositories;

import com.ead.course.models.ModuleModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID> {
    @EntityGraph(attributePaths = {"course"})
    ModuleModel findByTitle(String title);

    @Query(value = "from ModuleModel m where m.course.courseID = :courseId")
    List<ModuleModel> findAllModulesIntoCourse(@Param("courseId") UUID courseId);
    @Query(value = "from ModuleModel m where m.course.courseID = :courseId and m.moduleID = :moduleId")
    Optional<ModuleModel> findModuleIntoCourse(@Param("courseId") UUID courseId, @Param("moduleId") UUID moduleId);
}
