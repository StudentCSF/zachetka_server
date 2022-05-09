package ru.vsu.cs.zachetka_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.cs.zachetka_server.exception.*;
import ru.vsu.cs.zachetka_server.model.dto.response.LecturerInfoResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.LecturerKeySubjectResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.LecturerTableResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.MainLecturerInfoResponse;
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

    public MainLecturerInfoResponse getData(UUID userUid) {
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

        Map<LecturerKeySubjectResponse, LecturerTableResponse> result = new TreeMap<>(
                Comparator.comparing(LecturerKeySubjectResponse::getSemester)
                        .reversed()
                        .thenComparing(LecturerKeySubjectResponse::getName)
        );

        for (Map.Entry<SubjectEntity, List<MarkEntity>> kv : subjMarksMap.entrySet()) {
            Map<Float, List<LecturerInfoResponse>> value = new TreeMap<>();
            for (MarkEntity markEntity : kv.getValue()) {
                StudentEntity studentEntity = this.studentRepository.findById(markEntity.getStudUid())
                        .orElseThrow(StudentNotFoundException::new);
                if (!value.containsKey(studentEntity.getGroup())) {
                    value.put(studentEntity.getGroup(), new ArrayList<>());
                }
                value.get(studentEntity.getGroup()).add(
                        LecturerInfoResponse.builder()
                                .mark(markEntity.getMark())
                                .examDate(markEntity.getDate().toString())
                                .studFio(studentEntity.getFio())
                                .studUid(studentEntity.getUid())
                                .build());
            }
            for (List<LecturerInfoResponse> l : value.values())
                l.sort(Comparator.comparing(LecturerInfoResponse::getStudFio));

            result.put(LecturerKeySubjectResponse.builder()
                            .name(kv.getKey().getName())
                            .semester(kv.getKey().getSemester())
                            .slUid(this.subjLectRepository.findByLectUidAndSubjUid(
                                            lecturerEntity.getUid(),
                                            kv.getKey().getUid()
                                    )
                                    .orElseThrow(SubjectNotFoundException::new)
                                    .getUid())
                            .build()
                    , LecturerTableResponse.builder()
                            .table(value)
                            .build());
        }

        return MainLecturerInfoResponse.builder()
                .fio(lecturerEntity.getFio())
                .info(result)
                .build();
    }
}
