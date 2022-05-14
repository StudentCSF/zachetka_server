package ru.vsu.cs.zachetka_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.cs.zachetka_server.component.BaseRequestValidationComponent;
import ru.vsu.cs.zachetka_server.exception.LecturerNotFoundException;
import ru.vsu.cs.zachetka_server.exception.RequestNotValidException;
import ru.vsu.cs.zachetka_server.exception.SubjLectAlreadyExistsException;
import ru.vsu.cs.zachetka_server.exception.SubjectNotFoundException;
import ru.vsu.cs.zachetka_server.model.dto.request.AddSubjLectRequest;
import ru.vsu.cs.zachetka_server.model.dto.request.SubjLectRequest;
import ru.vsu.cs.zachetka_server.model.dto.response.LecturersAndSubjectsResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.SubjectResponse;
import ru.vsu.cs.zachetka_server.model.entity.LecturerEntity;
import ru.vsu.cs.zachetka_server.model.entity.SubjLectEntity;
import ru.vsu.cs.zachetka_server.model.entity.SubjectEntity;
import ru.vsu.cs.zachetka_server.repository.LecturerRepository;
import ru.vsu.cs.zachetka_server.repository.SubjLectRepository;
import ru.vsu.cs.zachetka_server.repository.SubjectRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubjLectService {

    private final SubjLectRepository subjLectRepository;

    private final SubjectRepository subjectRepository;

    private final LecturerRepository lecturerRepository;

    private final BaseRequestValidationComponent baseRequestValidationComponent;

    @Autowired
    public SubjLectService(SubjLectRepository subjLectRepository,
                           SubjectRepository subjectRepository,
                           LecturerRepository lecturerRepository,
                           BaseRequestValidationComponent baseRequestValidationComponent) {
        this.subjLectRepository = subjLectRepository;
        this.subjectRepository = subjectRepository;
        this.lecturerRepository = lecturerRepository;
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
                        .evalType(subjLectRequest.getEvalType())
                        .build()
        );
    }

    public void addSubjLect(AddSubjLectRequest addSubjLectRequest) {
        if (!baseRequestValidationComponent.isValid(addSubjLectRequest)) {
            throw new RequestNotValidException();
        }

        SubjectEntity subjectEntity = this.subjectRepository.findByNameAndSemester(
                        addSubjLectRequest.getSubjName(),
                        addSubjLectRequest.getSemester())
                .orElseThrow(SubjectNotFoundException::new);

        LecturerEntity lecturerEntity = this.lecturerRepository.findByFio(addSubjLectRequest.getLectFio())
                .orElseThrow(LecturerNotFoundException::new);

        this.subjLectRepository.save(SubjLectEntity.builder()
                .evalType(addSubjLectRequest.getEvalType())
                .uid(UUID.randomUUID())
                .subjUid(subjectEntity.getUid())
                .lectUid(lecturerEntity.getUid())
                .period(addSubjLectRequest.getPeriod())
                .build()
        );
    }

    public LecturersAndSubjectsResponse getSubjectsAndLecturers() {

        return LecturersAndSubjectsResponse.builder()
                .lecturers(
                        this.lecturerRepository.findAll().stream()
                                .map(LecturerEntity::getFio)
                                .sorted(String::compareTo)
                                .collect(Collectors.toList())
                )
                .subjects(
                        this.subjectRepository.findAll().stream()
                                .map(x -> SubjectResponse.builder()
                                        .name(x.getName())
                                        .semester(x.getSemester())
                                        .build()
                                )
                                .sorted(Comparator.comparing(SubjectResponse::getName)
                                        .thenComparing(SubjectResponse::getSemester))
                                .collect(Collectors.toList())
                )
                .build();
    }
}
