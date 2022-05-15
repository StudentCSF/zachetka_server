package ru.vsu.cs.zachetka_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.zachetka_server.model.entity.StudentEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, UUID> {

    Optional<StudentEntity> findByUserUid(UUID userUid);

    Optional<StudentEntity> findByFioAndInitYearAndInitSem(String fio, Integer year, Byte sem);
}
