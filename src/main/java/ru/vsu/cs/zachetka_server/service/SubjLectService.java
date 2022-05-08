package ru.vsu.cs.zachetka_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.cs.zachetka_server.component.BaseRequestValidationComponent;
import ru.vsu.cs.zachetka_server.exception.RequestNotValidException;
import ru.vsu.cs.zachetka_server.exception.SubjLectAlreadyExistsException;
import ru.vsu.cs.zachetka_server.model.dto.request.SubjLectRequest;
import ru.vsu.cs.zachetka_server.model.entity.SubjLectEntity;
import ru.vsu.cs.zachetka_server.repository.SubjLectRepository;

import java.util.UUID;

@Service
public class SubjLectService {

    private final SubjLectRepository subjLectRepository;

    private final BaseRequestValidationComponent baseRequestValidationComponent;

    @Autowired
    public SubjLectService(SubjLectRepository subjLectRepository,
                           BaseRequestValidationComponent baseRequestValidationComponent) {
        this.subjLectRepository = subjLectRepository;
        this.baseRequestValidationComponent = baseRequestValidationComponent;
    }

    public void addSubjLect(SubjLectRequest subjLectRequest) {
        if (!this.baseRequestValidationComponent.isValid(subjLectRequest)) {
            throw new RequestNotValidException();
        }

        if (this.subjLectRepository.findByLectUidAndSubjUid(
                        subjLectRequest.getLectUid(),
                        subjLectRequest.getSubjUid())
                .isPresent()
        ) {
            throw new SubjLectAlreadyExistsException();
        }

        this.subjLectRepository.save(
                SubjLectEntity.builder()
                        .lectUid(subjLectRequest.getLectUid())
                        .subjUid(subjLectRequest.getSubjUid())
                        .uid(UUID.randomUUID())
                        .build()
        );
    }
}
