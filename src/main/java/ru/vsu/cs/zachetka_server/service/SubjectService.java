package ru.vsu.cs.zachetka_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.cs.zachetka_server.component.BaseRequestValidationComponent;
import ru.vsu.cs.zachetka_server.exception.RequestNotValidException;
import ru.vsu.cs.zachetka_server.model.dto.request.SubjectRequest;
import ru.vsu.cs.zachetka_server.model.entity.SubjectEntity;
import ru.vsu.cs.zachetka_server.repository.SubjectRepository;

import java.util.UUID;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    private final BaseRequestValidationComponent baseRequestValidationComponent;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, BaseRequestValidationComponent baseRequestValidationComponent) {
        this.subjectRepository = subjectRepository;
        this.baseRequestValidationComponent = baseRequestValidationComponent;
    }

    public void addSubject(SubjectRequest subjectRequest) {
        if (!this.baseRequestValidationComponent.isValid(subjectRequest)) {
            throw new RequestNotValidException();
        }

        this.subjectRepository.save(
                SubjectEntity.builder()
                        .semester(subjectRequest.getSemester())
                        .name(subjectRequest.getName())
                        .uid(UUID.randomUUID())
                        .build()
        );
    }
}
