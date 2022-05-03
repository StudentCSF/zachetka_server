package ru.vsu.cs.zachetka_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.zachetka_server.model.entity.MarkEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MarkRepository extends JpaRepository<MarkEntity, UUID> {

    List<MarkEntity> findAllByStudUid(UUID studUid);

    Optional<MarkEntity> findByStudUidAndSlUid(UUID studUid, UUID slUid);
}
