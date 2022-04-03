package com.winio94.recruitment.schoolregistration.db;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DbCourseRepository extends JpaRepository<DbCourse, Long> {

    Optional<DbCourse> findByUuid(String uuid);

    Optional<DbCourse> findByCode(String code);

    void deleteByUuid(String uuid);

    @Query("SELECT c FROM DbCourse c LEFT JOIN DbRegistration r ON c.uuid = r.courseUuid WHERE r.studentUuid = :studentUuid")
    List<DbCourse> findAllByStudent(@Param("studentUuid") String studentUuid);

    @Query("SELECT c FROM DbCourse c LEFT JOIN DbRegistration r ON c.uuid = r.courseUuid WHERE r.courseUuid IS NULL")
    List<DbCourse> findAllWithoutStudent();
}
