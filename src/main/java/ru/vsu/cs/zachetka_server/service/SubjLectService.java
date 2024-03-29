package ru.vsu.cs.zachetka_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.cs.zachetka_server.component.BaseRequestValidationComponent;
import ru.vsu.cs.zachetka_server.exception.LecturerNotFoundException;
import ru.vsu.cs.zachetka_server.exception.RequestNotValidException;
import ru.vsu.cs.zachetka_server.exception.SubjLectAlreadyExistsException;
import ru.vsu.cs.zachetka_server.exception.SubjectNotFoundException;
import ru.vsu.cs.zachetka_server.model.dto.request.AddSubjLectRequest;
import ru.vsu.cs.zachetka_server.model.dto.response.LecturersAndSubjectsResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.SubjectResponse;
import ru.vsu.cs.zachetka_server.model.entity.LecturerEntity;
import ru.vsu.cs.zachetka_server.model.entity.SubjLectEntity;
import ru.vsu.cs.zachetka_server.model.entity.SubjectEntity;
import ru.vsu.cs.zachetka_server.repository.LecturerRepository;
import ru.vsu.cs.zachetka_server.repository.SubjLectRepository;
import ru.vsu.cs.zachetka_server.repository.SubjectRepository;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
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

        if (this.subjLectRepository.findByLectUidAndSubjUidAndEvalTypeAndPeriod(
                lecturerEntity.getUid(),
                subjectEntity.getUid(),
                addSubjLectRequest.getEvalType(),
                addSubjLectRequest.getPeriod()
        ).isPresent()
        ) {
            throw new SubjLectAlreadyExistsException();
        }

        this.subjLectRepository.save(SubjLectEntity.builder()
                .evalType(addSubjLectRequest.getEvalType())
                .uid(UUID.randomUUID())
                .subjUid(subjectEntity.getUid())
                .lectUid(lecturerEntity.getUid())
                .period(addSubjLectRequest.getPeriod().trim())
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

    public SortedSet<String> getPeriods() {
        return this.subjLectRepository.findAll()
                .stream()
                .map(SubjLectEntity::getPeriod)
                .collect(Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparing(String::valueOf).reversed()))
                );
    }
}
