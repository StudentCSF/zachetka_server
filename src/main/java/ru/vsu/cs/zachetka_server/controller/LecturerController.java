package ru.vsu.cs.zachetka_server.controller;

import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.zachetka_server.model.dto.response.LecturerInfoResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.MainLecturerInfoResponse;
import ru.vsu.cs.zachetka_server.service.LecturerService;

import java.util.List;
import java.util.Map;
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

    @GetMapping("/lecturer/{user_lect_uid}")
    public MainLecturerInfoResponse getMainPage(
            @PathVariable("user_lect_uid") UUID uid
    ) {
        return this.lecturerService.getData(uid);
    }
}
