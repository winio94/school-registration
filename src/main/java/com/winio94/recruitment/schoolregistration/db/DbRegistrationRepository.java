package com.winio94.recruitment.schoolregistration.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DbRegistrationRepository extends JpaRepository<DbRegistration, Long> {

    @Query("SELECT COUNT(1) FROM DbRegistration r WHERE r.courseUuid = :courseUuid")
    int getNumberOfStudentsRegistered(@Param("courseUuid") String courseUuid);

    @Query("SELECT COUNT(1) FROM DbRegistration r WHERE r.studentUuid = :studentUuid")
    int getNumberOfCoursesForStudent(@Param("studentUuid") String studentUuid);

    @Query("SELECT CASE WHEN COUNT(1) > 0 THEN TRUE ELSE FALSE END FROM DbRegistration r "
        + "WHERE r.studentUuid = :studentUuid " + "AND r.courseUuid = :courseUuid")
    boolean isStudentRegisteredToCourse(@Param("studentUuid") String studentUuid,
                                        @Param("courseUuid") String courseUuid);
}
