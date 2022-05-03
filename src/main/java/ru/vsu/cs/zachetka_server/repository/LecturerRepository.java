package ru.vsu.cs.zachetka_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.zachetka_server.model.entity.LecturerEntity;

import java.util.UUID;

@Repository
public interface LecturerRepository extends JpaRepository<LecturerEntity, UUID> {
}
