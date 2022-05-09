package ru.vsu.cs.zachetka_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.cs.zachetka_server.component.BaseRequestValidationComponent;
import ru.vsu.cs.zachetka_server.exception.*;
import ru.vsu.cs.zachetka_server.model.dto.request.StudentRequest;
import ru.vsu.cs.zachetka_server.model.dto.response.MainStudentInfoResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.StudentInfoResponse;
import ru.vsu.cs.zachetka_server.model.entity.*;
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

    @Autowired
    public StudentService(StudentRepository studentRepository, MarkRepository markRepository, SubjLectRepository subjLectRepository, SubjectRepository subjectRepository, LecturerRepository lecturerRepository, BaseRequestValidationComponent baseRequestValidationComponent) {
        this.studentRepository = studentRepository;
        this.markRepository = markRepository;
        this.subjLectRepository = subjLectRepository;
        this.subjectRepository = subjectRepository;
        this.lecturerRepository = lecturerRepository;
        this.baseRequestValidationComponent = baseRequestValidationComponent;
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
}
