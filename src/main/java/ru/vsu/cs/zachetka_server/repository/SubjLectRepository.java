package ru.vsu.cs.zachetka_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.zachetka_server.model.entity.SubjLectEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubjLectRepository extends JpaRepository<SubjLectEntity, UUID> {

    Optional<SubjLectEntity> findByLectUidAndSubjUid(UUID lectUid, UUID subjUid);

    List<SubjLectEntity> findAllByLectUid(UUID uid);

    List<SubjLectEntity> findAllByLectUidAndPeriod(UUID uid, String period);
}
