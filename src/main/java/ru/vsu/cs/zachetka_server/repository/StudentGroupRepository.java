package ru.vsu.cs.zachetka_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.zachetka_server.model.entity.StudentGroupEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentGroupRepository extends JpaRepository<StudentGroupEntity, UUID> {

    Optional<StudentGroupEntity> findByStudUidAndSemester(UUID uid, Byte semester);

    List<StudentGroupEntity> findAllBySemester(Byte semester);

    List<StudentGroupEntity> findAllByGroup(Float group);

    List<StudentGroupEntity> findAllByStudUidIn(List<UUID> uids);
}
