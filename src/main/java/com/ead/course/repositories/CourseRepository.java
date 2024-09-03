package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseModel, UUID>, JpaSpecificationExecutor<CourseModel> {

    @Query(value = "select case when count(tcu.course_id) > 0 THEN 'True' ELSE 'False' END FROM tb_courses_users as tcu WHERE" +
            " tcu.course_id= :courseId and tcu.user_id= :userId",nativeQuery = true)
    boolean existsByCourseAndUser(@Param("courseId") String courseId, @Param("userId") String userId);

    @Modifying
    @Query(value="insert into tb_courses_users(course_id, user_id) values (:courseId,:userId)",nativeQuery = true)
    void saveCourseUser(@Param("courseId") String courseId, @Param("userId") String userId);

    @Modifying
    @Query(value="delete from tb_courses_users where course_id = :courseId", nativeQuery = true)
    void deleteCourseUserByCourse(@Param("courseId") String courseId);

    @Modifying
    @Query(value="delete from tb_courses_users where user_id = :userId", nativeQuery = true)
    void deleteCourseUserByUser(@Param("userId") String userId);
}
