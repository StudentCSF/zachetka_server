package ru.vsu.cs.zachetka_server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.zachetka_server.model.dto.request.SubjectRequest;
import ru.vsu.cs.zachetka_server.service.SubjectService;

@RestController
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping("/subject")
    public void addSubject(@RequestBody SubjectRequest subjectRequest) {
        this.subjectService.addSubject(subjectRequest);
    }
}
