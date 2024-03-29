package ru.vsu.cs.zachetka_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.zachetka_server.model.entity.LecturerEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LecturerRepository extends JpaRepository<LecturerEntity, UUID> {

    Optional<LecturerEntity> findByUserUid(UUID userUid);

    Optional<LecturerEntity> findByFio(String fio);

    List<LecturerEntity> findAllByUidIn(List<UUID> uids);
}
