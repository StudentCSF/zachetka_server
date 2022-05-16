package ru.vsu.cs.zachetka_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.cs.zachetka_server.component.BaseRequestValidationComponent;
import ru.vsu.cs.zachetka_server.exception.*;
import ru.vsu.cs.zachetka_server.model.dto.request.AddMarkRawsRequest;
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
            markEntity.setDate(curr.getDate() == null ?
                    null :
                    LocalDate.parse(curr.getDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            this.markRepository.save(markEntity);
            result.add(LecturerInfoResponse.builder()
                    .examDate(markEntity.getDate() == null ? null : markEntity.getDate().toString())
                    .studFio(this.studentRepository.findById(markEntity.getStudUid())
                            .orElseThrow(StudentNotFoundException::new)
                            .getFio())
                    .mark(markEntity.getMark())
                    .studUid(markEntity.getStudUid())
                    .build());
        }
        return result;
    }

    public SubjLectsAndGroupsResponse getLists(String period, Byte semester) {
        if (period == null || period.length() == 0 || semester == null) {
            throw new RequestNotValidException();
        }

        List<SubjectEntity> subjectEntities = this.subjectRepository.findAllBySemester(semester);

        List<SubjLectEntity> subjLectEntities = this.subjLectRepository.findAllBySubjUidInAndPeriodEquals(subjectEntities.stream()
                        .map(SubjectEntity::getUid)
                        .collect(Collectors.toList()),
                period);

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


        List<StudentGroupEntity> studGroups = this.studentGroupRepository.findAllBySemester(semester);

        Map<UUID, StudentGroupEntity> map = new HashMap<>();

        for (StudentGroupEntity studentGroupEntity : studGroups) {
            map.put(studentGroupEntity.getStudUid(), studentGroupEntity);
        }

        int year = Integer.parseInt(period.split("-")[(semester + 1) % 2]);

        List<StudentEntity> studentEntities = this.studentRepository.findAllById(studGroups
                .stream()
                .map(StudentGroupEntity::getStudUid)
                .collect(Collectors.toList()));

        Set<Float> groups = new TreeSet<>(Float::compareTo);

        for (StudentEntity sge : studentEntities) {
            int is = sge.getInitSem().intValue();
            int diff = semester - is;
            if (semester % 2 != is % 2) diff++;
            if (diff / 2 + sge.getInitYear() == year)
                groups.add(map.get(sge.getUid()).getGroup());
        }

        return SubjLectsAndGroupsResponse.builder()
                .groups(groups)
                .subjLects(responseList)
                .build();
    }

    public void addMark(AddMarkRawsRequest addMarkRawsRequest) {
        if (!this.baseRequestValidationComponent.isValid(addMarkRawsRequest)) {
            throw new RequestNotValidException();
        }

        SubjLectEntity subjLectEntity = this.subjLectRepository.findById(addMarkRawsRequest.getSlUid())
                .orElseThrow(SubjLectNotFoundException::new);

        SubjectEntity subjectEntity = this.subjectRepository.findById(subjLectEntity.getSubjUid())
                .orElseThrow(SubjectNotFoundException::new);

        String period = subjLectEntity.getPeriod();

        Byte semester = subjectEntity.getSemester();

        int year = Integer.parseInt(period.split("-")[(semester + 1) % 2]);

        List<StudentGroupEntity> studentGroupEntities = this.studentGroupRepository.findAllByGroupAndSemester(
                addMarkRawsRequest.getGroup(),
                semester);

        List<StudentEntity> studentEntities = this.studentRepository.findAllById(studentGroupEntities.stream()
                .map(StudentGroupEntity::getStudUid)
                .collect(Collectors.toSet()));

        List<UUID> filteredStudents = new ArrayList<>();

        for (StudentEntity stud : studentEntities) {
            int is = stud.getInitSem().intValue();
            int diff = semester - is;
            if (semester % 2 != is % 2) diff++;
            if (diff / 2 + stud.getInitYear() == year)
                filteredStudents.add(stud.getUid());
        }

        List<MarkEntity> markEntities = this.markRepository.findAllByStudUidInAndSlUidEquals(
                filteredStudents,
                addMarkRawsRequest.getSlUid()
        );

        if (!markEntities.isEmpty()) {
            throw new MarkAlreadyExistsException();
        }

        List<MarkEntity> add = filteredStudents
                .stream()
                .map(x -> MarkEntity.builder()
                        .uid(UUID.randomUUID())
                        .slUid(addMarkRawsRequest.getSlUid())
                        .date(null)
                        .mark(null)
                        .studUid(x)
                        .build())
                .collect(Collectors.toList());

        this.markRepository.saveAll(add);
    }
}
