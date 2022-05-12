package ru.vsu.cs.zachetka_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.vsu.cs.zachetka_server.component.BaseRequestValidationComponent;
import ru.vsu.cs.zachetka_server.exception.*;
import ru.vsu.cs.zachetka_server.model.dto.request.AddStudentRequest;
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

    private final BaseRequestValidationComponent baseRequestValidationComponent;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    @Autowired
    public StudentService(StudentRepository studentRepository, MarkRepository markRepository, SubjLectRepository subjLectRepository, SubjectRepository subjectRepository, LecturerRepository lecturerRepository, BaseRequestValidationComponent baseRequestValidationComponent, UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.studentRepository = studentRepository;
        this.markRepository = markRepository;
        this.subjLectRepository = subjLectRepository;
        this.subjectRepository = subjectRepository;
        this.lecturerRepository = lecturerRepository;
        this.baseRequestValidationComponent = baseRequestValidationComponent;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public void addStudent(StudentRequest studentRequest, UUID userUid) {
        if (!this.baseRequestValidationComponent.isValid(studentRequest) || userUid == null) {
            throw new RequestNotValidException();
        }
        if (this.studentRepository.findByUserUid(userUid).isPresent()) {
            throw new StudentAlreadyExistsException();
        }
        this.studentRepository.save(
                StudentEntity.builder()
                        .course(studentRequest.getCourse())
                        .fio(studentRequest.getFio())
                        .group(studentRequest.getGroup())
                        .uid(UUID.randomUUID())
                        .userUid(userUid).build()
        );
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
        if (!this.baseRequestValidationComponent.isValid(addStudentRequest)) {
            throw new RequestNotValidException();
        }

        String login = addStudentRequest.getLogin();

        if (this.userRepository.findByLogin(login).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        UUID newUserUid = UUID.randomUUID();
        this.userRepository.save(UserEntity.builder()
                .uid(newUserUid)
                .role(UserRole.STUDENT)
                .password(this.encoder.encode(addStudentRequest.getPassword()))
                .login(login)
                .build()
        );

        this.studentRepository.save(StudentEntity.builder()
                .userUid(newUserUid)
                .uid(UUID.randomUUID())
                .group(addStudentRequest.getGroup())
                .fio(addStudentRequest.getFio())
                .course(addStudentRequest.getCourse())
                .build()
        );
    }
}
