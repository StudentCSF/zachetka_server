package ru.vsu.cs.zachetka_server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.zachetka_server.model.dto.request.AddSubjLectRequest;
import ru.vsu.cs.zachetka_server.model.dto.response.LecturersAndSubjectsResponse;
import ru.vsu.cs.zachetka_server.service.SubjLectService;

import java.util.SortedSet;

@RestController
public class SubjLectController {

    private final SubjLectService subjLectService;

    public SubjLectController(SubjLectService subjLectService) {
        this.subjLectService = subjLectService;
    }

    @PostMapping("/subjlect")
    public void addSubjLect(@RequestBody AddSubjLectRequest addSubjLectRequest) {
        this.subjLectService.addSubjLect(addSubjLectRequest);
    }

    @GetMapping("/subjlect")
    public LecturersAndSubjectsResponse getLecturersAndSubjects() {
        return this.subjLectService.getSubjectsAndLecturers();
    }

    @GetMapping("/subjlect/periods")
    public SortedSet<String> getPeriods() {
        return this.subjLectService.getPeriods();
    }
}
