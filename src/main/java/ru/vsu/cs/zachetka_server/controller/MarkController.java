package ru.vsu.cs.zachetka_server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.zachetka_server.model.dto.request.MarkRequest;
import ru.vsu.cs.zachetka_server.service.MarkService;

@RestController
public class MarkController {

    private final MarkService markService;

    public MarkController(MarkService markService) {
        this.markService = markService;
    }

    @PostMapping("/mark")
    public void addMark(@RequestBody MarkRequest markRequest) {
        this.markService.addMark(markRequest);
    }
}
