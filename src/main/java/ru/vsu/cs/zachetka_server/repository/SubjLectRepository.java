package ru.vsu.cs.zachetka_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.zachetka_server.model.entity.SubjLectEntity;

import java.util.UUID;

@Repository
public interface SubjLectRepository extends JpaRepository<SubjLectEntity, UUID> {
}
