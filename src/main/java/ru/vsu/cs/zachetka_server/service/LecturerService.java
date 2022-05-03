package ru.vsu.cs.zachetka_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.cs.zachetka_server.component.BaseRequestValidationComponent;
import ru.vsu.cs.zachetka_server.exception.LecturerAlreadyExistsException;
import ru.vsu.cs.zachetka_server.exception.RequestNotValidException;
import ru.vsu.cs.zachetka_server.model.entity.LecturerEntity;
import ru.vsu.cs.zachetka_server.repository.LecturerRepository;

import java.util.UUID;

@Service
public class LecturerService {

    private final LecturerRepository lecturerRepository;

    @Autowired
    public LecturerService(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    public void addLecturer(String lecturerFio, UUID userUid) {
        if (lecturerFio == null || lecturerFio.length() == 0 || userUid == null) {
            throw new RequestNotValidException();
        }

        if (this.lecturerRepository.findByUserUid(userUid).isPresent()) {
            throw new LecturerAlreadyExistsException();
        }

        this.lecturerRepository.save(
                LecturerEntity.builder()
                        .fio(lecturerFio)
                        .uid(UUID.randomUUID())
                        .userUid(userUid)
                        .build()
        );
    }
}
