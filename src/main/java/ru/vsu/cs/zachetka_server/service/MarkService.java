package ru.vsu.cs.zachetka_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.cs.zachetka_server.component.BaseRequestValidationComponent;
import ru.vsu.cs.zachetka_server.exception.MarkAlreadyExistsException;
import ru.vsu.cs.zachetka_server.exception.MarkRawNotFoundException;
import ru.vsu.cs.zachetka_server.exception.RequestNotValidException;
import ru.vsu.cs.zachetka_server.exception.StudentNotFoundException;
import ru.vsu.cs.zachetka_server.model.dto.request.AddMarkRawsRequest;
import ru.vsu.cs.zachetka_server.model.dto.request.MarkRequest;
import ru.vsu.cs.zachetka_server.model.dto.request.UpdateGroupMarksRequest;
import ru.vsu.cs.zachetka_server.model.dto.response.SubjLectResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.SubjLectsAndGroupsResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.lecturer.LecturerInfoResponse;
import ru.vsu.cs.zachetka_server.model.entity.*;
import ru.vsu.cs.zachetka_server.model.enumerate.Mark;
import ru.vsu.cs.zachetka_server.repository.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MarkService {

    private final MarkRepository markRepository;

    private final SubjectRepository subjectRepository;

    private final SubjLectRepository subjLectRepository;

    private final LecturerRepository lecturerRepository;

    private final StudentGroupRepository studentGroupRepository;

    private final StudentRepository studentRepository;

    private final BaseRequestValidationComponent baseRequestValidationComponent;

    @Autowired
    public MarkService(MarkRepository markRepository,
                       SubjectRepository subjectRepository,
                       SubjLectRepository subjLectRepository,
                       LecturerRepository lecturerRepository,
                       StudentGroupRepository studentGroupRepository,
                       StudentRepository studentRepository, BaseRequestValidationComponent baseRequestValidationComponent) {
        this.markRepository = markRepository;
        this.subjectRepository = subjectRepository;
        this.subjLectRepository = subjLectRepository;
        this.lecturerRepository = lecturerRepository;
        this.studentGroupRepository = studentGroupRepository;
        this.studentRepository = studentRepository;
        this.baseRequestValidationComponent = baseRequestValidationComponent;
    }

    public List<LecturerInfoResponse> updateGroupData(
            UUID uid,
            List<UpdateGroupMarksRequest> updateGroupMarksRequests
    ) {

        List<LecturerInfoResponse> result = new ArrayList<>();
        for (UpdateGroupMarksRequest curr : updateGroupMarksRequests) {

            if (curr.getStudUid() == null) throw new RequestNotValidException();

            MarkEntity markEntity = this.markRepository.findByStudUidAndSlUid(curr.getStudUid(), uid)
                    .orElseThrow(MarkRawNotFoundException::new);
            markEntity.setMark(curr.getMark() == Mark.NONE ? null : curr.getMark());
            markEntity.setDate(curr.getDate().length() != 10 ?
                    null :
                    LocalDate.parse(curr.getDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            this.markRepository.save(markEntity);
            result.add(LecturerInfoResponse.builder()
                    .examDate(markEntity.getDate().toString())
                    .studFio(this.studentRepository.findById(markEntity.getStudUid())
                            .orElseThrow(StudentNotFoundException::new)
                            .getFio())
                    .mark(markEntity.getMark())
                    .studUid(markEntity.getStudUid())
                    .build());
        }
        return result;
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

    public SubjLectsAndGroupsResponse getLists(String period, Byte semester) {
        if (period == null || period.length() == 0 || semester == null) {
            throw new RequestNotValidException();
        }

        List<SubjectEntity> subjectEntities = this.subjectRepository.findAllBySemester(semester);

        List<SubjLectEntity> subjLectEntities = this.subjLectRepository.findAllBySubjUidIn(subjectEntities.stream()
                .map(SubjectEntity::getUid)
                .collect(Collectors.toList()));

        Map<UUID, String> lecturerEntities = this.lecturerRepository.findAllByUidIn(
                        subjLectEntities.stream()
                                .map(SubjLectEntity::getLectUid)
                                .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(
                        LecturerEntity::getUid,
                        LecturerEntity::getFio
                ));

        Map<UUID, String> subjects = subjectEntities.stream()
                .collect(Collectors.toMap(
                        SubjectEntity::getUid,
                        SubjectEntity::getName
                ));

        List<SubjLectResponse> responseList = subjLectEntities.stream()
                .map(x -> SubjLectResponse.builder()
                        .evalType(x.getEvalType())
                        .lectFio(lecturerEntities.get(x.getLectUid()))
                        .subjName(subjects.get(x.getSubjUid()))
                        .slUid(x.getUid())
                        .build())
                .collect(Collectors.toList());

        Set<Float> groups = this.studentGroupRepository.findAllBySemester(semester)
                .stream()
                .map(StudentGroupEntity::getGroup)
                .collect(Collectors.toSet());

        return SubjLectsAndGroupsResponse.builder()
                .groups(groups)
                .subjLects(responseList)
                .build();
    }

    public void addMark(AddMarkRawsRequest addMarkRawsRequest) {
        if (!this.baseRequestValidationComponent.isValid(addMarkRawsRequest)) {
            throw new RequestNotValidException();
        }

        Set<Float> markEntities = this.studentGroupRepository.findAllByStudUidIn(this.markRepository.findAllBySlUid(addMarkRawsRequest.getSlUid())
                        .stream()
                        .map(MarkEntity::getStudUid)
                        .collect(Collectors.toList()))
                .stream()
                .map(StudentGroupEntity::getGroup)
                .filter(x -> !x.equals(addMarkRawsRequest.getGroup()))
                .collect(Collectors.toSet());

        if (!markEntities.isEmpty()) {
            throw new MarkAlreadyExistsException();
        }


        List<MarkEntity> add = this.studentGroupRepository.findAllByGroup(addMarkRawsRequest.getGroup())
                .stream()
                .map(x -> MarkEntity.builder()
                        .uid(UUID.randomUUID())
                        .slUid(addMarkRawsRequest.getSlUid())
                        .date(null)
                        .mark(null)
                        .studUid(x.getStudUid())
                        .build())
                .collect(Collectors.toList());

        this.markRepository.saveAll(add);
    }
}
