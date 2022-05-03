package ru.vsu.cs.zachetka_server.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.zachetka_server.service.LecturerService;

import java.util.UUID;

@RestController
public class LecturerController {

    private final LecturerService lecturerService;

    public LecturerController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @PostMapping("/lecturer/{user_uid}")
    public void addLecturer(@RequestBody String fio,
                            @PathVariable(value = "user_uid") UUID userUid
    ) {
        this.lecturerService.addLecturer(fio, userUid);
    }
}
