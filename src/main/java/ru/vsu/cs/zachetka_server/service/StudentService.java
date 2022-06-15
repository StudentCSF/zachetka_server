package ru.vsu.cs.zachetka_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.cs.zachetka_server.component.BaseRequestValidationComponent;
import ru.vsu.cs.zachetka_server.exception.RequestNotValidException;
import ru.vsu.cs.zachetka_server.exception.StudentAlreadyExistsException;
import ru.vsu.cs.zachetka_server.exception.UserNotFoundException;
import ru.vsu.cs.zachetka_server.model.dto.request.AddStudentRequest;
import ru.vsu.cs.zachetka_server.model.dto.request.AddUserRequest;
import ru.vsu.cs.zachetka_server.model.dto.response.student.StudentFirstResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.student.StudentInfoResponse;
import ru.vsu.cs.zachetka_server.model.entity.*;
import ru.vsu.cs.zachetka_server.model.enumerate.UserRole;
import ru.vsu.cs.zachetka_server.repository.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final MarkRepository markRepository;

    private final SubjLectRepository subjLectRepository;

    private final SubjectRepository subjectRepository;

    private final LecturerRepository lecturerRepository;

    private final StudentGroupRepository studentGroupRepository;

    private final UserService userService;

    private final BaseRequestValidationComponent baseRequestValidationComponent;

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          MarkRepository markRepository,
                          SubjLectRepository subjLectRepository,
                          SubjectRepository subjectRepository,
                          LecturerRepository lecturerRepository,
                          StudentGroupRepository studentGroupRepository,
                          UserService userService,
                          BaseRequestValidationComponent baseRequestValidationComponent) {
        this.studentRepository = studentRepository;
        this.markRepository = markRepository;
        this.subjLectRepository = subjLectRepository;
        this.subjectRepository = subjectRepository;
        this.lecturerRepository = lecturerRepository;
        this.studentGroupRepository = studentGroupRepository;
        this.userService = userService;
        this.baseRequestValidationComponent = baseRequestValidationComponent;
    }

    public void addStudent(AddStudentRequest addStudentRequest) {
        UUID newUserUid = this.userService.addUser(AddUserRequest.builder()
                .role(UserRole.STUDENT)
                .password(addStudentRequest.getPassword())
                .login(addStudentRequest.getLogin())
                .build());

        if (!this.baseRequestValidationComponent.isValid(addStudentRequest)) {
            throw new RequestNotValidException();
        }

//        if (this.studentRepository.findByFioAndInitYearAndInitSem(
//                        addStudentRequest.getFio().trim(),
//                        addStudentRequest.getInitYear(),
//                        addStudentRequest.getInitSem())
//                .isPresent()
//        ) {
//            throw new StudentAlreadyExistsException();
//        }

        this.studentRepository.save(StudentEntity.builder()
                .userUid(newUserUid)
                .uid(UUID.randomUUID())
                .fio(addStudentRequest.getFio().trim())
                .initYear(addStudentRequest.getInitYear())
                .initSem(addStudentRequest.getInitSem())
                .build()
        );
    }

    public StudentFirstResponse getSemesters(UUID uid) {
        StudentEntity student = this.studentRepository.findByUserUid(uid)
                .orElseThrow(UserNotFoundException::new);

        UUID studUid = student.getUid();

        List<Byte> sems = this.studentGroupRepository.findAllByStudUid(studUid)
                .stream()
                .map(StudentGroupEntity::getSemester)
                .filter(x -> !this.getInfo(studUid, x).isEmpty())
                .sorted()
                .collect(Collectors.toList());

        return StudentFirstResponse.builder()
                .studUid(studUid)
                .semesters(sems)
                .fio(student.getFio())
                .build();
    }

    public List<StudentInfoResponse> getInfo(UUID uid, Byte sem) {

        Map<UUID, MarkEntity> marks = this.markRepository.findAllByStudUid(uid)
                .stream()
                .collect(Collectors.toMap(
                        MarkEntity::getSlUid,
                        x -> x
                ));

        Map<UUID, SubjLectEntity> sls = this.subjLectRepository.findAllById(marks.values()
                        .stream()
                        .map(MarkEntity::getSlUid)
                        .collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(
                        SubjLectEntity::getSubjUid,
                        x -> x
                ));

        Map<UUID, SubjectEntity> subjs = this.subjectRepository.findAllById(sls.values()
                        .stream()
                        .map(SubjLectEntity::getSubjUid)
                        .collect(Collectors.toList()))
                .stream()
                .filter(x -> x.getSemester().equals(sem))
                .collect(Collectors.toList())
                .stream()
                .collect(Collectors.toMap(
                        SubjectEntity::getUid,
                        x -> x
                ));

        List<SubjLectEntity> actual = subjs.values().stream()
                .map(x -> sls.get(x.getUid()))
                .collect(Collectors.toList());

        Map<UUID, LecturerEntity> lects = this.lecturerRepository.findAllById(actual.stream()
                        .map(SubjLectEntity::getLectUid)
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(
                        LecturerEntity::getUid,
                        x -> x
                ));

        return actual.stream()
                .map(x -> StudentInfoResponse.builder()
                        .date(marks.get(x.getUid()).getDate())
                        .mark(marks.get(x.getUid()).getMark())
                        .lectFio(lects.get(x.getLectUid()).getFio())
                        .subjName(subjs.get(x.getSubjUid()).getName())
                        .build())
                .filter(x -> x.getDate() != null && x.getMark() != null)
                .sorted(Comparator.comparing(StudentInfoResponse::getMark)
                        .thenComparing(StudentInfoResponse::getSubjName))
                .collect(Collectors.toList());
    }
}
