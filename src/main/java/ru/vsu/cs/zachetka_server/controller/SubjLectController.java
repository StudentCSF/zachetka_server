package ru.vsu.cs.zachetka_server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.zachetka_server.model.dto.request.SubjLectRequest;
import ru.vsu.cs.zachetka_server.service.SubjLectService;

@RestController
public class SubjLectController {

    private final SubjLectService subjLectService;

    public SubjLectController(SubjLectService subjLectService) {
        this.subjLectService = subjLectService;
    }

    @PostMapping("/subjlect")
    public void addSubjLect(@RequestBody SubjLectRequest subjLectRequest) {
        this.subjLectService.addSubjLect(subjLectRequest);
    }
}
