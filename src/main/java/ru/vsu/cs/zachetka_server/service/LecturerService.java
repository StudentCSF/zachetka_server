package ru.vsu.cs.zachetka_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.cs.zachetka_server.exception.*;
import ru.vsu.cs.zachetka_server.model.dto.response.StudentRawResponse;
import ru.vsu.cs.zachetka_server.model.entity.*;
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

    @Autowired
    public LecturerService(LecturerRepository lecturerRepository,
                           SubjLectRepository subjLectRepository,
                           SubjectRepository subjectRepository,
                           MarkRepository markRepository,
                           StudentRepository studentRepository) {
        this.lecturerRepository = lecturerRepository;
        this.subjLectRepository = subjLectRepository;
        this.subjectRepository = subjectRepository;
        this.markRepository = markRepository;
        this.studentRepository = studentRepository;
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
                        .fio(lecturerFio)
                        .uid(UUID.randomUUID())
                        .userUid(userUid)
                        .build()
        );
    }

    public Map<String, Map<Float, List<StudentRawResponse>>> getData(UUID userUid) {
        LecturerEntity lecturerEntity = this.lecturerRepository.findByUserUid(userUid)
                .orElseThrow(LecturerNotFoundException::new);

        List<SubjLectEntity> lectsSubjs = this.subjLectRepository.findAllByLectUid(lecturerEntity.getUid());

        Map<SubjectEntity, List<MarkEntity>> subjMarksMap = lectsSubjs.stream()
                .collect(Collectors.toMap(
                                x -> this.subjectRepository.findById(x.getSubjUid())
                                        .orElseThrow(SubjectNotFoundException::new),
                                x -> this.markRepository.findAllBySlUid(x.getUid())
                        )
                );

        Map<String, Map<Float, List<StudentRawResponse>>> result = new TreeMap<>();

        for (Map.Entry<SubjectEntity, List<MarkEntity>> kv : subjMarksMap.entrySet()) {
            Map<Float, List<StudentRawResponse>> value = new TreeMap<>();
            for (MarkEntity markEntity : kv.getValue()) {
                StudentEntity studentEntity = this.studentRepository.findById(markEntity.getStudUid())
                        .orElseThrow(StudentNotFoundException::new);
                if (!value.containsKey(studentEntity.getGroup())) {
                    value.put(studentEntity.getGroup(), new ArrayList<>());
                }
                value.get(studentEntity.getGroup()).add(
                        StudentRawResponse.builder()
                                .mark(markEntity.getMark())
                                .examDate(markEntity.getDate())
                                .studFio(studentEntity.getFio())
                                .studUid(studentEntity.getUid())
                                .build());
            }
            for (List<StudentRawResponse> l : value.values())
                l.sort(Comparator.comparing(StudentRawResponse::getStudFio));

            result.put(String.format("%s (%s семестр)",
                    kv.getKey().getName(),
                    kv.getKey().getSemester()
            ), value);
        }

        return result;
    }
}
