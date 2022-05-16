package ru.vsu.cs.zachetka_server.controller;

import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.zachetka_server.model.dto.request.AddLecturerRequest;
import ru.vsu.cs.zachetka_server.model.dto.response.lecturer.LecturerFirstResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.lecturer.LecturerInfoResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.lecturer.LecturerKeySubjectResponse;
import ru.vsu.cs.zachetka_server.service.LecturerService;

import java.util.List;
import java.util.Set;
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
    public LecturerFirstResponse getMainPage(
            @PathVariable("user_lect_uid") UUID uid
    ) {
        return this.lecturerService.getPeriods(uid);
    }

    @GetMapping("/lecturer/subjects/{lect_uid}/{period}")
    public List<LecturerKeySubjectResponse> getSubjects(
            @PathVariable(value = "lect_uid") UUID uid,
            @PathVariable(value = "period") String period
    ) {
        return this.lecturerService.getSubjects(uid, period);
    }

    @GetMapping("/lecturer/groups/{sl_uid}")
    public Set<Float> getGroups(@PathVariable(value = "sl_uid") UUID uid) {
        return this.lecturerService.getGroups(uid);
    }

    @GetMapping("/lecturer/table/{sl_uid}/{group}")
    public List<LecturerInfoResponse> getTable(
            @PathVariable(value = "sl_uid") UUID uid,
            @PathVariable(value = "group") Float group
    ) {
        return this.lecturerService.getTable(uid, group);
    }

    @PostMapping("/lecturer")
    public void addLecturer(@RequestBody AddLecturerRequest addLecturerRequest) {
        this.lecturerService.addLecturer(addLecturerRequest);
    }
}
