package ru.vsu.cs.zachetka_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.cs.zachetka_server.component.BaseRequestValidationComponent;
import ru.vsu.cs.zachetka_server.exception.*;
import ru.vsu.cs.zachetka_server.model.dto.request.AddStudentRequest;
import ru.vsu.cs.zachetka_server.model.dto.request.AddUserRequest;
import ru.vsu.cs.zachetka_server.model.dto.request.StudentRequest;
import ru.vsu.cs.zachetka_server.model.dto.response.student.MainStudentInfoResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.student.StudentInfoResponse;
import ru.vsu.cs.zachetka_server.model.entity.*;
import ru.vsu.cs.zachetka_server.model.enumerate.UserRole;
import ru.vsu.cs.zachetka_server.repository.*;

import java.util.*;

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

    public MainStudentInfoResponse getInfo(UUID userUid) {
        if (userUid == null) {
            throw new RequestNotValidException();
        }
        StudentEntity studentEntity = this.studentRepository.findByUserUid(userUid)
                .orElseThrow(StudentNotFoundException::new);

        List<MarkEntity> allMarks = this.markRepository.findAllByStudUid(studentEntity.getUid());

        Map<Byte, List<StudentInfoResponse>> result = new TreeMap<>();

        for (MarkEntity markEntity : allMarks) {
            SubjLectEntity subjLectEntity = this.subjLectRepository.findById(markEntity.getSlUid())
                    .orElseThrow(SubjLectNotFoundException::new);

            SubjectEntity subjectEntity = this.subjectRepository.findById(subjLectEntity.getSubjUid())
                    .orElseThrow(SubjectNotFoundException::new);

            LecturerEntity lecturerEntity = this.lecturerRepository.findById(subjLectEntity.getLectUid())
                    .orElseThrow(LecturerNotFoundException::new);

            if (!result.containsKey(subjectEntity.getSemester())) {
                result.put(subjectEntity.getSemester(), new ArrayList<>());
            }

            result.get(subjectEntity.getSemester()).add(
                    StudentInfoResponse.builder()
                            .date(markEntity.getDate())
                            .mark(markEntity.getMark())
                            .lectFio(lecturerEntity.getFio())
                            .subjName(subjectEntity.getName())
                            .build()
            );
        }

        return MainStudentInfoResponse.builder()
                .info(result)
                .fio(studentEntity.getFio())
                .build();
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

        if (this.studentRepository.findByFioAndInitYearAndInitSem(
                        addStudentRequest.getFio(),
                        addStudentRequest.getInitYear(),
                        addStudentRequest.getInitSem())
                .isPresent()
        ) {
            throw new StudentAlreadyExistsException();
        }

        this.studentRepository.save(StudentEntity.builder()
                .userUid(newUserUid)
                .uid(UUID.randomUUID())
                .fio(addStudentRequest.getFio())
                .initYear(addStudentRequest.getInitYear())
                .initSem(addStudentRequest.getInitSem())
                .build()
        );
    }
}
