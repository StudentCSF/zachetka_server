package ru.vsu.cs.zachetka_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.zachetka_server.model.entity.SubjectEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, UUID> {

    Optional<SubjectEntity> findByNameAndSemester(String name, Byte semester);
}
