package com.winio94.recruitment.schoolregistration.db;

import com.winio94.recruitment.schoolregistration.api.NewStudent;
import com.winio94.recruitment.schoolregistration.api.Student;
import com.winio94.recruitment.schoolregistration.api.StudentsRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

public class StudentsRepositoryImpl implements StudentsRepository {

    private final DbStudentsRepository dbStudentsRepository;

    public StudentsRepositoryImpl(DbStudentsRepository dbStudentsRepository) {
        this.dbStudentsRepository = dbStudentsRepository;
    }

    @Override
    public Set<Student> getAll() {
        return dbStudentsRepository.findAll()
                                   .stream()
                                   .map(DbStudent::toDomainModel)
                                   .collect(Collectors.toSet());
    }

    @Override
    public Optional<Student> getOne(String uuid) {
        return dbStudentsRepository.findByUuid(uuid)
                                   .map(DbStudent::toDomainModel);
    }

    @Override
    public Student create(NewStudent newStudent) {
        DbStudent dbStudent = dbStudentsRepository.save(new DbStudent(UUID.randomUUID()
                                                                          .toString(),
                                                                      newStudent.getFirstName(),
                                                                      newStudent.getLastName()));
        return dbStudent.toDomainModel();
    }

    @Transactional
    @Override
    public void delete(String uuid) {
        dbStudentsRepository.deleteByUuid(uuid);
    }
}
