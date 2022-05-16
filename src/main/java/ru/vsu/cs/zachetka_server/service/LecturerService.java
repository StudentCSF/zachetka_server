package ru.vsu.cs.zachetka_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.cs.zachetka_server.exception.*;
import ru.vsu.cs.zachetka_server.model.dto.request.AddLecturerRequest;
import ru.vsu.cs.zachetka_server.model.dto.request.AddUserRequest;
import ru.vsu.cs.zachetka_server.model.dto.response.lecturer.LecturerFirstResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.lecturer.LecturerInfoResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.lecturer.LecturerKeySubjectResponse;
import ru.vsu.cs.zachetka_server.model.entity.*;
import ru.vsu.cs.zachetka_server.model.enumerate.UserRole;
import ru.vsu.cs.zachetka_server.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LecturerService {

    private final LecturerRepository lecturerRepository;

    private final SubjLectRepository subjLectRepository;

    private final SubjectRepository subjectRepository;

    private final MarkRepository markRepository;

    private final StudentRepository studentRepository;

    private final StudentGroupRepository studentGroupRepository;


    private final UserService userService;

    @Autowired
    public LecturerService(LecturerRepository lecturerRepository,
                           SubjLectRepository subjLectRepository,
                           SubjectRepository subjectRepository,
                           MarkRepository markRepository,
                           StudentRepository studentRepository,
                           StudentGroupRepository studentGroupRepository,
                           UserService userService) {
        this.lecturerRepository = lecturerRepository;
        this.subjLectRepository = subjLectRepository;
        this.subjectRepository = subjectRepository;
        this.markRepository = markRepository;
        this.studentRepository = studentRepository;
        this.studentGroupRepository = studentGroupRepository;
        this.userService = userService;
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
                        .fio(lecturerFio.trim())
                        .uid(UUID.randomUUID())
                        .userUid(userUid)
                        .build()
        );
    }

    public LecturerFirstResponse getPeriods(UUID uid) {
        LecturerEntity lecturerEntity = this.lecturerRepository.findByUserUid(uid)
                .orElseThrow(LecturerNotFoundException::new);

        List<SubjLectEntity> subjLectEntity = this.subjLectRepository.findAllByLectUid(lecturerEntity.getUid());

        return LecturerFirstResponse.builder()
                .fio(lecturerEntity.getFio())
                .lectUid(lecturerEntity.getUid())
                .periods(subjLectEntity.stream()
                        .map(SubjLectEntity::getPeriod)
                        .sorted()
                        .collect(Collectors.toCollection(LinkedHashSet::new)))
                .build();
    }

    public List<LecturerKeySubjectResponse> getSubjects(UUID uid, String period) {
        List<SubjLectEntity> subjLectEntities = this.subjLectRepository.findAllByLectUidAndPeriod(uid, period);

        List<LecturerKeySubjectResponse> result = new ArrayList<>();

        for (SubjLectEntity subjLectEntity : subjLectEntities) {
            SubjectEntity subjectEntity = this.subjectRepository.findById(subjLectEntity.getSubjUid())
                    .orElseThrow(SubjectNotFoundException::new);
            result.add(LecturerKeySubjectResponse.builder()
                    .name(subjectEntity.getName())
                    .semester(subjectEntity.getSemester())
                    .evalType(subjLectEntity.getEvalType())
                    .slUid(subjLectEntity.getUid())
                    .build()
            );
        }
        return result;
    }

    public void addLecturer(AddLecturerRequest addLecturerRequest) {
        UUID newUserUid = this.userService.addUser(AddUserRequest.builder()
                .role(UserRole.LECTURER)
                .password(addLecturerRequest.getPassword())
                .login(addLecturerRequest.getLogin())
                .build());

        if (addLecturerRequest.getFio() == null || addLecturerRequest.getFio().length() == 0) {
            throw new RequestNotValidException();
        }

        this.lecturerRepository.save(LecturerEntity.builder()
                .userUid(newUserUid)
                .uid(UUID.randomUUID())
                .fio(addLecturerRequest.getFio())
                .build()
        );
    }

    public Set<Float> getGroups(UUID uid) {

        SubjLectEntity subjLectEntity = this.subjLectRepository.findById(uid)
                .orElseThrow(SubjLectNotFoundException::new);

        String period = subjLectEntity.getPeriod();

        SubjectEntity subjectEntity = this.subjectRepository.findById(subjLectEntity.getSubjUid())
                .orElseThrow(SubjectNotFoundException::new);

        Byte semester = subjectEntity.getSemester();

        int year = Integer.parseInt(period.split("-")[(semester + 1) % 2]);

        List<StudentEntity> studentEntities = this.studentRepository.findAllById(
                this.markRepository.findAllBySlUid(uid)
                        .stream()
                        .map(MarkEntity::getStudUid)
                        .collect(Collectors.toList()));

        List<StudentEntity> actual = new ArrayList<>();

        for (StudentEntity stud : studentEntities) {
            int is = stud.getInitSem().intValue();
            int diff = semester - is;
            if (semester % 2 != is % 2) diff++;
            if (diff / 2 + stud.getInitYear() == year)
                actual.add(stud);
        }

        return this.studentGroupRepository.findAllByStudUidInAndSemesterEquals(
                        actual.stream()
                                .map(StudentEntity::getUid)
                                .collect(Collectors.toList()),
                        semester)
                .stream()
                .map(StudentGroupEntity::getGroup)
                .collect(Collectors.toSet());
    }

    public List<LecturerInfoResponse> getTable(UUID uid, Float g) {
        Map<UUID, MarkEntity> markEntities = this.markRepository.findAllBySlUid(uid)
                .stream()
                .collect(Collectors.toMap(
                        MarkEntity::getStudUid,
                        x -> x
                ));

        SubjLectEntity subjLectEntity = this.subjLectRepository.findById(uid)
                .orElseThrow(SubjLectNotFoundException::new);

        String period = subjLectEntity.getPeriod();

        SubjectEntity subjectEntity = this.subjectRepository.findById(subjLectEntity.getSubjUid())
                .orElseThrow(SubjectNotFoundException::new);

        Byte semester = subjectEntity.getSemester();

        int year = Integer.parseInt(period.split("-")[(semester + 1) % 2]);

        List<StudentEntity> studentEntities = this.studentRepository.findAllById(
                this.markRepository.findAllBySlUid(uid)
                        .stream()
                        .map(MarkEntity::getStudUid)
                        .collect(Collectors.toList()));

        Map<UUID, StudentEntity> actual = new HashMap<>();

        for (StudentEntity stud : studentEntities) {
            int is = stud.getInitSem().intValue();
            int diff = semester - is;
            if (semester % 2 != is % 2) diff++;
            if (diff / 2 + stud.getInitYear() == year)
                actual.put(stud.getUid(), stud);
        }

        return this.studentGroupRepository.findAllByStudUidInAndGroupEqualsAndSemesterEquals(
                        actual.values()
                                .stream()
                                .map(StudentEntity::getUid)
                                .collect(Collectors.toList()),
                        g, semester)
                .stream()
                .map(StudentGroupEntity::getStudUid)
                .map(x -> LecturerInfoResponse.builder()
                        .examDate(markEntities.get(x).getDate() == null ? null :
                                markEntities.get(x).getDate().toString())
                        .studFio(actual.get(x).getFio())
                        .mark(markEntities.get(x).getMark())
                        .studUid(x)
                        .build())
                .sorted(Comparator.comparing(LecturerInfoResponse::getStudFio))
                .collect(Collectors.toList());
    }
}
