package ru.vsu.cs.zachetka_server.service;

import org.springframework.stereotype.Service;
import ru.vsu.cs.zachetka_server.component.BaseRequestValidationComponent;
import ru.vsu.cs.zachetka_server.exception.MarkAlreadyExistsException;
import ru.vsu.cs.zachetka_server.exception.RequestNotValidException;
import ru.vsu.cs.zachetka_server.model.dto.request.MarkRequest;
import ru.vsu.cs.zachetka_server.model.entity.MarkEntity;
import ru.vsu.cs.zachetka_server.repository.MarkRepository;

import java.util.UUID;

@Service
public class MarkService {

    private final MarkRepository markRepository;

    private final BaseRequestValidationComponent baseRequestValidationComponent;

    public MarkService(MarkRepository markRepository,
                       BaseRequestValidationComponent baseRequestValidationComponent) {
        this.markRepository = markRepository;
        this.baseRequestValidationComponent = baseRequestValidationComponent;
    }

    public void addMark(MarkRequest markRequest) {
        if (!this.baseRequestValidationComponent.isValid(markRequest)) {
            throw new RequestNotValidException();
        }

        if (this.markRepository.findByStudUidAndSlUid(
                markRequest.getStudUid(),
                markRequest.getSlUid())
                .isPresent()
        ) {
            throw new MarkAlreadyExistsException();
        }

        this.markRepository.save(
                MarkEntity.builder()
                        .studUid(markRequest.getStudUid())
                        .mark(markRequest.getMark())
                        .date(markRequest.getDate())
                        .slUid(markRequest.getSlUid())
                        .uid(UUID.randomUUID())
                        .build()
        );
    }
}
