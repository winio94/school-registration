package com.winio94.recruitment.schoolregistration.db;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DbStudentsRepository extends JpaRepository<DbStudent, Long> {

    Optional<DbStudent> findByUuid(String uuid);

    @Query("SELECT s FROM DbStudent s LEFT JOIN DbRegistration r ON s.uuid = r.studentUuid WHERE r.courseUuid = :courseUuid")
    List<DbStudent> findAllByCourse(@Param("courseUuid") String courseUuid);

    void deleteByUuid(String uuid);

    @Query("SELECT s FROM DbStudent s LEFT JOIN DbRegistration r ON s.uuid = r.studentUuid WHERE r.studentUuid IS NULL")
    List<DbStudent> findAllWithoutCourse();
}
